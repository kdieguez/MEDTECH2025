package com.medtech.hospitales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medtech.hospitales.models.Medicamento;
import com.medtech.hospitales.services.MedicamentoService;
import com.medtech.hospitales.utils.JPAUtil;

import io.javalin.http.Context;

import java.util.Map;

/**
 * Controlador encargado de gestionar las operaciones relacionadas con medicamentos,
 * como el registro de nuevos medicamentos en el sistema.
 */
public class MedicamentoController {

    /**
     * Servicio encargado de la lógica de negocio para medicamentos.
     */
    private final MedicamentoService service = new MedicamentoService();

    /**
     * Mapper utilizado para la conversión entre objetos JSON y clases Java.
     */
    private final ObjectMapper objectMapper;

    /**
     * Constructor del controlador que recibe un ObjectMapper para deserialización de JSON.
     *
     * @param objectMapper instancia de ObjectMapper
     */
    public MedicamentoController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Guarda un nuevo medicamento en el sistema utilizando los datos enviados en el cuerpo de la solicitud.
     * 
     * @param ctx contexto de Javalin que contiene la solicitud y la respuesta
     */
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

    public void obtenerMedicamentos(Context ctx) {
    try {
        var em = JPAUtil.getEntityManager();
        var medicamentos = em.createQuery("SELECT m FROM Medicamento m", Medicamento.class)
                             .getResultList();
        em.close();
        ctx.json(medicamentos);
    } catch (Exception e) {
        e.printStackTrace();
        ctx.status(500).json(Map.of("error", "Error al obtener medicamentos"));
    }
}

}
