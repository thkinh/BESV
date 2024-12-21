package com.example.spring_api.API.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.spring_api.API.Model.AppUser;
import com.example.spring_api.API.Model.Pothole;
import com.example.spring_api.API.Model.Pothole.Location;;

@Repository
public interface PotholeRepository extends JpaRepository<Pothole, Integer> {

    List<Pothole> findAllPotholes();
    List<Pothole> findByLocation(Location location);;
    Optional<Pothole> findById(Integer id);
    List<Pothole> findByAppUser(AppUser userfound);
}  
