package com.nitish.insta.Controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.nitish.insta.Entities.Device;
import com.nitish.insta.Entities.Users;
import com.nitish.insta.Exception.ApiException;
import com.nitish.insta.Exception.ResourceNotFoundException;
import com.nitish.insta.Payloads.EmailRequestDto;
import com.nitish.insta.Payloads.OtpRequestDto;
import com.nitish.insta.Payloads.PasswordRequestDto;
import com.nitish.insta.Payloads.PasswordRequestRegisterUserDto;
import com.nitish.insta.Payloads.UserPasswordDto;
import com.nitish.insta.Repository.UsersRepo;
import com.nitish.insta.Security.JwtTokenHelper;
import com.nitish.insta.Service.EmailService;
import com.nitish.insta.Service.GoogleTokenVerifierService;
import com.nitish.insta.Service.NotificationForAuthentication;
import com.nitish.insta.Service.OtpService;
import com.nitish.insta.Service.UsersService;
import com.nitish.insta.Utils.ApiResponse;
import com.nitish.insta.Utils.JwtAuthRequest;
import com.nitish.insta.Utils.JwtAuthResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private UsersService usersService;
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private OtpService otpService;
    @Autowired
    private GoogleTokenVerifierService googleTokenVerifierService;
    @Autowired
    private NotificationForAuthentication notificationForAuthentication;
    @Autowired
    private EmailService emailService;

    @PostMapping("/google_auth") // YAHA
    public ResponseEntity<JwtAuthResponse> googleRegisterLogin(@Valid @RequestBody JwtAuthRequest emailToken)
            throws Exception {
                System.out.println("Google auth is triggred");
        Payload payload = googleTokenVerifierService.verifyToken(emailToken.getPassword());
        String email = payload.getEmail();
        String providerId = payload.getSubject();
        String username = (String) payload.get("name");
        if (email == null || providerId == null) {
            throw new ApiException("Unable to login with google. Try again.");
        }
        if (!email.equals(emailToken.getUsername())) {
            throw new ApiException("Email id's do not match. Try again.");
        }
        Device device = new Device();
        device.setDeviceId(emailToken.getDeviceId());
        device.setDeviceName(emailToken.getDeviceName());
        device.setNotificationToken(emailToken.getNotificationToken());
        device.setOs(emailToken.getOs());
        notificationForAuthentication.send(emailToken.getNotificationToken(), email, username,
                "You login to our app . If its not please report", "Google login notification");
        String paswrod = emailToken.getPassword().substring(0, 50);
        this.usersService.googleLoginRegistration(email, paswrod, username, device);
        String jwtToken = jwtTokenHelper.generateToken(email);
        return ResponseEntity.ok(new JwtAuthResponse(jwtToken));
    }

    @PostMapping("/login") // YAHA
    public ResponseEntity<JwtAuthResponse> liginUser(@Valid @RequestBody JwtAuthRequest request) throws Exception {
        this.authenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
        Device device = new Device();
        device.setDeviceId(request.getDeviceId());
        device.setDeviceName(request.getDeviceName());
        device.setNotificationToken(request.getNotificationToken());
        device.setOs(request.getOs());
        this.usersService.loginUser(device, request.getUsername());
        Users user = this.usersRepo.findByEmail(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getUsername()));
        String token = this.jwtTokenHelper.generateToken(userDetails.getUsername());
        notificationForAuthentication.send(request.getNotificationToken(), request.getUsername(), user.getFullName(),
                "You login to our app . If its not please report", "Login notification");
        this.emailService.sendMessageToEmail(request.getUsername(), "", false);
        return ResponseEntity.ok(new JwtAuthResponse(token));
    }

    @PostMapping("/login-auto")
    public ResponseEntity<JwtAuthResponse> createToken(@Valid @RequestBody UserPasswordDto request) throws Exception {
        this.authenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
        String token = this.jwtTokenHelper.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(new JwtAuthResponse(token));
    }

    @PostMapping("/register") // YAHA
    public ResponseEntity<JwtAuthResponse> registerUser(@Valid @RequestBody PasswordRequestRegisterUserDto request)
            throws Exception {
        Device device = new Device();
        device.setDeviceId(request.getDeviceId());
        device.setDeviceName(request.getDeviceName());
        device.setNotificationToken(request.getNotificationToken());
        device.setOs(request.getOs());
        this.otpService.setPassword(device, request.getOtpToken(), request.getEmail(), request.getPassword(),
                request.getFullName());
        this.authenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.jwtTokenHelper.generateToken(userDetails.getUsername());
        Users user = this.usersRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));
        notificationForAuthentication.send(request.getNotificationToken(), request.getEmail(), user.getFullName(),
                "You Registered to our app . If its not please report", "New registration notification");
        this.emailService.sendMessageToEmail(request.getEmail(), "", false);
        return new ResponseEntity(new JwtAuthResponse(token), HttpStatus.CREATED);
    }

    @PostMapping("/request-otp")
    public ResponseEntity<ApiResponse> requestOtp(@Valid @RequestBody EmailRequestDto request) {
        String message = otpService.sendOtp(request.getEmail(), request.isForReset());
        return ResponseEntity.ok(new ApiResponse(message));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> veriryOtp(@Valid @RequestBody OtpRequestDto request) {
        System.out.println("VERIFY OTP CALLED");
        String message = otpService.verifyOtp(request.getOtpToken(), request.getEmail(), request.getOtp());
        return ResponseEntity.ok(new ApiResponse(message));
    }

    @PostMapping("/forgot-password") // YAHA
    public ResponseEntity<JwtAuthResponse> forgotpassword(@Valid @RequestBody PasswordRequestDto request)
            throws Exception {
        Device device = new Device();
        device.setDeviceId(request.getDeviceId());
        device.setDeviceName(request.getDeviceName());
        device.setNotificationToken(request.getNotificationToken());
        device.setOs(request.getOs());
        this.otpService.setPassword(device, request.getOtpToken(), request.getEmail(), request.getPassword(), null);
        JwtAuthRequest request2 = new JwtAuthRequest();
        request2.setPassword(request.getPassword());
        request2.setUsername(request.getEmail());
        Users user = this.usersRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));
        notificationForAuthentication.send(request.getNotificationToken(), request.getEmail(), user.getFullName(),
                "You Registered to our app . If its not please report", "New registration notification");
        return liginUser(request2);
    }

    private void authenticate(String username, String password) throws Exception {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                username, password);
        try {
            this.usersRepo.findByEmail(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", username));
            this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (BadCredentialsException e) {
            throw new ApiException("Invalid password");
        }
    }
}
