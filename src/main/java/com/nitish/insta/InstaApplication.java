package com.nitish.insta;

import com.nitish.insta.Entities.Role;
import com.nitish.insta.Repository.RoleRepo;
import com.nitish.insta.Utils.AppConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class InstaApplication implements CommandLineRunner {
    @Autowired
    private RoleRepo roleRepo;

    public static void main(String[] args) {
        SpringApplication.run(InstaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
    try {
        Role roleAdmin=new Role();
        roleAdmin.setRoleId(AppConstant.ADMIN_ROLE_ID);
        roleAdmin.setRoleName("ADMIN");
        Role roleUser=new Role();
        roleUser.setRoleName("USER");
        roleUser.setRoleId(AppConstant.USER_ROLE_ID);
        List<Role> roles=List.of(roleUser,roleAdmin);
        List<Role> result=this.roleRepo.saveAll(roles);
        result.forEach(role -> {
            System.out.println(role.getRoleName());
        });
    }catch (Exception e){
        e.printStackTrace();
    }
    }

}
