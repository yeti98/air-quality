package com.devculi.embedded.air_quality.controller;

import com.devculi.embedded.air_quality.entity.AirQuality;
import com.devculi.embedded.air_quality.entity.TimeInterval;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/air-qualities")
public class AirQualityController {

    @GetMapping
    public String getQualityByTimeInterval(
            @RequestParam(name = "interval", required = false, defaultValue = "T_1M") TimeInterval timeInterval) {

        return "airqualities";
    }

    @PostMapping
    public String insertQuality(@RequestBody AirQuality airQuality) {
        return "air_quality_fragment";
    }
}
