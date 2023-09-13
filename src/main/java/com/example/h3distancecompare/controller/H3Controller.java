package com.example.h3distancecompare.controller;

import com.uber.h3core.H3Core;
import com.uber.h3core.exceptions.DistanceUndefinedException;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class H3Controller {

    @GetMapping("/distance")
    public double calculateDistance(
        @RequestParam double lat1,
        @RequestParam double lon1,
        @RequestParam double lat2,
        @RequestParam double lon2
    ) throws IOException {
        
        if (!isValidCoordinates(lat1, lon1) || !isValidCoordinates(lat2, lon2)) {
            throw new IllegalArgumentException("Invalid latitude or longitude values.");
        }

        H3Core h3 = H3Core.newInstance();
        long hex1 = h3.geoToH3(lat1, lon1, 5); 
        long hex2 = h3.geoToH3(lat2, lon2, 5);

        try {
            return h3.h3Distance(hex1, hex2);
        } catch (DistanceUndefinedException e) {
            
            throw new RuntimeException("Distance calculation error: " + e.getMessage(), e);
        }
    }


    private boolean isValidCoordinates(double lat, double lon) {
        return (lat >= -90 && lat <= 90) && (lon >= -180 && lon <= 180);
    }
}


//http://localhost:8080/distance?lat1=12.34&lon1=56.78&lat2=23.45&lon2=67.89
