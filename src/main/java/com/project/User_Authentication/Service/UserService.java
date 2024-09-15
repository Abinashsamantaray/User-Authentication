package com.project.User_Authentication.Service;


import java.security.SecureRandom;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.User_Authentication.DTO.ForgetPasswordDTO;
import com.project.User_Authentication.DTO.OTPVerificationDTO;
import com.project.User_Authentication.DTO.PasswordResetDTO;
import com.project.User_Authentication.DTO.UserRegistrationDTO;
import com.project.User_Authentication.Entity.User;
import com.project.User_Authentication.Exception.CustomAuthException;
import com.project.User_Authentication.Exception.InvalidOTPException;
import com.project.User_Authentication.Repository.UserRepository;

@Service
public class UserService {

    private static final String OTP_CACHE = "otpCache";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CacheManager cacheManager;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private SecureRandom random = new SecureRandom();
    
    public String registerUser(UserRegistrationDTO dto) {
    	Optional<User> existingUser = userRepository.findByEmail(dto.getEmail());
    	if(existingUser.isPresent()) {
    		return "User with this email ID already exist";
    	}
    	User user = new User();
    	user.setEmail(dto.getEmail());
    	user.setPassword( passwordEncoder.encode(dto.getPassword()));
    	userRepository.save(user);
    	return "User register Successfully!!";
    }

    public void resetPassword(PasswordResetDTO request) throws CustomAuthException {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if(userOpt.isEmpty()) {
            throw new CustomAuthException("User not found");
        }

        User user = userOpt.get();
        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new CustomAuthException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void forgotPassword(ForgetPasswordDTO request) throws CustomAuthException {
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if(userOpt.isEmpty()) {
            throw new CustomAuthException("User not found");
        }

        String otp = generateOtp();
        Cache cache = cacheManager.getCache(OTP_CACHE);
        if(cache != null) {
            cache.put(request.getEmail(), otp);
        }

        emailService.sendOtpEmail(request.getEmail(), otp);
    }

    public void verifyOtpAndResetPassword(OTPVerificationDTO request) throws CustomAuthException, InvalidOTPException {
        Cache cache = cacheManager.getCache(OTP_CACHE);
        if(cache == null) {
            throw new CustomAuthException("OTP service is not available");
        }

        String cachedOtp = cache.get(request.getEmail(), String.class);
        if(cachedOtp == null) {
            throw new InvalidOTPException("OTP expired or not found");
        }

        if(!cachedOtp.equals(request.getOtp())) {
            throw new InvalidOTPException("Invalid OTP");
        }

        // OTP is valid, reset password
        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());
        if(userOpt.isEmpty()) {
            throw new CustomAuthException("User not found");
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // Remove OTP from cache
        cache.evict(request.getEmail());
    }

    private String generateOtp() {
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }
}
