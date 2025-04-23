package com.medtech.hospitales.services;

import com.medtech.hospitales.dtos.FormularioCitaDTO;
import com.medtech.hospitales.dtos.RecetaDTO;
import com.medtech.hospitales.models.*;
import com.medtech.hospitales.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

/**
 * Servicio que maneja las operaciones relacionadas con el formulario de citas médicas,
 * incluyendo el registro de diagnósticos, resultados de exámenes y generación de recetas médicas.
 */
public class FormularioCitaService {

    /**
     * EntityManager inyectado para operaciones de persistencia.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Guarda la información del formulario de una cita médica, incluyendo
     * resultados de exámenes y diagnóstico, asociándolos a la cita existente.
     *
     * @param dto Objeto DTO que contiene los datos del formulario.
     */
    public void guardarFormulario(FormularioCitaDTO dto) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            System.out.println("➡️ Llegó FormularioCitaDTO: " + dto);

            if (dto.getIdCita() == null) {
                System.out.println("❌ idCita es null");
                throw new IllegalArgumentException("idCita es obligatorio y no fue recibido.");
            } else {
                System.out.println("✅ idCita recibido correctamente: " + dto.getIdCita());
            }

            em.getTransaction().begin();

            // Buscar la cita médica
            CitaMedica cita = em.find(CitaMedica.class, dto.getIdCita());
            if (cita == null) {
                throw new RuntimeException("No se encontró la cita médica con ID: " + dto.getIdCita());
            }

            // Guardar resultados de exámenes si existen
            if (dto.getUrlsResultadosExamenes() != null && !dto.getUrlsResultadosExamenes().isEmpty()) {
                for (String url : dto.getUrlsResultadosExamenes()) {
                    ResultadoExamen resultado = new ResultadoExamen();
                    resultado.setUrl(url);
                    resultado.setCita(cita);
                    em.persist(resultado);
                }
            }

            // Guardar historial médico si existe diagnóstico o pasos siguientes
            if (dto.getDiagnostico() != null || dto.getPasosSiguientes() != null) {
                HistorialServicio historial = new HistorialServicio();
                historial.setDiagnostico(dto.getDiagnostico());
                historial.setPasosSiguientes(dto.getPasosSiguientes());
                historial.setCita(cita);
                em.persist(historial);
            }

            em.getTransaction().commit();
            System.out.println("✅ Formulario de cita guardado correctamente.");

        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Error al guardar el formulario de cita: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * Genera un objeto de receta médica basado en la información de una cita específica.
     *
     * @param idCita ID de la cita médica a partir de la cual se generará la receta.
     * @return Objeto RecetaDTO con los datos de la receta generada.
     */
    public RecetaDTO generarReceta(Long idCita) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            CitaMedica cita = em.find(CitaMedica.class, idCita);
            if (cita == null) {
                throw new RuntimeException("No se encontró la cita con ID: " + idCita);
            }

            RecetaDTO receta = new RecetaDTO();
            receta.setFechaCita(cita.getFechaHora().toLocalDate());
            receta.setCodigoReceta("PAZ000-" + cita.getId());

            // Datos del paciente
            PerfilPaciente paciente = cita.getPaciente();
            if (paciente != null && paciente.getUsuario() != null) {
                receta.setNombrePaciente(
                    paciente.getUsuario().getNombre() + " " + paciente.getUsuario().getApellido()
                );
            }

            // Datos del doctor
            InfoDoctor doctor = cita.getInfoDoctor();
            if (doctor != null && doctor.getUsuario() != null) {
                receta.setNombreDoctor(
                    doctor.getUsuario().getNombre() + " " + doctor.getUsuario().getApellido()
                );
                receta.setNumColegiado(doctor.getNumColegiado());
            }

            // Especialidad
            SubcategoriaServicio especialidad = cita.getSubcategoria();
            if (especialidad != null) {
                receta.setEspecialidad(especialidad.getNombre());
            }

            return receta;

        } finally {
            em.close();
        }
    }
}
