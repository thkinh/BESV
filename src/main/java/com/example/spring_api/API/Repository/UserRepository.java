package com.example.spring_api.API.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.spring_api.API.Model.AppUser;
import java.util.Optional;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer>{

    // Query method to find user by email
    List<AppUser> findAllUser();
    AppUser findNameByEmail(String email); // Finds a user by their email
    Optional<AppUser> findByEmail(String email);
    Optional<AppUser> findById(Integer id);
    Optional<AppUser> findByUsername(String username);
}
