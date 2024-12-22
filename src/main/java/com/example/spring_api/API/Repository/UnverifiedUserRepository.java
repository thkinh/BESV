package com.example.spring_api.API.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_api.API.Model.UnverifiedUser;



@Repository
public interface UnverifiedUserRepository extends JpaRepository<UnverifiedUser, Integer>{

    Optional<UnverifiedUser> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("DELETE FROM UnverifiedUser u WHERE u.isConfirmed = :isConfirmed")
    void deleteByIsConfirmed(@Param("isConfirmed") boolean isConfirmed);


    @Modifying
    @Transactional
    @Query("DELETE FROM UnverifiedUser u WHERE u.email = :email")
    void deleteByEmail(@Param("email") String email);

}
