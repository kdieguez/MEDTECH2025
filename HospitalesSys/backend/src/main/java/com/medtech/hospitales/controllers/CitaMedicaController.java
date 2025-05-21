package com.medtech.hospitales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medtech.hospitales.dtos.*;
import com.medtech.hospitales.models.CitaMedica;
import com.medtech.hospitales.models.RecetaMedica;
import com.medtech.hospitales.services.CitaMedicaService;
import com.medtech.hospitales.services.FormularioCitaService;
import com.medtech.hospitales.services.RecetaMedicaCompletaService;
import com.medtech.hospitales.utils.JPAUtil;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Controlador encargado de gestionar todas las operaciones relacionadas con las citas médicas del hospital.
 * Incluye registro, consulta, eliminación, y generación de formularios y recetas.
 * Utiliza servicios para la lógica de negocio y Javalin para el manejo de peticiones HTTP.
 */
public class CitaMedicaController {

    private final CitaMedicaService service = new CitaMedicaService();
    private final ObjectMapper objectMapper;
    private final RecetaMedicaCompletaService recetaService = new RecetaMedicaCompletaService();

    /**
     * Constructor del controlador.
     *
     * @param objectMapper instancia para mapear JSON a objetos Java.
     */
    public CitaMedicaController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Registra una nueva cita médica desde una petición externa (como seguros).
     *
     * @param ctx Contexto HTTP con el body JSON de tipo CitaExternaDTO.
     */
    public void registrarCita(Context ctx) {
        try {
            CitaExternaDTO dto = objectMapper.readValue(ctx.body(), CitaExternaDTO.class);
            Long idCita = service.registrarCitaExterna(dto);
            ctx.status(201).json(Map.of("idCita", idCita));
        } catch (RuntimeException ex) {
            ctx.status(400).json(Map.of("error", ex.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error interno al registrar la cita"));
        }
    }

    /**
     * Obtiene las horas disponibles para un doctor en una fecha dada.
     * Requiere parámetros idDoctor y fecha.
     *
     * @param ctx Contexto HTTP.
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
     * Devuelve todas las citas médicas registradas.
     *
     * @param ctx Contexto HTTP.
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
     * Devuelve todas las citas de un doctor.
     * Requiere parámetro idUsuario.
     *
     * @param ctx Contexto HTTP.
     */
    public void obtenerMisCitas(Context ctx) {
        try {
            Long idUsuario = Long.parseLong(ctx.queryParam("idUsuario"));
            List<CitaDTO> citas = service.obtenerCitasPorDoctor(idUsuario);
            ctx.json(citas);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al obtener citas del doctor"));
        }
    }

    /**
     * Guarda el formulario médico para una cita, incluyendo diagnóstico y pasos a seguir.
     *
     * @param ctx Contexto HTTP con el cuerpo JSON de FormularioCitaDTO.
     */
    public void guardarFormularioCita(Context ctx) {
        try {
            FormularioCitaDTO dto = objectMapper.readValue(ctx.body(), FormularioCitaDTO.class);
            new FormularioCitaService().guardarFormulario(dto);
            ctx.status(201).json(Map.of("mensaje", "Formulario de cita guardado exitosamente"));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al guardar el formulario de cita"));
        }
    }

    /**
     * Genera y retorna los datos para la receta médica de una cita.
     *
     * @param ctx Contexto HTTP con path param idCita.
     */
    public void obtenerDatosReceta(Context ctx) {
        try {
            Long idCita = Long.parseLong(ctx.pathParam("idCita"));
            var recetaDTO = new FormularioCitaService().generarReceta(idCita);
            ctx.json(recetaDTO);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al generar receta médica"));
        }
    }

    /**
     * Guarda medicamentos recetados en una receta médica.
     *
     * @param ctx Contexto HTTP con path param id y arreglo JSON de MedicamentoRecetadoDTO.
     */
    public void guardarMedicamentosPorReceta(Context ctx) {
        try {
            Long idRM = Long.parseLong(ctx.pathParam("id"));
            MedicamentoRecetadoDTO[] medicamentos = objectMapper.readValue(ctx.bodyAsBytes(), MedicamentoRecetadoDTO[].class);
            recetaService.guardarMedicamentosPorReceta(idRM, List.of(medicamentos));
            ctx.status(201).json(Map.of("mensaje", "Medicamentos guardados correctamente"));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al guardar los medicamentos"));
        }
    }

    /**
     * Guarda una receta médica completa para una cita con comentario.
     *
     * @param ctx Contexto HTTP con path param id y query param comentario.
     */
    public void guardarRecetaCompleta(Context ctx) {
        try {
            Long idCita = Long.parseLong(ctx.pathParam("id"));
            String comentario = ctx.queryParam("comentario");

            if (comentario == null || comentario.isBlank()) {
                throw new RuntimeException("El comentario/anotación es obligatorio.");
            }

            recetaService.guardarReceta(idCita, comentario);
            ctx.status(201).json(Map.of("mensaje", "Receta completa guardada con éxito"));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "No se pudo guardar la receta completa"));
        }
    }

