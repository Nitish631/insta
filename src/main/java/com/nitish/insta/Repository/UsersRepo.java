package com.nitish.insta.Repository;

import com.nitish.insta.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepo extends JpaRepository<Users,Long> {
    public Optional<Users> findByEmail(String email);
}
