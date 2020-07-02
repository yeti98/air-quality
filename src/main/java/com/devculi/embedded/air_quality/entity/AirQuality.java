package com.devculi.embedded.air_quality.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    private LocalDateTime timeStamp;
    private LocalDate date;
    private LocalTime time;
    @OneToOne
    private Location location;
}
