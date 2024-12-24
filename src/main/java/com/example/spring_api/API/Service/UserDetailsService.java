package com.example.spring_api.API.Service;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.spring_api.API.Model.AppUser;
import com.example.spring_api.API.Model.UserDetails;
import com.example.spring_api.API.Repository.UserDetailsRepository;
import com.example.spring_api.API.Repository.UserRepository;
import com.example.spring_api.API.Util.ImageUtils;


@Service
public class UserDetailsService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    private UserRepository appUserRepository;

    public Optional<UserDetails> getUserDetailsById(Integer id) {
        return userDetailsRepository.findById(id);
    }

    public UserDetails updateUserDetails(Integer userId, UserDetails details) {
        // Find the AppUser by userId
        AppUser appUser = appUserRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("AppUser not found"));

        // Check if the AppUser already has associated UserDetails
        if (appUser.getDetails() == null) {
            appUser.setDetails(new UserDetails());
            // If no UserDetails exist for this user, create a new UserDetails and associate it
            details.setThisAppUser(appUser);
        } 
        else {
            // Update the existing UserDetails
            UserDetails existingDetails = appUser.getDetails();
            existingDetails.setEmail(appUser.getEmail());
            existingDetails.setPhoneNumber(details.getPhoneNumber());
            existingDetails.setDateOfBirth(details.getDateOfBirth());
            existingDetails.setJobTitle(details.getJobTitle());
            existingDetails.setFullName(details.getFullName());
            return userDetailsRepository.save(existingDetails);
        }

        // If there is no existing UserDetails, save the new one and associate it with the AppUser
        appUser.setDetails(details);
        appUserRepository.save(appUser);

        return userDetailsRepository.updateUserDetails(
            appUser.getId(),
            details.getEmail(),
            details.getPhoneNumber(),
            details.getDateOfBirth(),
            details.getJobTitle(),
            details.getFullName()
            );
    }
    
    public UserDetails uploadImage(Integer userId, MultipartFile file) throws IOException {
        Optional<AppUser> appUser = appUserRepository.findById(userId);
        if (appUser.isEmpty()) {
            throw new RuntimeException("AppUser not found");
        }
        Optional<UserDetails> userDetails = userDetailsRepository.findById(appUser.get().getDetails().getId());
        if (userDetails.isEmpty()) {
            throw  new RuntimeException("User not found with id: " + userId);
        }

        byte[] compressedImage = ImageUtils.compressImage(file.getBytes());
        userDetails.get().setProfileImage(compressedImage);

        return userDetailsRepository.save(userDetails.get());
    }


    @Transactional
    public byte[] getImage(Integer dtId) {
        UserDetails userDetails = userDetailsRepository.findById(dtId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dtId));

        byte[] decompressedImage = ImageUtils.decompressImage(userDetails.getProfileImage());
        return decompressedImage;
    }


}
