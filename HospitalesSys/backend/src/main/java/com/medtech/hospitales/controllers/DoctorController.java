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

/**
 * Controlador que maneja las operaciones relacionadas con los doctores,
 * como el registro, consulta de perfiles, y listado de doctores disponibles.
 */
public class DoctorController {

    /**
     * Servicio de negocio para operaciones relacionadas a doctores.
     */
    private final DoctorService doctorService;

    /**
     * Mapper para convertir entre objetos JSON y clases Java.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * EntityManager para operaciones de persistencia.
     */
    private EntityManager entityManager = null;

    /**
     * Constructor que inicializa el controlador con un EntityManager.
     *
     * @param entityManager instancia de EntityManager
     */
    public DoctorController(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.doctorService = new DoctorService(entityManager);
    }

    /**
     * Handler que registra un nuevo perfil de doctor.
     *
     * @return Handler de Javalin
     */
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

    /**
     * Handler que verifica si un usuario tiene un perfil de doctor registrado.
     * 
     * @param ctx contexto de Javalin con el parámetro de ID del usuario
     */
    public Handler verificarPerfilDoctor = ctx -> {
        Long idUsuario = Long.parseLong(ctx.pathParam("id"));

        Long count = entityManager
                .createQuery("SELECT COUNT(d) FROM InfoDoctor d WHERE d.usuario.id = :id", Long.class)
                .setParameter("id", idUsuario)
                .getSingleResult();

        ctx.json(count > 0);
    };

    /**
     * Handler que lista todos los doctores registrados en el sistema.
     * 
     * @param ctx contexto de Javalin
     */
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

    /**
     * Handler que obtiene el detalle de un doctor específico, incluyendo
     * sus teléfonos y especialidades.
     * 
     * @param ctx contexto de Javalin
     */
    public Handler detalleDoctor = ctx -> {
        try {
            Long idInfoDoctor = Long.parseLong(ctx.pathParam("id"));

InfoDoctor infoDoctor = entityManager.find(InfoDoctor.class, idInfoDoctor);
if (infoDoctor == null) {
    ctx.status(404).json("InfoDoctor no encontrado con ID: " + idInfoDoctor);
    return;
}

Usuario usuario = infoDoctor.getUsuario();


            if (infoDoctor == null) {
                ctx.status(404).json("InfoDoctor no encontrada con ID de usuario: " + idInfoDoctor);
                return;
            }

            List<TelefonoDoctor> telefonos = entityManager.createQuery(
    "FROM TelefonoDoctor t WHERE t.infoDoctor.id = :id", TelefonoDoctor.class)
    .setParameter("id", idInfoDoctor)
    .getResultList();

List<EspecialidadDoctor> especialidades = entityManager.createQuery(
    "FROM EspecialidadDoctor e WHERE e.infoDoctor.id = :id", EspecialidadDoctor.class)
    .setParameter("id", idInfoDoctor)
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

    /**
     * Handler que lista toda la información de los doctores (InfoDoctor).
     * 
     * @param ctx contexto de Javalin
     */
    public Handler listarDoctoresInfo = ctx -> {
        List<InfoDoctor> doctores = entityManager
                .createQuery("SELECT d FROM InfoDoctor d", InfoDoctor.class)
                .getResultList();
        ctx.json(doctores);
    };

    /**
     * Clase interna que representa un resumen de los datos de un doctor.
     */
    public static class DoctorResumen {
        public Long id;
        public String nombre;
        public String apellido;
        public String fotografia;

        /**
         * Constructor de DoctorResumen.
         *
         * @param id          ID del doctor
         * @param nombre      Nombre del doctor
         * @param apellido    Apellido del doctor
         * @param fotografia  URL de la fotografía del doctor
         */
        public DoctorResumen(Long id, String nombre, String apellido, String fotografia) {
            this.id = id;
            this.nombre = nombre;
            this.apellido = apellido;
            this.fotografia = fotografia;
        }
    }

    /**
     * Clase interna que representa el detalle completo de un doctor.
     */
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

        /**
         * Constructor de DoctorDetalleDTO.
         *
         * @param id               ID del doctor
         * @param nombre           Nombre
         * @param apellido         Apellido
         * @param email            Email de contacto
         * @param colegiado        Número de colegiado
         * @param fotografia       Fotografía del doctor
         * @param telefonos        Lista de teléfonos asociados
         * @param especialidades   Lista de especialidades
         * @param pacientes        Lista de pacientes (no utilizada aún)
         */
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

    /**
     * Handler que lista los doctores asociados a un servicio específico.
     * 
     * @param ctx contexto de Javalin
     */
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
