package com.example.spring_api.API.Model;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_User")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id; // Primary key

    private String username;

    @Column(unique = true)
    private String email;
    private String password;
    private Date date_created;

    @OneToMany(mappedBy = "appUser", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference 
    @JsonIgnore
    private List<Pothole> potholes;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "settingID", referencedColumnName = "id")

    @JsonIgnore
    private UserSetting setting;

    private Long DistanceTraveled;

    @JsonIgnore
    private String salt;

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Long getDistanceTraveled() {
        return DistanceTraveled;
    }

    public void setDistanceTraveled(Long distanceTraveled) {
        DistanceTraveled = distanceTraveled;
    }

    public List<Pothole> getPotholes() {
        return potholes;
    }

    public void setPotholes(List<Pothole> potholes) {
        this.potholes = potholes;
    }

    // Getters and Setters...
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

}
