package com.devculi.embedded.air_quality.service;

import com.devculi.embedded.air_quality.entity.AirQuality;
import com.devculi.embedded.air_quality.entity.Location;
import com.devculi.embedded.air_quality.repository.AirQualityRepository;
import com.devculi.embedded.air_quality.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AirQualityService {
    @Autowired
    AirQualityRepository airQualityRepository;
    @Autowired
    LocationRepository locationRepository;

    public AirQuality saveQuality(AirQuality airQuality) {
        Objects.requireNonNull(airQuality);
        Objects.requireNonNull(airQuality.getLocation().getId());
        Optional<Location> locationOptional = locationRepository.findById(airQuality.getLocation().getId());
        locationOptional.ifPresent(airQuality::setLocation);
        return airQualityRepository.save(airQuality);
    }

    public List<AirQuality> getTop8Data() {
        List<AirQuality> list = airQualityRepository.findAll(PageRequest.of(0, 8, Sort.by("date", "time").descending()))
                .getContent();

        return list;
    }
}
