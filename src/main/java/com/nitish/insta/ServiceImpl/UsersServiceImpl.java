package com.nitish.insta.ServiceImpl;

import com.nitish.insta.Entities.Device;
import com.nitish.insta.Entities.Role;
import com.nitish.insta.Entities.Users;
import com.nitish.insta.Exception.ResourceNotFoundException;
import com.nitish.insta.Payloads.UsersDto;
import com.nitish.insta.Repository.DeviceRepository;
import com.nitish.insta.Repository.RoleRepo;
import com.nitish.insta.Repository.UsersRepo;
import com.nitish.insta.Service.UsersService;
import com.nitish.insta.Utils.AppConstant;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    @Autowired
    private DeviceRepository deviceRepository;

    @Override
    public UsersDto registerNewUser(UsersDto usersDto, Device device) {
        Users user = this.modelMapper.map(usersDto, Users.class);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        Role role = this.roleRepo.findById(AppConstant.USER_ROLE_ID).get();
        user.getRoles().add(role);

        Optional<Device> fetchedDevice = this.deviceRepository.findByDeviceId(device.getDeviceId());
        Device deviceToSave = fetchedDevice.orElse(device);
        deviceToSave.getUsers().add(user);
        this.deviceRepository.save(deviceToSave);
        user.getDevices().add(deviceToSave);
        Users registeredUser = this.usersRepo.save(user);
        return this.modelMapper.map(registeredUser, UsersDto.class);
    }

    @Override
    public UsersDto updateUser(UsersDto usersDto, Integer userId) {
        Users user = this.usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Users", "id", userId));
        this.modelMapper.map(usersDto, user);
        user.setPassword(this.passwordEncoder.encode(usersDto.getPassword()));
        Users updatedUser = this.usersRepo.save(user);
        return this.modelMapper.map(updatedUser, UsersDto.class);
    }

    @Override
    public UsersDto getUser(Integer userId) {
        Users user = this.usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "user Id", userId));
        return this.modelMapper.map(user, UsersDto.class);
    }

    @Override
    public List<UsersDto> getAllUsers() {
        List<Users> users = this.usersRepo.findAll();
        return users.stream().map((user) -> this.modelMapper.map(user, UsersDto.class)).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Integer userId) {
        Users deletedUser = this.usersRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "user Id", userId));
        this.usersRepo.delete(deletedUser);
    }

    @Override
    public void googleLoginRegistration(String email, String password, String username, Device device) {
        Optional<Device> existingDevice = this.deviceRepository.findByDeviceId(device.getDeviceId());
        Device deviceToSave = existingDevice.orElse(device);
        Optional<Users> existingUser = this.usersRepo.findByEmail(email);
        if (existingUser.isEmpty()) {
            Role role = roleRepo.findById(AppConstant.USER_ROLE_ID).get();
            Users user = new Users();
            user.getRoles().add(role);
            user.setEmail(email);
            user.setFullName(username);
            user.setPassword(this.passwordEncoder.encode(password));
            user.setUserName(AppConstant.APP_MAME.concat("user"));
            user.getDevices().add(deviceToSave);
            deviceRepository.save(deviceToSave);
            this.usersRepo.save(user);
        } else {
            Users user = existingUser.get();
            Device existingdevice = existingDevice.get();
            existingdevice.setDeviceId(device.getDeviceId());
            existingdevice.setDeviceName(device.getDeviceName());
            existingdevice.setNotificationToken(device.getNotificationToken());
            existingdevice.setOs(device.getOs());
            if (!existingdevice.getUsers().contains(user)) {
                existingdevice.getUsers().add(user);
            }
            deviceRepository.save(existingdevice);
            if (!user.getDevices().contains(existingdevice)) {
                user.getDevices().add(existingdevice);
            }
            usersRepo.save(user);
        }

    }

    @Override
    public String changeUserName(String userName, String email) {
        Users users = this.usersRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        users.setUserName(userName);
        this.usersRepo.save(users);
        return "success";
    }

    @Override
    public String changeFullName(String fullName, String email) {
        Users users = this.usersRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        users.setFullName(fullName);
        this.usersRepo.save(users);
        return "success";
    }

    @Override
    public void loginUser(Device device, String email) {
        Users user = this.usersRepo.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("User", "email", email));
        Optional<Device> fetchedDevice = this.deviceRepository.findByDeviceId(device.getDeviceId());
        Device deviceToSave = fetchedDevice.orElse(device);
        deviceToSave.getUsers().add(user);
        this.deviceRepository.save(deviceToSave);
        user.getDevices().add(deviceToSave);
        this.usersRepo.save(user);
    }
}
