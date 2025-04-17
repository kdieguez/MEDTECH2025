package com.medtech.hospitales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medtech.hospitales.models.Medicamento;
import com.medtech.hospitales.services.MedicamentoService;
import io.javalin.http.Context;

import java.util.Map;

public class MedicamentoController {

    private final MedicamentoService service = new MedicamentoService();
    private final ObjectMapper objectMapper;

    public MedicamentoController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void guardarMedicamento(Context ctx) {
        try {
            Medicamento medicamento = objectMapper.readValue(ctx.body(), Medicamento.class);
            service.guardarMedicamento(medicamento);
            ctx.status(201).json(Map.of("mensaje", "Medicamento guardado correctamente"));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al guardar el medicamento"));
        }
    }
}
