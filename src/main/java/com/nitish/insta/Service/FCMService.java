package com.nitish.insta.Service;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.nitish.insta.Entities.NotificationRequest;

@Service
public class FCMService {
    public String sendNotification(NotificationRequest request){
        try{
            Notification notification=Notification.builder()
            .setTitle(request.getTitle())
            .setBody(request.getBody())
            .build();
            Message message=Message.builder()
            .setToken(request.getTargetToken())
            .setNotification(notification)
            .build();
            String response=FirebaseMessaging.getInstance().send(message);
            return response;
        }catch(Exception e){
            return "Error sending FCM:"+e.getMessage();
        }
    }
}
