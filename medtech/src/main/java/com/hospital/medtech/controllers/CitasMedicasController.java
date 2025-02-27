package com.hospital.medtech.controllers;

import com.hospital.medtech.models.CitasMedicas;
import com.hospital.medtech.service.CitasMedicasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/citas")
public class CitasMedicasController {

    @Autowired
    private CitasMedicasService citasMedicasService;

    // Obtener todas las citas (Solo para secretaria y doctores)
    @GetMapping
    @PreAuthorize("hasAnyRole('SECRETARIA', 'DOCTOR')")
    public List<CitasMedicas> obtenerTodasLasCitas() {
        return citasMedicasService.obtenerTodasLasCitas();
    }

    // Obtener citas de un paciente específico (Solo para paciente o secretaria)
    @GetMapping("/paciente/{idPaciente}")
    @PreAuthorize("hasAnyRole('PACIENTE', 'SECRETARIA')")
    public List<CitasMedicas> obtenerCitasPorPaciente(@PathVariable Long idPaciente) {
        return citasMedicasService.obtenerCitasPorPaciente(idPaciente);
    }

    // Agendar una cita (Solo secretaria puede agendar)
    @PostMapping
    @PreAuthorize("hasRole('SECRETARIA')")
    public CitasMedicas agendarCita(@RequestBody CitasMedicas cita) {
        return citasMedicasService.agendarCita(cita);
    }

    // Cancelar una cita (Solo secretaria o doctor pueden cancelar)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SECRETARIA', 'DOCTOR')")
    public void cancelarCita(@PathVariable Long id) {
        citasMedicasService.cancelarCita(id);
    }

    // Finalizar cita y agregar receta (Solo doctor puede hacerlo)
    @PutMapping("/{id}/finalizar")
    @PreAuthorize("hasRole('DOCTOR')")
    public CitasMedicas finalizarCita(@PathVariable Long id, @RequestBody String receta) {
        return citasMedicasService.finalizarCita(id, receta);
    }
}