package com.devculi.embedded.air_quality.controller;

import com.devculi.embedded.air_quality.entity.AirQuality;
import com.devculi.embedded.air_quality.entity.TimeInterval;
import com.devculi.embedded.air_quality.model.DataModel;
import com.devculi.embedded.air_quality.service.AirQualityService;
import com.devculi.embedded.air_quality.service.PreDataSection;
import com.google.gson.Gson;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/air-qualities")
public class AirQualityController {
    @Autowired
    AirQualityService airQualityService;

    @GetMapping
    public String getQualityByTimeInterval(
            @RequestParam(name = "interval", required = false, defaultValue = "T_1M") TimeInterval timeInterval,
            Model model) throws FileNotFoundException {

        List<AirQuality> data = airQualityService.getTop8Data();
        List<DataModel> dataModel = new ArrayList<>();
        for (AirQuality aq : data) {
            dataModel.add(new DataModel(aq));
        }
        Collections.reverse(dataModel);
        Gson gson = new Gson();
        model.addAttribute("data", gson.toJson(dataModel));
        model.addAttribute("ts", data.get(0));
        int j = 0;
        if(data.get(0).getTemperature()>30 && data.get(0).getHumidity()>75) j=0;
        if(data.get(0).getTemperature()>30 && data.get(0).getHumidity()<=75) j=1;
        if(data.get(0).getTemperature()<=30 && data.get(0).getHumidity()>75) j=2;
        if(data.get(0).getTemperature()<=30 && data.get(0).getHumidity()<=75) j=3;
        System.out.println("Here: " + j);
        model.addAttribute("weather", PreDataSection.getPredict(j));
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
