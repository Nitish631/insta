package com.nitish.insta.Service;

import com.nitish.insta.Payloads.UsersDto;

import java.util.List;

public interface UsersService {
    UsersDto registerNewUser(UsersDto user);
    UsersDto updateUser(UsersDto user,Integer userId);
    UsersDto getUser(Integer userId);
    List<UsersDto> getAllUsers();
    void deleteUser(Integer userId);
    void googleLoginRegistration(String email,String providerId,String username);
}
