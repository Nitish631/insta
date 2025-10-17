package com.nitish.insta.Service;

import com.nitish.insta.Entities.Device;
import com.nitish.insta.Entities.Users;
import com.nitish.insta.Repository.DeviceRepository;
import com.nitish.insta.Repository.UsersRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DeviceService {
    @Autowired
    private DeviceRepository deviceRepo;
    @Autowired
    private UsersRepo userRepo;


    public Device registerOrUpdateDevice(String deviceId, String name, String os, String token, Users user) {
        Device device = deviceRepo.findByDeviceId(deviceId).orElse(new Device());

        device.setDeviceId(deviceId);
        device.setDeviceName(name);
        device.setOs(os);
        device.setNotificationToken(token);

      
        device.getUsers().add(user);
        user.getDevices().add(device);

        userRepo.save(user);
        return device;
    }

    public void unlinkUserFromDevice(Users user, String deviceId) {
        Optional<Device> deviceOpt = deviceRepo.findByDeviceId(deviceId);
        if (deviceOpt.isPresent()) {
            Device device = deviceOpt.get();
            device.getUsers().remove(user);
            user.getDevices().remove(device);
            userRepo.save(user);
            if (device.getUsers().isEmpty()) {
                deviceRepo.delete(device);
            }
        }
    }
}
