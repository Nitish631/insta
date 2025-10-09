package com.nitish.insta.Security;

import com.nitish.insta.Entities.Users;
import com.nitish.insta.Exception.ResourceNotFoundException;
import com.nitish.insta.Repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UsersRepo usersRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user=this.usersRepo.findByEmail(username)
                .orElseThrow(()->new ResourceNotFoundException("User","email",username));
        return new UserPrincipal(user);
    }
}
