package com.nitish.insta.Configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nitish.insta.Entities.Comment;
import com.nitish.insta.Entities.Users;
import com.nitish.insta.Payloads.CommentDto;
import com.nitish.insta.Payloads.UsersDto;

@Configuration
public class AppConfiguration {
    @Bean
    public ModelMapper mapper(){
        ModelMapper mapper=new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);
        mapper.typeMap(Comment.class, CommentDto.class).addMappings((m)->{
            m.skip(CommentDto::setUsername);
        });
        mapper.typeMap(UsersDto.class, Users.class).addMappings(m->m.skip(Users::setUserId));
        return mapper;
    }
}
