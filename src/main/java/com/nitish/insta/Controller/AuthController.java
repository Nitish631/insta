package com.nitish.insta.Controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.nitish.insta.Exception.ApiException;
import com.nitish.insta.Exception.ResourceNotFoundException;
import com.nitish.insta.Payloads.EmailRequestDto;
import com.nitish.insta.Payloads.OtpRequestDto;
import com.nitish.insta.Payloads.PasswordRequestDto;
import com.nitish.insta.Payloads.PasswordRequestRegisterUserDto;
import com.nitish.insta.Repository.UsersRepo;
import com.nitish.insta.Security.JwtTokenHelper;
import com.nitish.insta.Service.EmailService;
import com.nitish.insta.Service.GoogleTokenVerifierService;
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
    private EmailService emailService;
    @PostMapping("/google_auth")
    public ResponseEntity<JwtAuthResponse> googleRegisterLogin(@Valid @RequestBody JwtAuthRequest emailToken) throws Exception {
        Payload payload=googleTokenVerifierService.verifyToken(emailToken.getPassword());
        String email=payload.getEmail();
        String providerId=payload.getSubject();
        String username=(String) payload.get("name");
        if(email==null || providerId==null){
            throw new ApiException("Unable to login with google. Try again.");
        }
        if(email!=emailToken.getUsername()){
            throw new ApiException("Email id's do not match. Try again.");
        }
        this.usersService.googleLoginRegistration(email, emailToken.getPassword(), username);
        String jwtToken=jwtTokenHelper.generateToken(email);
        return ResponseEntity.ok(new JwtAuthResponse(jwtToken));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@Valid @RequestBody JwtAuthRequest request) throws Exception {
        this.authenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getUsername());
        String token = this.jwtTokenHelper.generateToken(userDetails.getUsername());
        this.emailService.sendMessageToEmail(request.getUsername(),"",false);
        return ResponseEntity.ok(new JwtAuthResponse(token));
    }
//    @PostMapping("/login-email")
//    public ResponseEntity<JwtAuthResponse> loginWithEmail(@RequestBody String email)throws ResourceNotFoundException{
//        this.usersRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User","email",email));
//        String token=this.jwtTokenHelper.generateToken(email);
//        return ResponseEntity.ok(new JwtAuthResponse(token));
//    }
//    @PostMapping("/login-password")
//    public ResponseEntity<JwtAuthResponse> loginWithPassword(@RequestBody ManualLoginRequest request)throws Exception{
//        Boolean valid=this.jwtTokenHelper.validateToken(request.getToken(),request.getUsername());
//        if(!valid){
//            throw new ApiException("Invalid token");
//        }
//        this.authenticate(request.getUsername(), request.getPassword());
//        String token=this.jwtTokenHelper.generateToken(request.getUsername());
//        return ResponseEntity.ok(new JwtAuthResponse(token));
//    }
    @PostMapping("/register")
    public ResponseEntity<JwtAuthResponse> registerUser(@Valid @RequestBody PasswordRequestRegisterUserDto request)
            throws Exception {
                System.out.println("EMAIL:"+request.getEmail());
        this.otpService.setPassword(request.getOtpToken(), request.getEmail(), request.getPassword(),
                request.getFullName());
        this.authenticate(request.getEmail(), request.getPassword());
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(request.getEmail());
        String token = this.jwtTokenHelper.generateToken(userDetails.getUsername());
        this.emailService.sendMessageToEmail(request.getEmail(),"",false);
        return new ResponseEntity(new JwtAuthResponse(token), HttpStatus.CREATED);
    }

    @PostMapping("/request-otp")
    public ResponseEntity<ApiResponse> requestOtp(@Valid @RequestBody EmailRequestDto request) {
        String message = otpService.sendOtp(request.getEmail(),request.isForReset());
        return ResponseEntity.ok(new ApiResponse(message));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> veriryOtp(@Valid @RequestBody OtpRequestDto request) {
        System.out.println("VERIFY OTP CALLED");
        String message = otpService.verifyOtp(request.getOtpToken(), request.getEmail(), request.getOtp());
        return ResponseEntity.ok(new ApiResponse(message));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<JwtAuthResponse> forgotpassword(@Valid @RequestBody PasswordRequestDto request)throws Exception {
        this.otpService.setPassword(request.getOtpToken(), request.getEmail(), request.getPassword(), null);
        JwtAuthRequest request2 = new JwtAuthRequest();
        request2.setPassword(request.getPassword());
        request2.setUsername(request.getEmail());
        return createToken(request2);
    }

    private void authenticate(String username, String password) throws Exception {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                username, password);
        try {
            this.usersRepo.findByEmail(username).orElseThrow(()->new ResourceNotFoundException("User","email",username));
            this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch (BadCredentialsException e) {
            throw new ApiException("Invalid password");
        }
    }
}
