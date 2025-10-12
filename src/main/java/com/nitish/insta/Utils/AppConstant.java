package com.nitish.insta.Utils;

import com.nitish.insta.Entities.Users;
import com.nitish.insta.Payloads.UsersDto;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConstant {
    public static final Integer USER_ROLE_ID=999;
    public static final Integer ADMIN_ROLE_ID=222;
    public static final long JWT_TOKEN_VALIDITY=15*60*1000;
    public static final String JWT_SECRET_KEY="klkaiuyyjhqbjeguayyggjhhajbhaljfgfauygffjhhjhbjf";
    public static final String APP_MAME="Instagram";

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper mapper=new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.typeMap(UsersDto.class, Users.class).addMappings(m->m.skip(Users::setUserId));
        return mapper;
    }
}
