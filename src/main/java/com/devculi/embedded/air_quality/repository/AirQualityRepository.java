package com.devculi.embedded.air_quality.repository;

import java.util.List;

import com.devculi.embedded.air_quality.entity.AirQuality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirQualityRepository extends JpaRepository<AirQuality, Long> {

}
