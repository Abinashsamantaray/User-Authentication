package com.project.User_Authentication.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;



@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendOtpEmail(String to, String otp) {
        Context context = new Context();
        context.setVariable("otp", otp);
        String htmlContent = templateEngine.process("otp-email", context);

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("abinashsamantaray01@gmail.com");
            messageHelper.setTo(to);
            messageHelper.setSubject("Your OTP Code");
            messageHelper.setText(htmlContent, true);
        };

        mailSender.send(messagePreparator);
    }
}
