package uk.ac.bradford.projecttwo.webinterface.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for handling Google Maps API key retrieval.
 * This endpoint is used to provide the API key for frontend integration.
 */
@RestController
public class GoogleMapsController {

    /**
     * Injects the Google Maps API key from the application properties.
     * The key is stored securely in an environment variable or properties file.
     */
    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    /**
     * Provides the Google Maps API key via a REST endpoint.
     *
     * This endpoint is used by the frontend to access the Google Maps API
     * for map-related features like geospatial asset visualization.
     *
     * @return The Google Maps API key as a string.
     */
    @GetMapping("/api/maps-key")
    public String getGoogleMapsApiKey() {
        return googleMapsApiKey;
    }
}
