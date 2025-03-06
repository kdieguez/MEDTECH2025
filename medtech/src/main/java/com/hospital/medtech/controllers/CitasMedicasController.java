package com.hospital.medtech.controllers;

import com.hospital.medtech.models.CitasMedicas;
import com.hospital.medtech.service.CitasMedicasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/citas")
public class CitasMedicasController {

    @Autowired
    private CitasMedicasService citasMedicasService;

    // Obtener todas las citas
    @GetMapping
    public List<CitasMedicas> obtenerTodasLasCitas() {
        return citasMedicasService.obtenerTodasLasCitas();
    }

    // Obtener citas de un paciente específico
   // @GetMapping("/paciente/{idPaciente}")
    //public List<CitasMedicas> obtenerCitasPorPaciente(@PathVariable Long idPaciente) {
        //return citasMedicasService.obtenerCitasPorPaciente(idPaciente);
    //}

    // Obtener citas por doctor
   // @GetMapping("/doctor/{idDoctor}")
    //public List<CitasMedicas> obtenerCitasPorDoctor(@PathVariable Long idDoctor) {
    //    return citasMedicasService.obtenerCitasPorDoctor(idDoctor);
    //}

    // Agendar una cita
    @PostMapping
    public CitasMedicas agendarCita(@RequestBody CitasMedicas cita) {
        return citasMedicasService.agendarCita(cita);
    }

    // Cancelar una cita
    @DeleteMapping("/{id}")
    public void cancelarCita(@PathVariable Long id) {
        citasMedicasService.cancelarCita(id);
    }

    // Finalizar cita y agregar receta
  //  @PutMapping("/{id}/finalizar")
    //public CitasMedicas finalizarCita(@PathVariable Long id, @RequestBody String receta) {
      //  return citasMedicasService.finalizarCita(id, receta);
    //}
}


