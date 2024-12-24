package com.example.spring_api.API.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;

@Embeddable
public class PotholeDetails {
    
    private Double  accel_var_z;
    public Double getAccel_var_z() {
        return accel_var_z;
    }
    public void setAccel_var_z(Double accel_var_z) {
        this.accel_var_z = accel_var_z;
    }
    private Double  accel_mean_z;
    public Double  getAccel_mean_z() {
        return accel_mean_z;
    }
    public void setAccel_mean_z(Double accel_mean_z) {
        this.accel_mean_z = accel_mean_z;
    }
    private Double  accel_sd_z;
    private Double  Depth;
    private Double  Width;
    public Double getAccel_sd_z() {
        return accel_sd_z;
    }
    public void setAccel_sd_z(Double accel_sd_z) {
        this.accel_sd_z = accel_sd_z;
    }
    public Double getDepth() {
        return Depth;
    }
    public void setDepth(Double depth) {
        Depth = depth;
    }
    public Double getWidth() {
        return Width;
    }
    public void setWidth(Double width) {
        Width = width;
    }
    private Boolean isConfirmed;

    @Lob
    @Column(name = "image", nullable = true, length = 2000)
    @Basic(fetch = FetchType.LAZY, optional = true)
    @JsonIgnore
    private byte[] image;

    

    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }
  
    public Boolean getIsConfirmed() {
        return isConfirmed;
    }
    public void setIsConfirmed(Boolean isConfirmed) {
        this.isConfirmed = isConfirmed;
    }
}
