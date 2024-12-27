package com.example.spring_api.API.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("SELECT p FROM Pothole p WHERE p.location.latitude = :latitude AND p.location.longitude = :longitude")
    Optional<Pothole> findByLatitudeAndLongitude(@Param("latitude") Double latitude, @Param("longitude") Double longitude);


    @Query("SELECT p.id AS id, p.dateFound AS dateFound, p.timeFound AS timeFound, " +
           "p.severity AS severity, p.location AS location, p.appUser.id AS userId " +
           "FROM Pothole p")
    List<PotholeProjection> findAllWithUserId();

    @Query("SELECT p.id AS id, p.dateFound AS dateFound, p.timeFound AS timeFound, " +
       "p.severity AS severity, p.location AS location, p.appUser.id AS userId " +
       "FROM Pothole p WHERE p.appUser.id = :userId")
    List<PotholeProjection> findAllByUserId(@Param("userId") Integer userId);

}  
