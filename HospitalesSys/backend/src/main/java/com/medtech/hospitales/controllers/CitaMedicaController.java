package com.medtech.hospitales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medtech.hospitales.dtos.CitaRegistroDTO;
import com.medtech.hospitales.dtos.FormularioCitaDTO;
import com.medtech.hospitales.dtos.CitaDTO;
import com.medtech.hospitales.services.CitaMedicaService;
import com.medtech.hospitales.services.FormularioCitaService;
import com.medtech.hospitales.services.RecetaMedicaService;

import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Controlador encargado de gestionar las operaciones relacionadas con las citas médicas,
 * incluyendo el registro de citas, consulta de horarios disponibles, creación de recetas médicas, entre otras.
 */
public class CitaMedicaController {

    /**
     * Servicio encargado de la lógica de negocio relacionada con las citas médicas.
     */
    private final CitaMedicaService service = new CitaMedicaService();

    /**
     * Objeto para el mapeo de datos JSON a objetos Java.
     */
    private final ObjectMapper objectMapper;

    /**
     * Constructor del controlador que recibe un ObjectMapper.
     *
     * @param objectMapper instancia de ObjectMapper para deserialización de datos
     */
    public CitaMedicaController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Registra una nueva cita médica a partir de los datos enviados en el cuerpo de la solicitud.
     *
     * @param ctx contexto de Javalin que contiene la solicitud y respuesta
     */
    public void registrarCita(Context ctx) {
        try {
            CitaRegistroDTO dto = objectMapper.readValue(ctx.body(), CitaRegistroDTO.class);
            System.out.println("DTO parseado correctamente: " + dto.getFechaHora());

            service.registrarCita(dto);
            ctx.status(201).json("Cita registrada correctamente");

        } catch (RuntimeException ex) {
            ctx.status(400).json(Map.of("error", ex.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error interno al registrar la cita"));
        }
    }

    /**
     * Obtiene las horas disponibles para agendar una cita con un doctor en una fecha específica.
     *
     * @param ctx contexto de Javalin que contiene la solicitud y respuesta
     */
    public void obtenerHorasDisponibles(Context ctx) {
        try {
            Long idDoctor = Long.parseLong(ctx.queryParam("idDoctor"));
            LocalDate fecha = LocalDate.parse(ctx.queryParam("fecha"));

            List<String> horas = service.obtenerHorasDisponiblesParaDoctor(idDoctor, fecha);
            ctx.json(horas);

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(400).json(Map.of("error", "Parámetros inválidos o error al obtener horarios"));
        }
    }

    /**
     * Obtiene todas las citas médicas registradas en el sistema.
     *
     * @param ctx contexto de Javalin que contiene la solicitud y respuesta
     */
    public void obtenerTodasLasCitas(Context ctx) {
        try {
            List<CitaDTO> citas = service.obtenerTodasLasCitas();
            ctx.json(citas);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al obtener citas"));
        }
    }

    /**
     * Obtiene todas las citas médicas asociadas a un doctor específico.
     *
     * @param ctx contexto de Javalin que contiene la solicitud y respuesta
     */
    public void obtenerMisCitas(Context ctx) {
        try {
            Long idUsuario = Long.parseLong(ctx.queryParam("idUsuario"));
            System.out.println("ID de usuario recibido en citas/mias: " + idUsuario);

            List<CitaDTO> citas = service.obtenerCitasPorDoctor(idUsuario);
            ctx.json(citas);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al obtener citas del doctor"));
        }
    }

    /**
     * Guarda el formulario de una cita médica enviado en el cuerpo de la solicitud.
     *
     * @param ctx contexto de Javalin que contiene la solicitud y respuesta
     */
    public void guardarFormularioCita(Context ctx) {
        try {
            FormularioCitaDTO dto = objectMapper.readValue(ctx.body(), FormularioCitaDTO.class);

            FormularioCitaService service = new FormularioCitaService();
            service.guardarFormulario(dto);

            ctx.status(201).json(Map.of("mensaje", "Formulario de cita guardado exitosamente"));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al guardar el formulario de cita"));
        }
    }

    /**
     * Obtiene los datos de la receta médica generada a partir de una cita específica.
     *
     * @param ctx contexto de Javalin que contiene la solicitud y respuesta
     */
    public void obtenerDatosReceta(Context ctx) {
        try {
            Long idCita = Long.parseLong(ctx.pathParam("idCita"));
            FormularioCitaService service = new FormularioCitaService();
            var recetaDTO = service.generarReceta(idCita);

            ctx.json(recetaDTO);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al generar receta médica"));
        }
    }

    /**
     * Crea una receta médica asociada a una cita específica.
     *
     * @param ctx contexto de Javalin que contiene la solicitud y respuesta
     */
    public void crearRecetaMedica(Context ctx) {
        try {
            Long idCita = Long.parseLong(ctx.pathParam("id"));
            RecetaMedicaService recetaService = new RecetaMedicaService();
            recetaService.crearEncabezadoReceta(idCita);

            ctx.status(201).json(Map.of("mensaje", "Receta médica creada exitosamente"));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al crear receta médica"));
        }
    }

}
