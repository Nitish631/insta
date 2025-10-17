package com.nitish.insta.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthMessaging {
    @Autowired
    private FCMService fcmService;
    public void sendMessage(){
        
    }
}
