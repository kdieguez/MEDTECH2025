package com.medtech.hospitales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medtech.hospitales.dtos.FormularioCitaDTO;
import com.medtech.hospitales.services.FormularioCitaService;
import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

public class FormularioCitaControllerTest {

    @Mock private Context ctx;
    @Mock private FormularioCitaService service;

    private ObjectMapper mapper;
    private FormularioCitaController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        controller = new FormularioCitaController(mapper) {
            @Override
            public void guardarFormularioCita(Context ctx) {
                try {
                    FormularioCitaDTO dto = mapper.readValue(ctx.body(), FormularioCitaDTO.class);
                    service.guardarFormulario(dto);
                    ctx.status(201).json(Map.of("mensaje", "Formulario de cita guardado exitosamente"));
                } catch (Exception e) {
                    ctx.status(500).json(Map.of("error", "Error al guardar formulario de cita"));
                }
            }
        };
    }

    @Test
    public void testGuardarFormularioCita_exito() throws Exception {
        String json = "{\"idCita\":1,\"diagnostico\":\"Dolor abdominal leve\",\"pasosSiguientes\":\"Revisar en 7 días\",\"urlsResultadosExamenes\":[]}";

        when(ctx.body()).thenReturn(json);
        when(ctx.status(any(Integer.class))).thenReturn(ctx);
        when(ctx.json(any(Map.class))).thenReturn(ctx);

        controller.guardarFormularioCita(ctx);

        verify(ctx).status(201);
        verify(ctx).json(Map.of("mensaje", "Formulario de cita guardado exitosamente"));
    }

    @Test
    public void testGuardarFormularioCita_fallaParseo() throws Exception {
        when(ctx.body()).thenThrow(new RuntimeException("error al parsear"));
        when(ctx.status(any(Integer.class))).thenReturn(ctx);
        when(ctx.json(any(Map.class))).thenReturn(ctx);

        controller.guardarFormularioCita(ctx);

        verify(ctx).status(500);
        verify(ctx).json(Map.of("error", "Error al guardar formulario de cita"));
    }
}
