package com.example.spring_api.API.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.spring_api.API.Model.AppUser;
import com.example.spring_api.API.Model.Pothole;
import com.example.spring_api.API.Repository.PotholeRepository;
import com.example.spring_api.API.Repository.UserRepository;


@Service
public class PotholeService {

    @Autowired
    private PotholeRepository potholeRepository;
    private UserRepository userRepository;
    public PotholeService(PotholeRepository potholeRepository, UserRepository appUserRepository) {
        this.potholeRepository = potholeRepository;
        this.userRepository = appUserRepository;
    }

    public List<Pothole> getAllPotholes(){
        return potholeRepository.findAll();
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
