package com.hospital.medtech.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReservaController {

    @GetMapping("/hospitales/reservas")
    public String mostrarReservas() {
        return "hospitales/reservas"; // Solo carga la vista Thymeleaf
    }
}
