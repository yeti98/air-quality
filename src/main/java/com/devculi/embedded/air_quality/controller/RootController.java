package com.devculi.embedded.air_quality.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class RootController {

    @GetMapping
    public String sayHello() {
        return "redirect:/air-qualities";
    }
}
