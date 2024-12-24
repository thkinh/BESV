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

    @Transactional
    public PotholeDetails updatePotholeDetails(Integer potholeId, PotholeDetails newDetails) {
        Optional<Pothole> potholeOpt = potholeRepository.findById(potholeId);
        
        if (potholeOpt.isPresent()) {
            Pothole pothole = potholeOpt.get();
            PotholeDetails currentDetails = pothole.getDetails();
            
            // Update values in the existing PotholeDetails entity
            if (newDetails.getAccel_sd_z() != null) {
                currentDetails.setAccel_sd_z(newDetails.getAccel_sd_z());
            }
            if (newDetails.getAccel_mean_z() != null) {
                currentDetails.setAccel_mean_z(newDetails.getAccel_mean_z());
            }
            if (newDetails.getAccel_var_z() != null) {
                currentDetails.setAccel_var_z(newDetails.getAccel_var_z());
            }
            if (newDetails.getDepth() != null) {
                currentDetails.setDepth(newDetails.getDepth());
            }
            if (newDetails.getWidth() != null) {
                currentDetails.setWidth(newDetails.getWidth());
            }
            if (newDetails.getIsConfirmed() != null) {
                currentDetails.setIsConfirmed(newDetails.getIsConfirmed());
            }

            // Save the updated details back into the Pothole
            pothole.setDetails(currentDetails);
            potholeRepository.save(pothole);
            
            return currentDetails; // Return the updated details
        } else {
            return null; // Return null if Pothole is not found
        }
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
