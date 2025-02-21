package com.hospital.medtech.controllers;

import com.hospital.medtech.models.Doctor;
import com.hospital.medtech.service.DoctorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class DoctorController {
    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/catalogoDoctores")
    public String catalogoDoctores(Model model) {
        List<Doctor> doctores = doctorService.obtenerDoctores();
        model.addAttribute("doctores", doctores);
        return "catalogoDoctores";
    }

    @GetMapping("/doctor/{id}")
    public String detalleDoctor(@PathVariable int id, Model model) {
        Doctor doctor = doctorService.obtenerDoctorPorId(id);
        if (doctor == null) {
            return "error404"; // Página de error si no encuentra el doctor
        }

        model.addAttribute("doctor", doctor);
        return "detalleDoctor";
    }
}
