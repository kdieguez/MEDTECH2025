//Archivo donde está la API REST

package com.hospital.medtech.controllers;

import com.hospital.medtech.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hospitales")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @PostMapping("/crear")
    public ResponseEntity<String> crearHospital(@RequestParam String nombre) {
        try {
            int idGenerado = hospitalService.agregarHospital(nombre);
            return ResponseEntity.ok("Hospital creado con ID: " + idGenerado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear el hospital: " + e.getMessage());
        }
    }
}

