package com.nitish.insta.Configuration;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;

@Service
public class FirebaseConfig {
    @PostConstruct
    public void initialize() throws IOException{
        try (InputStream serviceAccount =getClass().getClassLoader().getResourceAsStream("firebase-service-account.json")){
            if(serviceAccount==null){
                throw new IOException("Firebase service account file not found!");
            }
            FirebaseOptions options=FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();
            if(FirebaseApp.getApps().isEmpty()){
                FirebaseApp.initializeApp(options);
            }
            
        }
    }
}
