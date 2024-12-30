package com.example.spring_api.API.Model;

import java.sql.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "u_details")
public class UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    public Integer getId() {
        return id;
    }
    @OneToOne(mappedBy = "details")
    @JsonBackReference
    @JsonIgnore
    private AppUser thisAppUser;

     // Field for storing the image as binary data
    @Lob
    @Column(name = "profile_image", nullable = true, length = 2000)
    @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    private byte[] profileImage;

    public byte[] getProfileImage() {
        return profileImage;
    }
    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }
    public AppUser getThisAppUser() {
        return thisAppUser;
    }
    public void setThisAppUser(AppUser thisAppUser) {
        this.thisAppUser = thisAppUser;
    }
    @Column(unique = true)    
    private String email;
    private String phoneNumber;
    private Date dateOfBirth;
    private String jobTitle;
    private String fullName;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public Date getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public String getJobTitle() {
        return jobTitle;
    }
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

}
