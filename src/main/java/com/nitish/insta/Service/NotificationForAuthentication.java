package com.nitish.insta.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nitish.insta.Entities.NotificationRequest;

@Service
public class NotificationForAuthentication {
    @Autowired
    private FCMService fcmService;
    public void send(String token,String email,String fullName,String body,String title){
        String boddy=String.format("By: %s \n email:%s \n message:%s", fullName,email,body);
        NotificationRequest request=new NotificationRequest();
        request.setBody(boddy);
        request.setTargetToken(token);
        request.setTitle(title);
        this.fcmService.sendNotification(request);
    }
}
