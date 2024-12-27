package com.example.spring_api.API.Controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.spring_api.API.Model.AppUser;
import com.example.spring_api.API.Model.Pothole;
import com.example.spring_api.API.Model.PotholeDetails;
import com.example.spring_api.API.Model.PotholeProjection;
import com.example.spring_api.API.Service.PotholeService;
import com.example.spring_api.API.Service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/pothole")
public class PotholeController {
    
    private final PotholeService potholeService;
    private final UserService userService;

    public PotholeController(PotholeService potholeService, UserService userService)
    {
        this.userService = userService;
        this.potholeService = potholeService;
    }


    @GetMapping("get/details")
    public ResponseEntity<PotholeDetails> getMethodName(@RequestParam Integer id) {
        Optional<Pothole> pothole = potholeService.getPothole(id);
        if (pothole.isEmpty()) {
            return ResponseEntity.status(504).body(null);
        }
        PotholeDetails details = pothole.get().getDetails();
        return ResponseEntity.status(200).body(details);
    }
    
    @PostMapping("/updateDetails")
    public ResponseEntity<PotholeDetails> updatePotholeDetails(
            @RequestParam("id") Integer potholeId, 
            @RequestBody PotholeDetails potholeDetails) {
        
        PotholeDetails updatedDetails = potholeService.updatePotholeDetails(potholeId, potholeDetails);
        if (updatedDetails != null) {
            return ResponseEntity.ok(updatedDetails);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadProfileImage(
            @RequestParam Integer id,
            @RequestParam("imageFile") MultipartFile file) {
        try {
            potholeService.uploadPotholeImage(id, file);
            return ResponseEntity.ok("Image uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload image.");
        }
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> getPotholeImage(@RequestParam Integer id) {
        byte[] image = potholeService.getPHImage(id);
        if (image == null) {
            return ResponseEntity.status(504).body(null);
        }
        return ResponseEntity.ok().header("Content-Type", "image/jpeg").body(image);
    }

    @GetMapping("get-singlePH")
    public ResponseEntity<Pothole> getPothole(@RequestParam(name = "id") Integer id){
        Optional<Pothole> pothole = potholeService.getPothole(id);

        if (pothole.isPresent()) {
            return ResponseEntity.ok(pothole.get());
        }
        return ResponseEntity.status(404).body(null);
    }

    @GetMapping("get/ByLocation")
    public ResponseEntity<Pothole> findByLocation(@RequestParam Double latitude, @RequestParam Double longitude) {
        try {
            Pothole pothole = potholeService.getPotholeByLocation(latitude, longitude);
            return ResponseEntity.ok(pothole);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("get/ALL")
    public ResponseEntity<List<PotholeProjection>> getALL() {
        List<PotholeProjection> potholes = potholeService.getALLPotholesWithID();
        if (!potholes.isEmpty()) {
            return ResponseEntity.ok(potholes);
        }
        return ResponseEntity.status(504).body(null);
    }

    @GetMapping("get")
    public ResponseEntity<List<PotholeProjection>> getPothole(@RequestParam(name = "user") String username){
        Optional<AppUser> user = userService.getUserByUsername(username);
        
        if (user.isPresent()) {

            List<PotholeProjection> potholes = potholeService.getPotholesByUserId(user.get().getId()); 
            return ResponseEntity.status(504).body(potholes);    
        }
        return ResponseEntity.status(504).body(null);
    }

    @PostMapping("add")
    public ResponseEntity<Pothole> addPothole(@RequestBody Pothole pothole) {
        try {
            Pothole pothole2 = potholeService.addPothole(pothole);
            return ResponseEntity.ok(pothole2);
        }
        catch(Exception e){ 
            return ResponseEntity.status(504).body(pothole);
        }
    }

    
}
