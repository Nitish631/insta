package com.nitish.insta.Controller;

import com.nitish.insta.Payloads.UpdateNameDto;
import com.nitish.insta.Service.UsersService;
import com.nitish.insta.Utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserControllers {
    @Autowired
    private UsersService usersService;
    @PostMapping("/updateUserName")
    public ResponseEntity<ApiResponse>updateUserName(@Valid @RequestBody  UpdateNameDto request,@AuthenticationPrincipal
    UserDetails userDetails) {
        String currentUserEmail = userDetails.getUsername();
        usersService.changeUserName(request.getName(), currentUserEmail);
        return ResponseEntity.ok(new ApiResponse("Username updated successfully"));
    }
    @PostMapping("/updateFullName")
    public ResponseEntity<ApiResponse>updateFullName(@Valid @RequestBody  UpdateNameDto request,@AuthenticationPrincipal UserDetails userDetails){
        String currentUserEmail = userDetails.getUsername();
        usersService.changeFullName(request.getName(), currentUserEmail);
        return ResponseEntity.ok(new ApiResponse("FullName updated successfully"));
    }
}
