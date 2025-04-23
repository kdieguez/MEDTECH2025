package com.medtech.hospitales.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Set;
import com.medtech.hospitales.dtos.CitaDTO;
import com.medtech.hospitales.dtos.CitaRegistroDTO;
import com.medtech.hospitales.models.*;
import com.medtech.hospitales.utils.JPAUtil;
import jakarta.persistence.EntityManager;

/**
 * Servicio que maneja las operaciones relacionadas con citas médicas.
 */
public class CitaMedicaService {

    /**
     * Registra una nueva cita médica en el sistema.
     *
     * @param dto DTO con los datos de la cita a registrar.
     */
    public void registrarCita(CitaRegistroDTO dto) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            if (existeCitaParaDoctorEnHorario(dto.getIdDoctor(), dto.getFechaHora())) {
                throw new RuntimeException("Ya existe una cita para ese doctor en esa fecha y hora");
            }

            CitaMedica cita = new CitaMedica();
            cita.setPaciente(em.find(PerfilPaciente.class, dto.getIdPaciente()));
            cita.setInfoDoctor(em.find(InfoDoctor.class, dto.getIdDoctor()));
            cita.setSubcategoria(em.find(SubcategoriaServicio.class, dto.getIdSubcategoria()));
            cita.setFechaHora(dto.getFechaHora());

            em.persist(cita);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Error al registrar la cita médica: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * Verifica si ya existe una cita registrada para un doctor en una fecha y hora específicas.
     *
     * @param idDoctor ID del doctor
     * @param fechaHora Fecha y hora a verificar
     * @return true si existe cita, false en caso contrario
     */
    public boolean existeCitaParaDoctorEnHorario(Long idDoctor, LocalDateTime fechaHora) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long total = em.createQuery("""
                SELECT COUNT(c) FROM CitaMedica c
                WHERE c.infoDoctor.id = :idDoctor AND c.fechaHora = :fechaHora
            """, Long.class)
            .setParameter("idDoctor", idDoctor)
            .setParameter("fechaHora", fechaHora)
            .getSingleResult();

            return total > 0;
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene las horas disponibles para agendar una cita con un doctor en una fecha específica.
     *
     * @param idDoctor ID del doctor
     * @param fecha Fecha a consultar
     * @return lista de horas disponibles
     */
    public List<String> obtenerHorasDisponiblesParaDoctor(Long idDoctor, LocalDate fecha) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            LocalDateTime inicioDia = fecha.atStartOfDay();
            LocalDateTime finDia = fecha.atTime(23, 59, 59);

            List<LocalDateTime> citasOcupadas = em.createQuery("""
                SELECT c.fechaHora FROM CitaMedica c
                WHERE c.infoDoctor.id = :idDoctor
                  AND c.fechaHora BETWEEN :inicio AND :fin
            """, LocalDateTime.class)
            .setParameter("idDoctor", idDoctor)
            .setParameter("inicio", inicioDia)
            .setParameter("fin", finDia)
            .getResultList();

            List<String> todasLasHoras = new ArrayList<>();
            for (int h = 8; h <= 16; h++) {
                todasLasHoras.add(String.format("%02d:00", h));
                todasLasHoras.add(String.format("%02d:30", h));
            }

            Set<String> horasOcupadas = citasOcupadas.stream()
                .map(h -> h.toLocalTime().withSecond(0).toString().substring(0, 5))
                .collect(Collectors.toSet());

            return todasLasHoras.stream()
                .filter(hora -> !horasOcupadas.contains(hora))
                .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene todas las citas registradas en el sistema.
     *
     * @return lista de citas en formato DTO
     */
    public List<CitaDTO> obtenerTodasLasCitas() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("""
                SELECT new com.medtech.hospitales.dtos.CitaDTO(
                    c.id,
                    CONCAT(pu.nombre, ' ', pu.apellido),
                    p.documentoIdentificacion,
                    CONCAT(du.nombre, ' ', du.apellido),
                    s.nombre,
                    sc.nombre,
                    c.fechaHora
                )
                FROM CitaMedica c
                JOIN PerfilPaciente p ON c.paciente.id = p.id
                JOIN Usuario pu ON pu.id = p.usuario.id
                JOIN InfoDoctor d ON c.infoDoctor.id = d.id
                JOIN Usuario du ON du.id = d.usuario.id
                JOIN SubcategoriaServicio sc ON c.subcategoria.id = sc.id
                JOIN ServicioHospitalario s ON sc.servicio.id = s.id
            """, CitaDTO.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene las citas asociadas a un doctor basado en su ID de usuario.
     *
     * @param idUsuario ID del usuario del doctor
     * @return lista de citas del doctor
     */
    public List<CitaDTO> obtenerCitasPorDoctor(Long idUsuario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Long> idsDoctor = em.createQuery("""
                SELECT d.id FROM InfoDoctor d
                WHERE d.usuario.id = :idUsuario
            """, Long.class)
            .setParameter("idUsuario", idUsuario)
            .getResultList();

            if (idsDoctor.isEmpty()) {
                return new ArrayList<>();
            }

            Long idDoctor = idsDoctor.get(0);

            return em.createQuery("""
                SELECT new com.medtech.hospitales.dtos.CitaDTO(
                    c.id,
                    CONCAT(pu.nombre, ' ', pu.apellido),
                    p.documentoIdentificacion,
                    CONCAT(du.nombre, ' ', du.apellido),
                    s.nombre,
                    sc.nombre,
                    c.fechaHora
                )
                FROM CitaMedica c
                JOIN PerfilPaciente p ON c.paciente.id = p.id
                JOIN Usuario pu ON pu.id = p.usuario.id
                JOIN InfoDoctor d ON c.infoDoctor.id = d.id
                JOIN Usuario du ON du.id = d.usuario.id
                JOIN SubcategoriaServicio sc ON c.subcategoria.id = sc.id
                JOIN ServicioHospitalario s ON sc.servicio.id = s.id
                WHERE c.infoDoctor.id = :idDoctor
            """, CitaDTO.class)
            .setParameter("idDoctor", idDoctor)
            .getResultList();
        } finally {
            em.close();
        }
    }
}
