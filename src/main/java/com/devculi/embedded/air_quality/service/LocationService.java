package com.devculi.embedded.air_quality.service;

import com.devculi.embedded.air_quality.entity.Location;
import com.devculi.embedded.air_quality.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
public class LocationService {
    @Autowired
    LocationRepository locationRepository;
    public Collection<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Transactional(rollbackFor = Exception.class)
    public Location insertNewLocation(Location location) {
        Objects.requireNonNull(location);
        return locationRepository.save(location);
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean removeLocationById(final Long locationId) {
        Objects.requireNonNull(locationId);
        Optional<Location> locationOptional = locationRepository.findById(locationId);
        locationOptional.ifPresent(location -> locationRepository.delete(location));
        return true;
    }

    public Location updateLocation(final Long locationId, Location location) {
        Objects.requireNonNull(location);
        Optional<Location> locationOptional = locationRepository.findById(locationId);
        if (locationOptional.isPresent()) {
            Location updatedLocation = locationOptional.get();
            updatedLocation.setLocationLong(location.getLocationLong());
            updatedLocation.setLocationLat(location.getLocationLat());
            updatedLocation.setDescription(location.getDescription());
            locationRepository.save(updatedLocation);
            return updatedLocation;
        }
        return null;
    }
}
