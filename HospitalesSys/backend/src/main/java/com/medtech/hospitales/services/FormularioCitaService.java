package com.medtech.hospitales.services;

import com.medtech.hospitales.dtos.FormularioCitaDTO;
import com.medtech.hospitales.dtos.MedicamentoRecetadoDTO;
import com.medtech.hospitales.dtos.RecetaDTO;
import com.medtech.hospitales.models.*;
import com.medtech.hospitales.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio encargado de manejar las operaciones relacionadas con
 * formularios médicos completados al finalizar una cita.
 * 
 * Esto incluye guardar resultados, historial clínico, recetas
 * médicas y generar documentos detallados para visualización.
 */
public class FormularioCitaService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Guarda el formulario de cita médica, incluyendo:
     * - Resultados de exámenes
     * - Historial clínico (diagnóstico y pasos siguientes)
     * - Receta médica (si corresponde)
     *
     * @param dto Objeto con los datos del formulario a registrar
     */
    public void guardarFormulario(FormularioCitaDTO dto) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            if (dto.getIdCita() == null) {
                throw new IllegalArgumentException("idCita es obligatorio y no fue recibido.");
            }

            em.getTransaction().begin();

            CitaMedica cita = em.find(CitaMedica.class, dto.getIdCita());
            if (cita == null) {
                throw new RuntimeException("No se encontró la cita médica con ID: " + dto.getIdCita());
            }

            if (dto.getUrlsResultadosExamenes() != null && !dto.getUrlsResultadosExamenes().isEmpty()) {
                for (String url : dto.getUrlsResultadosExamenes()) {
                    ResultadoExamen resultado = new ResultadoExamen();
                    resultado.setUrl(url);
                    resultado.setCita(cita);
                    em.persist(resultado);
                }
            }

            if (dto.getDiagnostico() != null || dto.getPasosSiguientes() != null) {
                HistorialServicio historial = new HistorialServicio();
                historial.setDiagnostico(dto.getDiagnostico());
                historial.setPasosSiguientes(dto.getPasosSiguientes());
                historial.setCita(cita);
                em.persist(historial);
            }

            if (dto.isCrearRecetaMedica() && dto.getMedicamentos() != null && !dto.getMedicamentos().isEmpty()) {
                RecetaMedica receta = new RecetaMedica();
                receta.setCitaMedica(cita);
                receta.setCodigoReceta("HOSP-SEG-" + cita.getId());
                receta.setAnotaciones(dto.getDiagnostico());

                double total = 0.0;
                for (MedicamentoRecetadoDTO medDTO : dto.getMedicamentos()) {
                    Medicamento medicamento = em.find(Medicamento.class, medDTO.getIdMedicamento());
                    if (medicamento == null) {
                        throw new RuntimeException("Medicamento no encontrado con ID: " + medDTO.getIdMedicamento());
                    }
                    MedicamentoRecetado med = new MedicamentoRecetado();
                    med.setMedicamento(medicamento);
                    med.setDosis(medDTO.getDosis());
                    med.setFrecuencia(medDTO.getFrecuencia());
                    med.setDuracion(medDTO.getDuracion());
                    med.setReceta(receta);
                    em.persist(med);
                    total += medicamento.getPrecio();
                }

                receta.setTotal(total);
                em.persist(receta);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Error al guardar el formulario de cita: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * Genera un objeto RecetaDTO con todos los datos de la receta
     * asociada a una cita médica, incluyendo paciente, doctor,
     * medicamentos, diagnóstico y recomendaciones.
     *
     * @param idCita ID de la cita médica
     * @return objeto RecetaDTO con todos los datos detallados
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
            receta.setCodigoReceta("HOSP-SEG-" + cita.getId());
    
            PerfilPaciente paciente = cita.getPaciente();
            if (paciente != null && paciente.getUsuario() != null) {
                receta.setNombrePaciente(paciente.getUsuario().getNombre() + " " + paciente.getUsuario().getApellido());
            }
    
            InfoDoctor doctor = cita.getInfoDoctor();
            if (doctor != null && doctor.getUsuario() != null) {
                receta.setNombreDoctor(doctor.getUsuario().getNombre() + " " + doctor.getUsuario().getApellido());
                receta.setNumColegiado(doctor.getNumColegiado());
            }
    
            // Especialidades del doctor
            List<String> especialidades = em.createQuery("""
                SELECT e.nombre 
                FROM EspecialidadDoctor ed
                JOIN ed.especialidad e
                WHERE ed.infoDoctor.id = :idDoctor
            """, String.class)
            .setParameter("idDoctor", doctor.getId())
            .getResultList();
            receta.setEspecialidades(especialidades);
    
            // Teléfonos del doctor
            List<String> telefonos = em.createQuery("""
                SELECT t.telefono
                FROM TelefonoDoctor t
                WHERE t.infoDoctor.id = :idDoctor
            """, String.class)
            .setParameter("idDoctor", doctor.getId())
            .getResultList();
            receta.setTelefonosDoctor(telefonos);
    
            // Título del hospital (HeaderFooter)
            String tituloTexto = em.createQuery("""
                SELECT h.contenido FROM HeaderFooter h WHERE h.id = 22
            """, String.class)
            .setMaxResults(1)
            .getSingleResult();
            
            receta.setTituloHospital(tituloTexto);
            // Extraer diagnóstico y siguientes pasos desde HISTORIAL_SERVICIOS
            List<Object[]> historial = em.createQuery("""
                SELECT h.diagnostico, h.pasosSiguientes
                FROM HistorialServicio h
                WHERE h.cita.id = :idCita
            """, Object[].class)
            .setParameter("idCita", idCita)
            .getResultList();

            if (!historial.isEmpty()) {
                Object[] fila = historial.get(0);
                receta.setDiagnostico((String) fila[0]);
                receta.setPasosSiguientes((String) fila[1]);
            }
    
            // Datos de receta médica
            RecetaMedica recetaMedica = em.createQuery("""
                SELECT r FROM RecetaMedica r
                WHERE r.citaMedica.id = :idCita
            """, RecetaMedica.class)
            .setParameter("idCita", idCita)
            .setMaxResults(1)
            .getResultStream()
            .findFirst()
            .orElse(null);
    
            if (recetaMedica != null) {
                receta.setCodigoReceta(recetaMedica.getCodigoReceta());
                receta.setAnotaciones(recetaMedica.getAnotaciones());
                receta.setIdRM(recetaMedica.getId());
                receta.setTotal(recetaMedica.getTotal());
    
                List<MedicamentoRecetadoDTO> medicamentos = em.createQuery("""
                    SELECT new com.medtech.hospitales.dtos.MedicamentoRecetadoDTO(
                        m.medicamento.idMedicamento,
                        m.dosis,
                        m.frecuencia,
                        m.duracion
                    )
                    FROM MedicamentoRecetado m
                    WHERE m.receta.id = :idReceta
                """, MedicamentoRecetadoDTO.class)
                .setParameter("idReceta", recetaMedica.getId())
                .getResultList();
    
                receta.setMedicamentos(medicamentos);
            }
    
            return receta;
        } finally {
            em.close();
        }
    }

    /**
     * Verifica si ya existe un historial de cita médica guardado
     * para una cita específica.
     *
     * @param idCita ID de la cita médica
     * @return true si ya existe historial guardado, false si no
     */
    public boolean existeFormularioParaCita(Long idCita) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery("""
                SELECT COUNT(h) FROM HistorialServicio h WHERE h.cita.id = :idCita
            """, Long.class)
            .setParameter("idCita", idCita)
            .getSingleResult();
            return count > 0;
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene los datos previamente guardados del formulario de una cita,
     * incluyendo diagnóstico, pasos siguientes y resultados de exámenes.
     *
     * @param idCita ID de la cita médica
     * @return DTO con la información del formulario recuperado
     */
    public FormularioCitaDTO obtenerFormularioPorCita(Long idCita) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            HistorialServicio historial = em.createQuery("""
                SELECT h FROM HistorialServicio h WHERE h.cita.id = :idCita
            """, HistorialServicio.class)
            .setParameter("idCita", idCita)
            .setMaxResults(1)
            .getSingleResult();

            List<String> urls = em.createQuery("""
                SELECT r.url FROM ResultadoExamen r WHERE r.cita.id = :idCita
            """, String.class)
            .setParameter("idCita", idCita)
            .getResultList();

            FormularioCitaDTO dto = new FormularioCitaDTO();
            dto.setDiagnostico(historial.getDiagnostico());
            dto.setPasosSiguientes(historial.getPasosSiguientes());
            dto.setUrlsResultadosExamenes(urls);
            return dto;
        } finally {
            em.close();
        }
    }
}
