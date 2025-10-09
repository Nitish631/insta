package com.nitish.insta.ServiceImpl;

import com.nitish.insta.Entities.Role;
import com.nitish.insta.Entities.Users;
import com.nitish.insta.Exception.ResourceNotFoundException;
import com.nitish.insta.Payloads.UsersDto;
import com.nitish.insta.Repository.RoleRepo;
import com.nitish.insta.Repository.UsersRepo;
import com.nitish.insta.Service.UsersService;
import com.nitish.insta.Utils.AppConstant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public UsersDto registerNewUser(UsersDto usersDto) {
        Users user=this.modelMapper.map(usersDto,Users.class);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        Role role=this.roleRepo.findById(AppConstant.USER_ROLE_ID).get();
        user.getRoles().add(role);
        Users registeredUser=this.usersRepo.save(user);
        return this.modelMapper.map(registeredUser,UsersDto.class);
    }
    @Override
    public UsersDto updateUser(UsersDto usersDto, Integer userId) {
        Users user=this.usersRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("Users","id",userId));
        this.modelMapper.map(usersDto, user);
        user.setPassword(this.passwordEncoder.encode(usersDto.getPassword()));
        Users updatedUser=this.usersRepo.save(user);
        return this.modelMapper.map(updatedUser,UsersDto.class);
    }

    @Override
    public UsersDto getUser(Integer userId) {
        Users user=this.usersRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","user Id",userId));
        return this.modelMapper.map(user,UsersDto.class);
    }

    @Override
    public List<UsersDto> getAllUsers() {
        List<Users> users=this.usersRepo.findAll();
        return users.stream().map((user)->this.modelMapper.map(user,UsersDto.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Integer userId) {
        Users deletedUser=this.usersRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","user Id",userId));
        this.usersRepo.delete(deletedUser);
    }
}
