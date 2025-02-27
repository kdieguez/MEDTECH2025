package com.hospital.medtech.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Arrays;
import java.util.List;

@Controller
public class PacienteController {

    // Modelo de datos para historial de servicios
    public static class HistorialServicio {
        private String fecha;
        private String nombreServicio;
        private String doctor;
        private String detalles;

        public HistorialServicio(String fecha, String nombreServicio, String doctor, String detalles) {
            this.fecha = fecha;
            this.nombreServicio = nombreServicio;
            this.doctor = doctor;
            this.detalles = detalles;
        }

        public String getFecha() { return fecha; }
        public String getNombreServicio() { return nombreServicio; }
        public String getDoctor() { return doctor; }
        public String getDetalles() { return detalles; }
    }

    @GetMapping("/perfil-paciente")
    public String mostrarFichaPaciente(Model model) {
        // Datos de prueba para historial de servicios del paciente
        List<HistorialServicio> historial = Arrays.asList(
            new HistorialServicio("01/02/2024", "Consulta General", "Dr. Pérez", "Revisión médica completa"),
            new HistorialServicio("10/02/2024", "Examen de Sangre", "Laboratorio", "Pruebas de rutina")
        );

        // Pasar datos a la vista Thymeleaf
        model.addAttribute("historialServicios", historial);

        return "hospitales/perfilPaciente"; // Nombre del archivo en /templates/hospitales/
    }
}