package com.nitish.insta;

import com.nitish.insta.Entities.Role;
import com.nitish.insta.Repository.RoleRepo;
import com.nitish.insta.Utils.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
@EnableScheduling
@SpringBootApplication
public class InstaApplication implements CommandLineRunner {
    @Autowired
    private RoleRepo roleRepo;
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecretKey;

    public static void main(String[] args) {
        SpringApplication.run(InstaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("CLIENT ID :" + clientId);
        System.out.println("SECRET KEY: " + clientSecretKey);
        try {
            Role roleAdmin = new Role();
            roleAdmin.setRoleId(AppConstant.ADMIN_ROLE_ID);
            roleAdmin.setRoleName("ADMIN");
            Role roleUser = new Role();
            roleUser.setRoleName("USER");
            roleUser.setRoleId(AppConstant.USER_ROLE_ID);
            List<Role> roles = List.of(roleUser, roleAdmin);
            List<Role> result = this.roleRepo.saveAll(roles);
            result.forEach(role -> {
                System.out.println(role.getRoleName());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
