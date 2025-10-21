package com.nitish.insta;

import com.nitish.insta.Configuration.AppConstant;
import com.nitish.insta.Entities.BadMainCategories;
import com.nitish.insta.Entities.BadSubCategories;
import com.nitish.insta.Entities.MainCategory;
import com.nitish.insta.Entities.Role;
import com.nitish.insta.Entities.SubCategory;
import com.nitish.insta.Repository.BadMainCategoryRepo;
import com.nitish.insta.Repository.BadSubCategoryRepo;
import com.nitish.insta.Repository.MainCategoryRepo;
import com.nitish.insta.Repository.RoleRepo;
import com.nitish.insta.Repository.SubCategoryRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;
import java.util.Map;
@EnableScheduling
@SpringBootApplication
public class InstaApplication implements CommandLineRunner {
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private AppConstant appConstant;
    @Autowired
    private BadMainCategoryRepo badMainCategoryRepo;
    @Autowired
    private BadSubCategoryRepo badSubCategoryRepo;
    @Autowired
    private MainCategoryRepo mainCategoryRepo;
    @Autowired
    private SubCategoryRepo subCategoryRepo;
    public static void main(String[] args) {
        SpringApplication.run(InstaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            Role roleAdmin = new Role();
            roleAdmin.setRoleId(appConstant.ADMIN_ROLE_ID);
            roleAdmin.setRoleName("ADMIN");
            Role roleUser = new Role();
            roleUser.setRoleName("USER");
            roleUser.setRoleId(appConstant.USER_ROLE_ID);
            List<Role> roles = List.of(roleUser, roleAdmin);
            List<Role> result = this.roleRepo.saveAll(roles);
            assingCategoriesToDB();
            result.forEach(role -> {
                System.out.println(role.getRoleName());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void assingCategoriesToDB(){
        for(Map.Entry<Integer,String> entry:appConstant.MAIN_CATEGORIES.entrySet()){
            MainCategory mainCatgory=new MainCategory();
            mainCatgory.setId(entry.getKey());
            mainCatgory.setName(entry.getValue());
            this.mainCategoryRepo.save(mainCatgory);
        }
        for(Map.Entry<Integer,String> entry:appConstant.SUB_CATEGORIES.entrySet()){
            SubCategory subCateogry=new SubCategory();
            subCateogry.setId(entry.getKey());
            subCateogry.setName(entry.getValue());
            this.subCategoryRepo.save(subCateogry);
        }
        for(Map.Entry<Integer,String> entry:appConstant.MAIN_BAD_CATEGORIES.entrySet()){
            BadMainCategories badMainCategories=new BadMainCategories();
            badMainCategories.setId(entry.getKey());
            badMainCategories.setName(entry.getValue());
            this.badMainCategoryRepo.save(badMainCategories);
        }
        for(Map.Entry<Integer,String> entry:appConstant.SUB_BAD_CATEGORIES.entrySet()){
            BadSubCategories badSubCategories=new BadSubCategories();
            badSubCategories.setId(entry.getKey());
            badSubCategories.setName(entry.getValue());
            this.badSubCategoryRepo.save(badSubCategories);
        }
    }

}
