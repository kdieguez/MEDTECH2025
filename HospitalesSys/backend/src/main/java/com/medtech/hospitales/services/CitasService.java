package com.medtech.hospitales.services;

import com.medtech.hospitales.models.CitaMedica;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CitasService {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("HospitalesPU");

    // Listar todas las citas
    public List<CitaMedica> listarTodas() {
        EntityManager em = emf.createEntityManager();
        List<CitaMedica> citas = em.createQuery("SELECT c FROM CitaMedica c", CitaMedica.class).getResultList();
        em.close();
        return citas;
    }

    // Consultar disponibilidad de horarios
    public List<String> obtenerHorariosDisponibles(LocalDate fecha, Long doctorId) {
        EntityManager em = emf.createEntityManager();

        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia = fecha.atTime(23, 59, 59);

        // Citas de ese día para ese doctor
        List<CitaMedica> citasExistentes = em.createQuery(
                "SELECT c FROM CitaMedica c WHERE c.fechaHora BETWEEN :inicioDia AND :finDia AND c.idDoctor = :doctorId",
                CitaMedica.class)
                .setParameter("inicioDia", inicioDia)
                .setParameter("finDia", finDia)
                .setParameter("doctorId", doctorId)
                .getResultList();

        em.close();

        String[] horarios = {
                "08:00", "08:30", "09:00", "09:30",
                "10:00", "10:30", "11:00", "11:30",
                "14:00", "14:30", "15:00", "15:30",
                "16:00", "16:30"
        };

        List<String> ocupados = citasExistentes.stream()
                .map(c -> c.getFechaHora().toLocalTime().toString().substring(0, 5))
                .toList();

        return List.of(horarios).stream()
                .filter(h -> !ocupados.contains(h))
                .toList();
    }

    // Crear una nueva cita
    public void crearCita(CitaMedica cita) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Armamos el LocalDateTime desde fecha + hora (que vienen como string)
            String fechaStr = cita.getFecha(); // Nueva propiedad temporal en DTO
            String horaStr = cita.getHora();   // Nueva propiedad temporal en DTO

            LocalDateTime fechaHora = LocalDateTime.parse(fechaStr + "T" + horaStr);
            cita.setFechaHora(fechaHora);

            em.persist(cita);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al crear la cita: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    public void cerrar() {
        emf.close();
    }
}