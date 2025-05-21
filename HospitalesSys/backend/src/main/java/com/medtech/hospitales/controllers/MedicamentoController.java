package com.medtech.hospitales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medtech.hospitales.models.Medicamento;
import com.medtech.hospitales.services.MedicamentoService;
import com.medtech.hospitales.utils.JPAUtil;
import io.javalin.http.Context;

import java.util.Map;

/**
 * Controlador encargado de gestionar las operaciones relacionadas con los medicamentos,
 * incluyendo su registro y obtención desde la base de datos.
 */
public class MedicamentoController {

    /**
     * Servicio que contiene la lógica de negocio para manejar medicamentos.
     */
    private final MedicamentoService service = new MedicamentoService();

    /**
     * Mapper utilizado para la conversión entre JSON y objetos Java.
     */
    private final ObjectMapper objectMapper;

    /**
     * Constructor del controlador.
     *
     * @param objectMapper instancia de ObjectMapper para deserialización JSON.
     */
    public MedicamentoController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Registra un nuevo medicamento en el sistema a partir del JSON recibido.
     *
     * @param ctx contexto HTTP que contiene el cuerpo con datos del medicamento.
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

    /**
     * Obtiene todos los medicamentos registrados en el sistema y los retorna como JSON.
     *
     * @param ctx contexto HTTP.
     */
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
