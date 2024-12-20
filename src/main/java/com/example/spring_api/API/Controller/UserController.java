package com.example.spring_api.API.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.spring_api.API.Model.AppUser;
import com.example.spring_api.API.Model.UnverifiedUser;
import com.example.spring_api.API.Service.MailService;
import com.example.spring_api.API.Service.UserService;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.UUID;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final MailService mailService;

    public UserController(UserService userService, MailService mailService) {
        this.userService = userService;
        this.mailService = mailService;
    }

    @GetMapping("/getByEmail")
    public ResponseEntity<AppUser> getMethodName(@RequestParam(name = "email") String email) {
        Optional<AppUser> user = userService.getUserByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        else{
            return ResponseEntity.status(504).body(null);
        }}
    

    @GetMapping("/getByName")
    public ResponseEntity<AppUser> getUserByName(@RequestParam(name = "name") String name){
        Optional<AppUser> user = userService.getUserByUsername(name);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        else{
            return ResponseEntity.status(504).body(null);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<AppUser> getUserByID(@RequestParam(name = "id") Integer id) {
        // Get user from service layer
        Optional<AppUser> user = userService.getUserByID(id);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get()); // Return 200 OK with user data
        } else {
            return ResponseEntity.status(504).body(null); // Return 404 Not Found if the user does not exist
        }
    }

    @GetMapping("get/name")
    public ResponseEntity<String> getUsernameByEmail(@RequestParam(name = "email") String email) {
        String username = userService.getUsernameByEmail(email);
        if (username != null) {
            return ResponseEntity.ok(username); // Return the username if found
        } else {
            return ResponseEntity.status(504).body("User not found"); // Return 404 if no user found
        }
    }

    @GetMapping("get/id")
    public ResponseEntity<Integer> getIDbyEmail(@RequestParam(name = "email") String email) {
        Integer id = userService.getIDbyEmail(email);
        if (id != 0) {
            return ResponseEntity.ok(id);
        }
        return ResponseEntity.status(500).body(0);
    }

    @PostMapping("/password/confirm")
    public ResponseEntity<String> confirmPassword(@RequestParam String email, @RequestParam String verify_code) {
        try {
            Optional<UnverifiedUser> user = userService.getUnverifiedUser(email);
            if (user.isPresent() && !user.get().isConfirmed()) {
                UnverifiedUser unverifiedUser = user.get();
                
                if (verify_code.equals(unverifiedUser.getvCode())) {
                    unverifiedUser.setConfirmed(true); // Update the entity's state
                    userService.saveUnverifiedUser(unverifiedUser); // Save the updated entity to the database
                    return ResponseEntity.status(200).body("Confirmed");
                } else {
                    return ResponseEntity.status(500).body("Your verify code was wrong");
                }
            }
            
            return ResponseEntity.status(500).body("No match for this unverified user");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }
    

    @PostMapping("/password/getVerify")
    public ResponseEntity<String> getVerifyCode(@RequestParam(name = "email") String email) {
        String verifyCode = UUID.randomUUID().toString();
        try {
            Boolean isSent = mailService.sendEmail(email);
            if (isSent) {
                UnverifiedUser user = new UnverifiedUser();
                user.setEmail(email);
                user.setvCode(verifyCode);
                userService.addUnverifiedUser(user);
                return ResponseEntity.status(200).body(user.getId().toString());
            }
            return ResponseEntity.status(500).body("Couldn't send the email to client");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody AppUser user) {
        try {
            AppUser createdUser = userService.addUser(user);
            return ResponseEntity.status(201).body(createdUser);  // Return 201 with the created user
        } 
        catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(500).body("User already exists with this email.");  
        } 
        catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while creating the user: " + e.getMessage()); 
        }
    }
    

    @GetMapping("/FuckYou")
    public String getFuckYou(@RequestParam(name = "name") String param) {
        return String.format("Fuck you %s", param);
    }
}
