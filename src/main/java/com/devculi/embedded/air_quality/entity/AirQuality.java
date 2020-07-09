package com.devculi.embedded.air_quality.entity;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AirQuality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double ppm;
    private Double correctedPPM;
    private Double temperature;
    private Double humidity;
    private LocalDate date;
    private LocalTime time;
    private String weather;
    @OneToOne
    private Location location;
}
