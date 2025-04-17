package com.medtech.hospitales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medtech.hospitales.dtos.FormularioCitaDTO;
import com.medtech.hospitales.services.FormularioCitaService;
import io.javalin.http.Context;

import java.util.Map;

public class FormularioCitaController {

    private final FormularioCitaService service = new FormularioCitaService();
    private final ObjectMapper objectMapper;

    public FormularioCitaController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void guardarFormularioCita(Context ctx) {
        try {
            FormularioCitaDTO dto = objectMapper.readValue(ctx.body(), FormularioCitaDTO.class);
            
            System.out.println("DTO recibido:");
            System.out.println("ID de cita -> " + dto.getIdCita());
            System.out.println("Diagnóstico -> " + dto.getDiagnostico());
            System.out.println("Pasos siguientes -> " + dto.getPasosSiguientes());
            System.out.println("URLs de resultados -> " + dto.getUrlsResultadosExamenes());
    
            service.guardarFormulario(dto);
    
            ctx.status(201).json(Map.of("mensaje", "Formulario de cita guardado exitosamente"));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al guardar formulario de cita"));
        }
    }
    
}
