package com.example.spring_api.API.Service;

import java.util.List;
import java.util.Optional;

//import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.spring_api.API.Model.AppUser;
import com.example.spring_api.API.Model.UnverifiedUser;
import com.example.spring_api.API.Repository.UnverifiedUserRepository;
import com.example.spring_api.API.Repository.UserRepository;
import org.springframework.security.crypto.bcrypt.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UnverifiedUserRepository unverifiedUserRepository;


    private String salt = BCrypt.gensalt();

    public UserService() {
    }

    public String getSalt(){
        return salt;
    }

    public Optional<AppUser> getUserByID(Integer id) {
        // Use findById which returns an Optional
        return userRepository.findById(id);
    }

    public String getUsernameByEmail(String email) {
        AppUser user = userRepository.findNameByEmail(email);
        if (user != null) {
            return user.getUsername(); // Return the username if the user is found
        } else {
            return null; // Return null if the user is not found
        }
    }

    public List<AppUser> getAllUser(){
        return userRepository.findAll();
    }

    @Transactional
    public Optional<AppUser> getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    @Transactional
    public Optional<AppUser> getUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public Integer getIDbyEmail(String email) {
        AppUser user = userRepository.findNameByEmail(email);
        if (user != null) {
            return user.getId();
        }
        return 0;
    }

    @Transactional
    public AppUser addUser(AppUser user) {
        user.setPassword(BCrypt.hashpw(user.getPassword(), salt));
        return userRepository.save(user); // Save user to the database
    }

    public UnverifiedUser addUnverifiedUser(UnverifiedUser user) {
        return unverifiedUserRepository.save(user);
    }

    public void saveUnverifiedUser(UnverifiedUser user) {
        unverifiedUserRepository.save(user);
    }

    public Optional<UnverifiedUser> getUnverifiedUser(String email) {
        return unverifiedUserRepository.findByEmail(email);
    }

    public void deleteConfirmedUsers() {
        unverifiedUserRepository.deleteByIsConfirmed(true);
    }

    public void deleteUnverifiedByEmail(String email){
        unverifiedUserRepository.deleteByEmail(email);
    }

    public AppUser updatePassword(String email, String newPassword) {
        Optional<AppUser> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            AppUser user = userOptional.get();
            user.setPassword(BCrypt.hashpw(newPassword, salt)); 
            userRepository.save(user);   
            return user; // Return the updated user
        }
        return null; // Return null if user is not found
    }
    

    public Integer updateDistance(Integer id, Long DistanceTraveled){
        return userRepository.addDistanceByID(id, DistanceTraveled);
    }
}
