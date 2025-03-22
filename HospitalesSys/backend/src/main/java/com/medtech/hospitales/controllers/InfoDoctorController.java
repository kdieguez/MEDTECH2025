package com.medtech.hospitales.controllers;

import com.medtech.hospitales.dtos.DoctorDTO;
import io.javalin.Javalin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.math.BigDecimal;
import java.util.List;

public class InfoDoctorController {

    // Singleton de EntityManagerFactory, ya configurado
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("HospitalesPU");

    public static void addRoutes(Javalin app) {

        app.get("/api/doctores", ctx -> {
            EntityManager em = emf.createEntityManager();

            try {
                @SuppressWarnings("unchecked") // Evita el warning de conversión de tipos
                List<Object[]> resultados = em.createNativeQuery(
                    "SELECT " +
                        "idoc.ID_INFO_DOCTOR, " +
                        "u.NOMBRE || ' ' || u.APELLIDO AS NOMBRE_COMPLETO, " +
                        "idoc.NUMCOLEGIADO, " +
                        "idoc.FOTOGRAFIA, " +
                        "esp.NOMBRE_ESPECIALIDAD " +
                    "FROM " +
                        "INFO_DOCTOR idoc " +
                        "INNER JOIN USUARIOS u ON idoc.ID_USUARIO = u.ID_USUARIO " +
                        "INNER JOIN ESPECIALIDAD_X_DOCTOR edoc ON idoc.ID_INFO_DOCTOR = edoc.ID_INFO_DOCTOR " +
                        "INNER JOIN ESPECIALIDADES esp ON edoc.ID_ESPECIALIDAD = esp.ID_ESPECIALIDAD"
                ).getResultList();

                // Mapear los resultados al DTO
                List<DoctorDTO> doctores = resultados.stream().map(row -> {
                    DoctorDTO dto = new DoctorDTO();
                    dto.setIdDoctor(((BigDecimal) row[0]).longValue());
                    dto.setNombreCompleto((String) row[1]);
                    dto.setNumColegiado((String) row[2]);
                    dto.setFotografia((String) row[3]);
                    dto.setEspecialidad((String) row[4]);
                    return dto;
                }).toList();

                ctx.json(doctores);
            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).result("Error al obtener doctores: " + e.getMessage());
            } finally {
                em.close();
            }
        });
    }

    // Cerrar el EntityManagerFactory cuando termines la app (opcional)
    public static void cerrar() {
        emf.close();
    }
}
