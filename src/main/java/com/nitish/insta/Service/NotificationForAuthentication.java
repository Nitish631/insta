package com.nitish.insta.Service;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nitish.insta.Entities.NotificationRequest;

@Service
public class NotificationForAuthentication {
    @Autowired
    private FCMService fcmService;
    private final ScheduledExecutorService scheduler=Executors.newScheduledThreadPool(1);
    public void send(String token, String email, String fullName, String body, String title) {
        String boddy = String.format("By: %s \n email:%s \n message:%s", fullName, email, body);
        NotificationRequest request = new NotificationRequest();
        request.setBody(boddy);
        request.setTargetToken(token);
        request.setTitle(title);
        Runnable task = () -> {
            this.fcmService.sendNotification(request);
        };
        scheduler.schedule(task, 3, TimeUnit.MINUTES);
    }
}
