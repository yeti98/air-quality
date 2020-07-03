package com.devculi.embedded.air_quality.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.devculi.embedded.air_quality.entity.AirQuality;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataModel {
    String date;
    List<Category> categories;
    List<Category> lineCategory;

    @Data
    @AllArgsConstructor
    public static class Category {
        String name;
        double value;
    }

    public DataModel(AirQuality airQuality) {
        date = airQuality.getTime().toString();
        categories = new ArrayList<>();
        categories.add(new Category("Độ ẩm", airQuality.getHumidity()));
        categories.add(new Category("PPM", airQuality.getCorrectedPPM()/10.0));

        lineCategory = new ArrayList<>();
        lineCategory.add(new Category("Nhiệt độ", airQuality.getTemperature()));
    }
}
