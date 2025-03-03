package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoogleMapsController {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @GetMapping("/api/maps-key")
    public String getGoogleMapsApiKey() {
        return googleMapsApiKey;
    }
}
