package com.nitish.insta.Controller;

import com.nitish.insta.Exception.ApiException;
import com.nitish.insta.Payloads.UsersDto;
import com.nitish.insta.Security.JwtTokenHelper;
import com.nitish.insta.Service.UsersService;
import com.nitish.insta.Utils.ApiResponse;
import com.nitish.insta.Utils.JwtAuthRequest;
import com.nitish.insta.Utils.JwtAuthResponse;
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
    private JwtTokenHelper jwtTokenHelper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UsersService usersService;
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request)throws  Exception{
        this.authenticate(request.getUsername(),request.getPassword());
        UserDetails userDetails=this.userDetailsService.loadUserByUsername(request.getUsername());
        String token=this.jwtTokenHelper.generateToken(userDetails);
        return ResponseEntity.ok(new JwtAuthResponse(token));
    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody UsersDto usersDto)throws Exception{
        UsersDto registeredUser=this.usersService.registerNewUser(usersDto);
        return new ResponseEntity<ApiResponse>( new ApiResponse("user successfully register"), HttpStatus.CREATED);
    }
    private void authenticate(String username,String password) throws Exception{
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                new UsernamePasswordAuthenticationToken(username,password);
        try{
            this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }catch (BadCredentialsException e){
            System.out.println("Invalid Details !!");
            throw new ApiException("Invalid username or password");
        }
    }
}
