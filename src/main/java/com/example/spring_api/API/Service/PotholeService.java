package com.example.spring_api.API.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.spring_api.API.Model.AppUser;
import com.example.spring_api.API.Model.Pothole;
import com.example.spring_api.API.Model.PotholeDetails;
import com.example.spring_api.API.Model.PotholeProjection;
import com.example.spring_api.API.Model.UserDetails;
import com.example.spring_api.API.Repository.PotholeRepository;
import com.example.spring_api.API.Repository.UserRepository;
import com.example.spring_api.API.Util.ImageUtils;


@Service
public class PotholeService {

    @Autowired
    private PotholeRepository potholeRepository;
    private UserRepository userRepository;
    public PotholeService(PotholeRepository potholeRepository, UserRepository appUserRepository) {
        this.potholeRepository = potholeRepository;
        this.userRepository = appUserRepository;
    }

    @Transactional
    public Pothole uploadPotholeImage(Integer potholeId, MultipartFile file) throws IOException {
        Optional<Pothole> potholeOpt = potholeRepository.findById(potholeId);
        if (potholeOpt.isEmpty()) {
            throw new RuntimeException("Pothole not found with id: " + potholeId);
        }
        Pothole pothole = potholeOpt.get();
        byte[] compressedImage = ImageUtils.compressImage(file.getBytes());

        if (pothole.getDetails() == null) {
            pothole.setDetails(new PotholeDetails());
        }
        pothole.getDetails().setImage(compressedImage);
        return potholeRepository.save(pothole);
    }

    @Transactional
    public byte[] getPHImage(Integer id) {
        Pothole pothole = potholeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        byte[] decompressedImage = ImageUtils.decompressImage(pothole.getDetails().getImage());
        return decompressedImage;
    }

    public Pothole getPotholeByLocation(Double latitude, Double longitude) {
        return potholeRepository.findByLatitudeAndLongitude(latitude, longitude)
                .orElseThrow(() -> new RuntimeException("No pothole found at the given location"));
    }



    public List<Pothole> getAllPotholes(){
        return potholeRepository.findAll();
    }

    public List<PotholeProjection> getALLPotholesWithID(){
        return potholeRepository.findAllWithUserId();
    }

    public Optional<Pothole> getPothole(Integer id) {
        return potholeRepository.findById(id);
    }
    
    public Pothole addPothole(Pothole pothole)
    {
        return potholeRepository.save(pothole);
    }

    public List<Pothole> getPotholesByUsername(String username) {
        Optional<AppUser> appUser = userRepository.findByUsername(username);
        if (appUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        return potholeRepository.findByAppUser(appUser.get());
    }
}
