package com.example.spring_api.API.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.spring_api.API.Model.AppUser;
import com.example.spring_api.API.Model.Pothole;
import com.example.spring_api.API.Model.Pothole.Location;
import com.example.spring_api.API.Model.PotholeProjection;;

@Repository
public interface PotholeRepository extends JpaRepository<Pothole, Integer> {

    List<Pothole> findAll();
    List<Pothole> findByLocation(Location location);;
    Optional<Pothole> findById(Integer id);
    List<Pothole> findByAppUser(AppUser userfound);

    @Query("SELECT p.id AS id, p.dateFound AS dateFound, p.timeFound AS timeFound, " +
           "p.severity AS severity, p.location AS location, p.appUser.id AS userId " +
           "FROM Pothole p")
    List<PotholeProjection> findAllWithUserId();
}  