    /**
     * Crea una receta médica vacía si no existe y retorna su ID.
     *
     * @param ctx Contexto HTTP con path param id.
     */
    public void crearRecetaYRetornarId(Context ctx) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long idCita = Long.parseLong(ctx.pathParam("id"));
            RecetaMedica existente = em.createQuery("""
                SELECT r FROM RecetaMedica r
                WHERE r.citaMedica.id = :idCita
            """, RecetaMedica.class)
            .setParameter("idCita", idCita)
            .setMaxResults(1)
            .getResultStream()
            .findFirst()
            .orElse(null);

            if (existente != null) {
                ctx.status(200).json(Map.of("idRM", existente.getId()));
                return;
            }

            em.getTransaction().begin();
            CitaMedica cita = em.find(CitaMedica.class, idCita);
            if (cita == null) {
                ctx.status(404).json(Map.of("error", "Cita médica no encontrada"));
                return;
            }

            RecetaMedica nueva = new RecetaMedica();
            nueva.setCitaMedica(cita);
            nueva.setCodigoReceta("HOSP-SEG-" + cita.getId());
            nueva.setAnotaciones(null);
            nueva.setTotal(0.0);

            em.persist(nueva);
            em.getTransaction().commit();

            ctx.status(201).json(Map.of("idRM", nueva.getId()));
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "No se pudo crear la receta médica"));
        } finally {
            em.close();
        }
    }

    /**
     * Verifica si ya existe un formulario médico asociado a la cita.
     *
     * @param ctx Contexto HTTP con path param id.
     */
    public void verificarFormularioLlenado(Context ctx) {
        try {
            Long idCita = Long.parseLong(ctx.pathParam("id"));
            boolean existe = new FormularioCitaService().existeFormularioParaCita(idCita);
            ctx.json(Map.of("formularioLlenado", existe));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al verificar formulario"));
        }
    }

    /**
     * Handler que obtiene las imágenes de resultados de examen de una cita médica.
     */
    public Handler obtenerImagenesResultados = ctx -> {
        Long idCita = Long.parseLong(ctx.pathParam("id"));
        EntityManager em = JPAUtil.getEntityManager();

        try {
            List<String> urls = em.createQuery("""
                SELECT r.url FROM ResultadoExamen r
                WHERE r.cita.id = :idCita
            """, String.class)
            .setParameter("idCita", idCita)
            .getResultList();

            List<Map<String, String>> json = urls.stream()
                .map(url -> Map.of("url", url))
                .toList();

            ctx.json(json);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al obtener imágenes"));
        } finally {
            em.close();
        }
    };

    /**
     * Obtiene el formulario médico asociado a una cita.
     *
     * @param ctx Contexto HTTP con path param id.
     */
    public void obtenerFormularioCita(Context ctx) {
        try {
            Long idCita = Long.parseLong(ctx.pathParam("id"));
            FormularioCitaDTO dto = new FormularioCitaService().obtenerFormularioPorCita(idCita);
            ctx.json(dto);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al obtener datos del formulario"));
        }
    }

    /**
     * Registra una cita médica externa desde seguros, imprimiendo subcategoría recibida.
     *
     * @param ctx Contexto HTTP con body JSON de CitaExternaDTO.
     */
    public void registrarCitaExterna(Context ctx) {
        try {
            CitaExternaDTO dto = objectMapper.readValue(ctx.body(), CitaExternaDTO.class);
            System.out.println("Subcategoría recibida desde seguros: [" + dto.getNombreSubcategoria() + "]");
            Long idCita = service.registrarCitaExterna(dto);
            ctx.status(201).json(Map.of("idCita", idCita));
        } catch (RuntimeException ex) {
            ctx.status(409).json(Map.of("error", ex.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error interno al registrar la cita externa"));
        }
    }

    /**
     * Elimina una cita médica por su ID.
     *
     * @param ctx Contexto HTTP con path param id.
     */
    public void eliminarCita(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            CitaMedica cita = em.find(CitaMedica.class, id);
            if (cita == null) {
                ctx.status(404).json(Map.of("error", "Cita no encontrada"));
                return;
            }
            em.remove(cita);
            em.getTransaction().commit();
            ctx.status(200).json(Map.of("mensaje", "Cita eliminada correctamente"));
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al eliminar la cita"));
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza los datos de una cita externa (fecha y subcategoría).
     *
     * @param ctx Contexto HTTP con path param id y body JSON con nuevaFechaHora y nuevaSubcategoria.
     */
    public void actualizarCitaExterna(Context ctx) {
        try {
            Long idCita = Long.parseLong(ctx.pathParam("id"));
            Map<String, String> json = new ObjectMapper().readValue(ctx.body(), Map.class);

            String nuevaFechaHora = json.get("nuevaFechaHora");
            String nuevaSubcategoria = json.get("nuevaSubcategoria");

            service.actualizarCitaExterna(idCita, nuevaFechaHora, nuevaSubcategoria);

            ctx.status(200).json(Map.of("mensaje", "Cita actualizada correctamente"));
        } catch (RuntimeException ex) {
            ctx.status(400).json(Map.of("error", ex.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error interno al actualizar la cita externa"));
        }
    }
}
