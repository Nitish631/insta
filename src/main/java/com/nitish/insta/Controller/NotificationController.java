package com.nitish.insta.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nitish.insta.Entities.NotificationRequest;
import com.nitish.insta.Service.FCMService;
@RestController
@RequestMapping("/api/notify")
public class NotificationController {
     @Autowired
    private FCMService fcmService;

    @PostMapping
    public String sendNotification(@RequestBody NotificationRequest request) {
        return fcmService.sendNotification(request);
    }
}
