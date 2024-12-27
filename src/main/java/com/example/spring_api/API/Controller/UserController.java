package com.example.spring_api.API.Controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.spring_api.API.Model.AppUser;
import com.example.spring_api.API.Model.UnverifiedUser;
import com.example.spring_api.API.Model.UserDetails;
import com.example.spring_api.API.Service.MailService;
import com.example.spring_api.API.Service.RandomGenerator;
import com.example.spring_api.API.Service.UserDetailsService;
import com.example.spring_api.API.Service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService           userService;
    private final UserDetailsService    userDetailsService;
    private final MailService           mailService;

    public UserController(UserService userService, MailService mailService, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.mailService = mailService;
        this.userDetailsService = userDetailsService;
    }



    @GetMapping("/details/profileImage")
    public ResponseEntity<byte[]> getProfileImage(@RequestParam Integer id) {
        Optional<AppUser> user = userService.getUserByID(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(501).body(null);
        }
        UserDetails details = user.get().getDetails();
        byte[] image = userDetailsService.getImage(details.getId());
        if (image == null) {
            return ResponseEntity.status(504).header("Content-Type", "image/jpeg").body(image);    
        }
        return ResponseEntity.ok().header("Content-Type", "image/jpeg").body(image);
    }
    
    @PostMapping("details/uploadImage")
    public ResponseEntity<String> uploadProfileImage(
            @RequestParam Integer id,
            @RequestParam("imageFile") MultipartFile file) {
        try {
            userDetailsService.uploadImage(id, file);
            return ResponseEntity.ok("Image uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to upload image.");
        }
    }

    @GetMapping("/details")
    public ResponseEntity<UserDetails> getUserDetails(@RequestParam Integer id) {
        
        Optional<UserDetails> details = userDetailsService.getUserDetailsById(id);
        if (details.isPresent()) {
            return ResponseEntity.status(200).body(details.get());
        }
        return ResponseEntity.status(504).body(null);
    }

    @PostMapping("/details/update")
    public ResponseEntity<UserDetails> updateUserDetails(
            @RequestParam("uID") Integer userId, @RequestBody UserDetails details) {
        try {
            // Validate input, make sure necessary fields are not null
            if (userId == null || details == null) {
                return ResponseEntity.badRequest().body(null);
            }
            // Update the UserDetails and associate it with the AppUser
            UserDetails updatedDetails = userDetailsService.updateUserDetails(userId, details);
            // Return the updated UserDetails
            return ResponseEntity.ok(updatedDetails);
        } catch (Exception e) {
            // Handle any errors
            return ResponseEntity.status(500).body(null);
        }
    }
    
  



    @PostMapping("/updateDistance")
    public ResponseEntity<Integer> postMethodName(@RequestParam(name ="id") Integer id, @RequestParam(name = "distance") Long distance) {

        Optional<AppUser> user = userService.getUserByID(id);
        if (user.isPresent()) {
            userService.updateDistance(id, distance);
            return ResponseEntity.status(200).body(user.get().getId());
        }

        return ResponseEntity.status(504).body(null);
    }
    
    @GetMapping("/getByEmail") //return app user, used by old login
    public ResponseEntity<AppUser> getMethodName(@RequestParam(name = "email") String email) {
        Optional<AppUser> user = userService.getUserByEmail(email);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        else{
            return ResponseEntity.status(504).body(null);
        }
    }


    @GetMapping("/getByName") //return app user, used for ez get method
    public ResponseEntity<AppUser> getUserByName(@RequestParam(name = "name") String name){
        Optional<AppUser> user = userService.getUserByUsername(name);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        else{
            return ResponseEntity.status(504).body(null);
        }
    }

    @GetMapping("/get") //return app user, used for ez get method
    public ResponseEntity<AppUser> getUserByID(@RequestParam(name = "id") Integer id) {
        // Get user from service layer
        Optional<AppUser> user = userService.getUserByID(id);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get()); // Return 200 OK with user data
        } else {
            return ResponseEntity.status(504).body(null); // Return 504 Not Found if the user does not exist
        }
    }

    @GetMapping("get/name") //return name of the user, used by checking email and name
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


    @GetMapping("SIRequest")
    public ResponseEntity<AppUser> SIRequest(@RequestParam(name = "email") String email, @RequestParam(name = "password") String password) {
        Optional<AppUser> user = userService.getUserByEmail(email);
        if (user.isPresent()) {
            if(BCrypt.checkpw(password, user.get().getPassword()))
            {
                return ResponseEntity.ok(user.get()); // Return 200 OK with user data    
            }
            return ResponseEntity.status(501).body(null); //501 = wrong password
        } else {
            return ResponseEntity.status(504).body(null); // Return 504 Not Found if the user does not exist
        }   
    }

    


    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody AppUser user) {
        try {
            user.setDetails(new UserDetails());
            AppUser createdUser = userService.addUser(user);
            Boolean isSent = mailService.signUpNotification(user.getEmail());
            if (isSent) {
                return ResponseEntity.status(200).body(createdUser);  // Return 201 with the created user    
            }
            return ResponseEntity.status(501).body(null); //Could not verify email
        } 
        catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(502).body(null);  //User already exists with this email.
        } 
        catch (Exception e) {
            return ResponseEntity.status(504).body("An error occurred while creating the user: " + e.getMessage()); 
        }
    }

    @GetMapping("get/ALL")
    public ResponseEntity<List<AppUser>> getAllUser() {
        List<AppUser> users = userService.getAllUser();
        if (!users.isEmpty()) {
            return ResponseEntity.ok(users);
        }
        return ResponseEntity.status(504).body(null);
    }
    
    
    @PostMapping("/password/getVerify")
    public ResponseEntity<String> getVerifyCode(@RequestParam(name = "email") String email) {
        String verifyCode = RandomGenerator.generateRandomString(6);
        String existEmail = email.replace("\"", "");
        try {
            Optional<AppUser> appUser = userService.getUserByEmail(existEmail);
            if (!appUser.isPresent()) {
                System.out.println(existEmail);
                Integer errorCode = -1;
                return ResponseEntity.status(501).body(errorCode.toString());
            }
            Boolean isSent = mailService.sendCode(email, verifyCode);
            if (isSent) {
                UnverifiedUser user = new UnverifiedUser();
                user.setEmail(email);
                user.setvCode(verifyCode);
                userService.addUnverifiedUser(user);
                return ResponseEntity.status(200).body(user.getId().toString());
            }
            return ResponseEntity.status(502).body("Couldn't send email to client");
        } catch (Exception e) {
            return ResponseEntity.status(504).body(e.getMessage());
        }
    }

    //verify OTP code
    @PostMapping("/password/OTPConfirm")
    public ResponseEntity<String> verifyOTP(@RequestParam String email, @RequestParam(name = "code") String verify_code) {
        try {
            Optional<UnverifiedUser> user = userService.getUnverifiedUser(email);
            if (user.isPresent() && !user.get().isConfirmed()) {
                UnverifiedUser unverifiedUser = user.get();
                if (verify_code.equals(unverifiedUser.getvCode())) {
                    unverifiedUser.setConfirmed(true); // Update the entity's state
                    userService.saveUnverifiedUser(unverifiedUser); // Save the updated entity to the database
                    return ResponseEntity.ok("0");
                } else {
                    return ResponseEntity.status(501).body("Your verified code was wrong");
                }
            }
            return ResponseEntity.status(502).body("No match for this unverified user\n" + "User:" + email + "/code:" + verify_code);
        } catch (Exception e) {
            return ResponseEntity.status(504).body("An error occurred: " + e.getMessage());
        }
    }
        
    @PostMapping("/password/confirm")
    public ResponseEntity<?> getUpdatedUser(@RequestParam String email, @RequestParam String password) {
        AppUser updatedUser = userService.updatePassword(email, password);
        if (updatedUser != null) {
            String trueMail = "\""+email+ "\"";
            userService.deleteUnverifiedByEmail(trueMail);
            return ResponseEntity.ok(updatedUser); // Return updated user in the response
        } else {
            return ResponseEntity.status(504).body("User not found");
        }
    }

    @GetMapping("/FuckYou")
    public String getFuckYou(@RequestParam(name = "name") String param) {
        return String.format("Fuck you %s", param);
    }
}
