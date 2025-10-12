package com.nitish.insta.Controller;

import com.nitish.insta.Exception.ApiException;
import com.nitish.insta.Payloads.EmailRequestDto;
import com.nitish.insta.Payloads.OtpRequestDto;
import com.nitish.insta.Payloads.PasswordRequestDto;
import com.nitish.insta.Payloads.PasswordRequestRegisterUserDto;
// import com.nitish.insta.Payloads.UsersDto;
import com.nitish.insta.Security.JwtTokenHelper;
import com.nitish.insta.Service.OtpService;
// import com.nitish.insta.Service.UsersService;
import com.nitish.insta.Utils.ApiResponse;
import com.nitish.insta.Utils.JwtAuthRequest;
import com.nitish.insta.Utils.JwtAuthResponse;
// import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private OtpService oauth2Services;

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) throws Exception {
        this.authenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
        String token = this.jwtTokenHelper.generateToken(userDetails);
        return ResponseEntity.ok(new JwtAuthResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtAuthResponse> registerUser(@RequestBody PasswordRequestRegisterUserDto request)
            throws Exception {
        this.oauth2Services.setPassword(request.getOtpToken(), request.getEmail(), request.getPassword(),
                request.getFullName());
        JwtAuthRequest request2 = new JwtAuthRequest();
        request2.setPassword(request.getPassword());
        request2.setUsername(request.getEmail());
        return createToken(request2);
    }

    @PostMapping("/request-otp")
    public ResponseEntity<ApiResponse> requestOtp(@RequestBody EmailRequestDto request) {
        String message = oauth2Services.sendOtp(request.getEmail(),request.forReset);
        return ResponseEntity.ok(new ApiResponse(message));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> veriryOtp(@RequestBody OtpRequestDto request) {
        String message = oauth2Services.verifyOtp(request.getOtpToken(), request.getEmail(), request.getOtp());
        return ResponseEntity.ok(new ApiResponse(message));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<JwtAuthResponse> forgotpassword(@RequestBody PasswordRequestDto request)throws Exception {
        this.oauth2Services.setPassword(request.getOtpToken(), request.getEmail(), request.getPassword(), null);
        JwtAuthRequest request2 = new JwtAuthRequest();
        request2.setPassword(request.getPassword());
        request2.setUsername(request.getEmail());
        return createToken(request2);
    }

    private void authenticate(String username, String password) throws Exception {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                username, password);
        try {
            this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (BadCredentialsException e) {
            System.out.println("Invalid Details !!");
            throw new ApiException("Invalid username or password");
        }
    }
}
