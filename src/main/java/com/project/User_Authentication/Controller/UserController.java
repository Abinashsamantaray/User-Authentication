package com.project.User_Authentication.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.User_Authentication.DTO.ForgetPasswordDTO;
import com.project.User_Authentication.DTO.OTPVerificationDTO;
import com.project.User_Authentication.DTO.PasswordResetDTO;
import com.project.User_Authentication.DTO.UserRegistrationDTO;
import com.project.User_Authentication.Exception.CustomAuthException;
import com.project.User_Authentication.Exception.InvalidOTPException;
import com.project.User_Authentication.Service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class UserController {
		
		@Autowired
		private UserService userService;
		
		// End-point to register the user
		@PostMapping("/register-user")
		public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDTO request){
			String registerUser  = userService.registerUser(request);
			if(registerUser.equals("User register Successfully!!")) {
				return ResponseEntity.ok(registerUser);
			}else {
				 return ResponseEntity.badRequest().body(registerUser);
			}
		}
		
		
		
		// End-point to reset password with old password .
		@PostMapping("/reset-password")
		public ResponseEntity<String> resetPassword(@Valid @RequestBody PasswordResetDTO request)
				throws CustomAuthException {
			userService.resetPassword(request);
			return ResponseEntity.ok("Password Reset Successfully");
		}
		
		// End-point to initiate forget password (send otp)
		@PostMapping("/forget-password")
		public ResponseEntity<String> forgetPassword(@Valid @RequestBody ForgetPasswordDTO request)
				throws CustomAuthException {
			userService.forgotPassword(request);
			return ResponseEntity.ok("OTP has been send to your email");
		}
		
		//End-point to verify OTP and send new Password 
		@PostMapping("/verify-otp")
		public ResponseEntity<String> verifyOTP(@Valid @RequestBody OTPVerificationDTO request) throws CustomAuthException, InvalidOTPException{
			userService.verifyOtpAndResetPassword(request);
			return ResponseEntity.ok("Password has been reset successfully");
		}
		
}
