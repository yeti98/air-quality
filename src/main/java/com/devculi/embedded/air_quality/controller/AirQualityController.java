package com.devculi.embedded.air_quality.controller;

import com.devculi.embedded.air_quality.entity.AirQuality;
import com.devculi.embedded.air_quality.entity.TimeInterval;
import com.devculi.embedded.air_quality.service.AirQualityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/air-qualities")
public class AirQualityController {
    @Autowired
    AirQualityService airQualityService;

    @GetMapping
    public String getQualityByTimeInterval(
            @RequestParam(name = "interval", required = false, defaultValue = "T_1M") TimeInterval timeInterval) {

        return "airqualities";
    }

    @PostMapping
    @ResponseBody
    public void insertQuality(@RequestBody AirQuality airQuality) {
        airQuality.setDate(LocalDate.now());
        airQuality.setTime(LocalTime.now());
        airQualityService.saveQuality(airQuality);
        System.out.println(airQuality);
    }
}
