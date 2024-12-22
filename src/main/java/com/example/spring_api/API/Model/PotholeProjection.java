package com.example.spring_api.API.Model;

import java.sql.Date;
import java.sql.Time;

public interface PotholeProjection {
    Integer getId();
    Date getDateFound();
    Time getTimeFound();
    String getSeverity();
    Pothole.Location getLocation();
    Integer getUserId();

    interface Location {
        Double getLatitude();
        Double getLongitude();
        String getCountry();
        String getCity();
    }
}
