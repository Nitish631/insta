package com.nitish.insta.Security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus((HttpServletResponse.SC_UNAUTHORIZED));
        response.setContentType("application/json:charset=UTF-8");
        String message=authException==null?"Unauthorized":authException.getMessage();
        String json = String.format("{\"timestamp\":%d,\"status\":401,\"error\":\"Unauthorized\",\"message\":\"%s\",\"path\":\"%s\"}", System.currentTimeMillis(), message, request.getRequestURI());
        response.getWriter().write(json);
    }
}
