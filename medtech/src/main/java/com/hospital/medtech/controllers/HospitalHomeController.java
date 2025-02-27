package com.hospital.medtech.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HospitalHomeController {

    @GetMapping("/hospitales")
    public String hospitalHome(Model model) {
        return "hospitales/homehs"; // Carga la vista homehs.html
    }
}