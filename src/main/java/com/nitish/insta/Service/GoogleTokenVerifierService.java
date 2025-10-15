package com.nitish.insta.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
@Service
public class GoogleTokenVerifierService {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String CLIENT_ID;

    public GoogleIdToken.Payload verifyToken(String idTokenString)throws GeneralSecurityException,IOException{
        GoogleIdTokenVerifier verifier=new GoogleIdTokenVerifier
        .Builder(new NetHttpTransport(),new JacksonFactory())
        .setAudience(Collections.singletonList(CLIENT_ID))
        .build();

        GoogleIdToken idToken=verifier.verify(idTokenString);
        if(idToken!=null){
            return idToken.getPayload();
        }else{
            throw new RuntimeException("Unable to login. Try again.");
        }

    }
   
}
