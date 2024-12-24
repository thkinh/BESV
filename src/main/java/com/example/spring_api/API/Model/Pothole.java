package com.example.spring_api.API.Model;
import java.sql.Date;

import java.sql.Time;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;


@Entity
public class Pothole {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Date dateFound;
    private Time timeFound;
    private String severity;

    

    @Embedded
    @AttributeOverride(name = "latitude", column = @Column(nullable = false))
    @AttributeOverride(name = "longitude", column = @Column(nullable = false))
    @NaturalId 
    private Location location;
    
    @Embedded
    @JsonIgnore
    private PotholeDetails details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uID", nullable = false)
    @JsonBackReference 
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AppUser appUser; 

    public AppUser getAppUser() {
        return appUser;
    }

    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDateFound() {
        return dateFound;
    }

    public void setDateFound(Date dateFound) {
        this.dateFound = dateFound;
    }

    public Time getTimeFound() {
        return timeFound;
    }

    public void setTimeFound(Time timeFound) {
        this.timeFound = timeFound;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
    
    public PotholeDetails getDetails() {
        return details;
    }

    public void setDetails(PotholeDetails details) {
        this.details = details;
    }

    @Embeddable
    public static class Location {

        @Column(nullable = false)
        private Double latitude;
        @Column(nullable = false)
        private Double longitude;


        private String country;
        private String city;
        private String street;

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}



