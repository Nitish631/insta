package com.nitish.insta.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.nitish.insta.Configuration.AppConstant;
@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendMessageToEmail(String toEmail, String mes,boolean forOtp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            String subject=forOtp?"Your OTP code":"New Login Notification";
            String text=forOtp?String.format("Your OTP for registration in %s is: %s",AppConstant.APP_MAME,mes):String.format("New login to your account in %s. If this wasn't you, please secure your account.",AppConstant.APP_MAME);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("FAILED TO SEND EMAIL");
            throw new RuntimeException("Failed to send the OTP. Please check the email address.");
        }
    }

}
