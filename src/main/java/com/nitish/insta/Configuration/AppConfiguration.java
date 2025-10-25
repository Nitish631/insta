package com.nitish.insta.Configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nitish.insta.Entities.*;
import com.nitish.insta.Payloads.*;

@Configuration
public class AppConfiguration {

    @Bean
    public ModelMapper mapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setSkipNullEnabled(true);

        // --- Comment Mapping ---
        mapper.typeMap(Comment.class, CommentDto.class).addMappings(m -> {
            m.skip(CommentDto::setUsername);
        });

        mapper.typeMap(UsersDto.class, Users.class).addMappings(m -> m.skip(Users::setUserId));

        // --- Post Mapping ---
        mapper.typeMap(Post.class, PostResponseDto.class).addMappings(m -> {
            m.map(src -> src.getUser() != null ? src.getUser().getUserId() : null, PostResponseDto::setUserId);
            m.map(src -> src.getComments() != null ? src.getComments().size() : 0, PostResponseDto::setCommentCount);
        });

        // --- FileResource Mapping ---
        mapper.typeMap(FileResource.class, FileResourceDto.class);

        return mapper;
    }
}
