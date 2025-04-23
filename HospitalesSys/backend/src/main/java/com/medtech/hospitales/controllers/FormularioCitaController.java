package com.medtech.hospitales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medtech.hospitales.dtos.FormularioCitaDTO;
import com.medtech.hospitales.services.FormularioCitaService;
import io.javalin.http.Context;

import java.util.Map;

/**
 * Controlador encargado de gestionar el formulario de una cita médica,
 * permitiendo guardar la información adicional como diagnóstico, pasos a seguir y resultados.
 */
public class FormularioCitaController {

    /**
     * Servicio encargado de la lógica de negocio para el formulario de cita.
     */
    private final FormularioCitaService service = new FormularioCitaService();

    /**
     * Mapper utilizado para la conversión entre objetos JSON y clases Java.
     */
    private final ObjectMapper objectMapper;

    /**
     * Constructor del controlador que recibe un ObjectMapper para parseo de JSON.
     *
     * @param objectMapper instancia de ObjectMapper
     */
    public FormularioCitaController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Guarda la información adicional de una cita médica, como diagnóstico,
     * pasos a seguir y resultados de exámenes.
     * 
     * @param ctx contexto de Javalin que contiene la solicitud y la respuesta
     */
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
