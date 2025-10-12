package com.nitish.insta.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.nitish.insta.Utils.AppConstant;
@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Your OTP code");
            message.setText(String.format("Your OTP for registration in %s is: %s", AppConstant.APP_MAME, otp));
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send the OTP. Please check the email address.");
        }
    }

}
