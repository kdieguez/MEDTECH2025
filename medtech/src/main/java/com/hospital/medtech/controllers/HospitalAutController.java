package com.hospital.medtech.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hospitales")
public class HospitalAutController {

    @GetMapping("/login")
    public String loginHospital() {
        return "hospitales/loginh"; 
    }
}
