package com.hospital.medtech.controllers;

import com.hospital.medtech.models.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        return "home"; 
    }

    @GetMapping("/hospitales/loginh")
    public String loginHospital() {
        return "hospitales/loginh"; 
    }

    @GetMapping("/hospitales/homehs")
    public String homeHospital(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/hospitales/loginh"; 
        }

        model.addAttribute("usuario", usuario);
        return "hospitales/homehs";
    }
}
