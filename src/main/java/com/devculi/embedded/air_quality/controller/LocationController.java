package com.devculi.embedded.air_quality.controller;

import com.devculi.embedded.air_quality.entity.Location;
import com.devculi.embedded.air_quality.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/locations")
public class LocationController {
    @Autowired
    LocationService locationService;

    @GetMapping
    private String getAllLocations(Model model) {
        Collection<Location> locations = locationService.getAllLocations();
        model.addAttribute("locations", locations);
        return "locations";
    }

    @PostMapping
    public String insertLocation(Model model, @RequestBody Location newLocation, HttpServletRequest request) {
        Collection<Location> locations = locationService.getAllLocations();
        Location savedLocation = locationService.insertNewLocation(newLocation);
        locations.add(savedLocation);
        model.addAttribute("locations", locations);
        return "locations :: locations-table";
    }

    @DeleteMapping("/{id}")
    public String removeLocationById(Model model, @PathVariable("id") Long locationId) {
        locationService.removeLocationById(locationId);
        Collection<Location> locations = locationService.getAllLocations();
        model.addAttribute("locations", locations);
        return "locations :: locations-table";
    }

    @PutMapping("/{id}")
    public String updateLocationById(Model model, @PathVariable("id") Long locationId, @RequestBody Location location) {
        Location updated = locationService.updateLocation(locationId, location);
        Collection<Location> locations = locationService.getAllLocations();
        model.addAttribute("locations", locations);
        return "locations :: locations-table";
    }
}
