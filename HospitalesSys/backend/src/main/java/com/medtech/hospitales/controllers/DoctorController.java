package com.medtech.hospitales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medtech.hospitales.dtos.DoctorRegistroDTO;
import com.medtech.hospitales.dtos.ErrorDTO;
import com.medtech.hospitales.models.*;
import com.medtech.hospitales.services.DoctorService;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;

public class DoctorController {

    private final DoctorService doctorService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private EntityManager entityManager = null;

    public DoctorController(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.doctorService = new DoctorService(entityManager);
    }

    public Handler registrarPerfilDoctor() {
        return ctx -> {
            try {
                DoctorRegistroDTO dto = objectMapper.readValue(ctx.body(), DoctorRegistroDTO.class);
                doctorService.registrarPerfilDoctor(dto);
                ctx.status(201).result("Perfil de doctor registrado correctamente.");
            } catch (Exception e) {
                ctx.status(400).result("Error al registrar perfil: " + e.getMessage());
            }
        };
    }

    public Handler verificarPerfilDoctor = ctx -> {
        Long idUsuario = Long.parseLong(ctx.pathParam("id"));

        Long count = entityManager
                .createQuery("SELECT COUNT(d) FROM InfoDoctor d WHERE d.usuario.id = :id", Long.class)
                .setParameter("id", idUsuario)
                .getSingleResult();

        ctx.json(count > 0);
    };

    public Handler listarDoctores = ctx -> {
        List<InfoDoctor> doctores = entityManager
            .createQuery("SELECT d FROM InfoDoctor d", InfoDoctor.class)
            .getResultList();
    
            List<DoctorResumen> resumenes = doctores.stream()
            .map(d -> {
                Usuario u = d.getUsuario();
                if (u == null) return null;
        
                try {
                    return new DoctorResumen(
                        d.getId(),
                        u.getNombre(),
                        u.getApellido(),
                        d.getFotografia()
                    );
                } catch (Exception e) {
                    return null;
                }
            })
            .filter(dto -> dto != null)
            .collect(Collectors.toList());        
    
        ctx.json(resumenes);
    };
    
    

    public Handler detalleDoctor = ctx -> {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));

            Usuario usuario = entityManager.find(Usuario.class, id);
            if (usuario == null) {
                ctx.status(404).json("Usuario no encontrado con ID: " + id);
                return;
            }

            InfoDoctor infoDoctor = entityManager.createQuery(
                    "FROM InfoDoctor d WHERE d.usuario.id = :id", InfoDoctor.class)
                    .setParameter("id", id)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (infoDoctor == null) {
                ctx.status(404).json("InfoDoctor no encontrada con ID de usuario: " + id);
                return;
            }

            List<TelefonoDoctor> telefonos = entityManager.createQuery(
                    "FROM TelefonoDoctor t WHERE t.infoDoctor.usuario.id = :id", TelefonoDoctor.class)
                    .setParameter("id", id)
                    .getResultList();

            List<EspecialidadDoctor> especialidades = entityManager.createQuery(
                    "FROM EspecialidadDoctor e WHERE e.infoDoctor.usuario.id = :id", EspecialidadDoctor.class)
                    .setParameter("id", id)
                    .getResultList();

            DoctorDetalleDTO dto = new DoctorDetalleDTO(
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getApellido(),
                    usuario.getEmail(),
                    infoDoctor.getNumColegiado(),
                    infoDoctor.getFotografia(),
                    telefonos,
                    especialidades,
                    List.of()
            );

            ctx.json(dto);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(new ErrorDTO("Error interno: " + e.getMessage()));
        }
    };

    public Handler listarDoctoresInfo = ctx -> {
        List<InfoDoctor> doctores = entityManager
                .createQuery("SELECT d FROM InfoDoctor d", InfoDoctor.class)
                .getResultList();
        ctx.json(doctores);
    };

    public static class DoctorResumen {
        public Long id;
        public String nombre;
        public String apellido;
        public String fotografia;

        public DoctorResumen(Long id, String nombre, String apellido, String fotografia) {
            this.id = id;
            this.nombre = nombre;
            this.apellido = apellido;
            this.fotografia = fotografia;
        }
    }

    public static class DoctorDetalleDTO {
        public Long id;
        public String nombre;
        public String apellido;
        public String email;
        public String colegiado;
        public String fotografia;
        public List<TelefonoDoctor> telefonos;
        public List<EspecialidadDoctor> especialidades;
        public List<Object> pacientes;

        public DoctorDetalleDTO(Long id, String nombre, String apellido, String email,
                                String colegiado, String fotografia,
                                List<TelefonoDoctor> telefonos,
                                List<EspecialidadDoctor> especialidades,
                                List<Object> pacientes) {
            this.id = id;
            this.nombre = nombre;
            this.apellido = apellido;
            this.email = email;
            this.colegiado = colegiado;
            this.fotografia = fotografia;
            this.telefonos = telefonos;
            this.especialidades = especialidades;
            this.pacientes = pacientes;
        }
    }
    
    public Handler doctoresPorServicio = ctx -> {
        Long idServicio = Long.parseLong(ctx.pathParam("id"));
    
        List<ServicioXDoctor> relaciones = entityManager.createQuery(
            "SELECT s FROM ServicioXDoctor s WHERE s.servicio.id = :id",
            ServicioXDoctor.class
        )
        .setParameter("id", idServicio)
        .getResultList();
    
        List<DoctorResumen> doctores = relaciones.stream()
            .map(rel -> rel.getDoctor())
            .filter(d -> d.getUsuario() != null)
            .map(d -> new DoctorResumen(
                d.getId(),
                d.getUsuario().getNombre(),
                d.getUsuario().getApellido(),
                d.getFotografia()
            ))
            .collect(Collectors.toList());
    
        ctx.json(doctores);
    };    
    
}
