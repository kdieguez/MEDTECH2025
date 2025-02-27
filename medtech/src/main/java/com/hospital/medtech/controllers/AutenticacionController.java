package com.hospital.medtech.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AutenticacionController {

    @GetMapping("/loginh")
    public String loginh() {
        return "auth/loginh"; // Vista de login
    }
    

    @GetMapping("/registro")
    public String registro() {
        return "auth/registro"; // Vista de registro
    }

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return "auth/forgot-password"; // Vista de recuperación de contraseña
    }
}