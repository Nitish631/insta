package com.nitish.insta.Service;

import com.nitish.insta.Entities.Device;
import com.nitish.insta.Payloads.UsersDto;

import java.util.List;

public interface UsersService {
    UsersDto registerNewUser(UsersDto user,Device device);
    UsersDto updateUser(UsersDto user,Long userId);
    UsersDto getUser(Long userId);
    void loginUser(Device device,String email);
    List<UsersDto> getAllUsers();
    void deleteUser(Long userId);
    void googleLoginRegistration(String email,String providerId,String username,Device device);
    String changeUserName(String userName,String email);
    String changeFullName(String fullName,String email);
}
