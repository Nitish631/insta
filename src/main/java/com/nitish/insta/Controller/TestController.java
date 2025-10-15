package com.nitish.insta.Controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class TestController {
    @GetMapping("/user")
    public Object greet(@AuthenticationPrincipal OAuth2User principal) {
        return "Success";
    }

    

}
