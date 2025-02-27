package com.hospital.medtech.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/resultado-receta")
public class ResultadoRecetaController {

    @GetMapping("/{id}")
    public String mostrarFormularioResultados(@PathVariable int id, Model model) {
        model.addAttribute("idCita", id);
        return "hospitales/resultado_receta"; // Redirige al formulario de resultados
    }
}