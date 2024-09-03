package com.example.controler;

import com.example.customannotation.RateLimited;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyControllerRest {

    @RateLimited
    @GetMapping("/resource")
    public String getResource() {
        return "Resource accessed";
    }
}
