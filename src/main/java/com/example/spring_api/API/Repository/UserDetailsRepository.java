package com.example.spring_api.API.Repository;

import java.sql.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.spring_api.API.Model.UserDetails;

import jakarta.transaction.Transactional;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer >{
    
    Optional<UserDetails> findById(Integer id);


    @Modifying
    @Transactional
    @Query("UPDATE UserDetails u SET u.email = :email, u.phoneNumber = :phoneNumber, " +
        "u.dateOfBirth = :dateOfBirth, u.jobTitle = :jobTitle, u.fullName = :fullName " +
        "WHERE u.id = :id")
    UserDetails updateUserDetails(@Param("id") Integer id,
                        @Param("email") String email,
                        @Param("phoneNumber") String phoneNumber,
                        @Param("dateOfBirth") Date dateOfBirth,
                        @Param("jobTitle") String jobTitle,
                        @Param("fullName") String fullName);
}
