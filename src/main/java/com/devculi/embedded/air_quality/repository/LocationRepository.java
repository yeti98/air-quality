package com.devculi.embedded.air_quality.repository;

import com.devculi.embedded.air_quality.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
