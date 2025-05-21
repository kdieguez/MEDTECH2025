package com.medtech.hospitales.services;

import com.medtech.hospitales.dtos.DoctorRegistroDTO;
import com.medtech.hospitales.models.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con
 * el registro y manejo de la información profesional de doctores en el sistema hospitalario.
 * 
 * Incluye la creación del perfil del doctor, sus teléfonos y especialidades médicas.
 */
public class DoctorService {

    /**
     * EntityManager utilizado para realizar las operaciones de persistencia.
     */
    private final EntityManager entityManager;

    /**
     * Constructor del servicio, recibe el EntityManager para conexión a la base de datos.
     *
     * @param entityManager instancia de EntityManager para persistencia
     */
    public DoctorService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Registra el perfil profesional de un doctor, incluyendo:
     * - Fotografía y número de colegiado.
     * - Asociación con un usuario existente.
     * - Lista de teléfonos de contacto.
     * - Lista de especialidades médicas, permitiendo crear nuevas si es necesario.
     *
     * @param dto Objeto DTO con los datos del doctor para registro.
     * @throws IllegalArgumentException si el usuario no existe o alguna especialidad es inválida.
     * @throws RuntimeException en caso de errores en la persistencia.
     */
    public void registrarPerfilDoctor(DoctorRegistroDTO dto) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            // Crear nueva instancia de InfoDoctor
            InfoDoctor infoDoctor = new InfoDoctor();
            infoDoctor.setFotografia(dto.getFotografia());
            infoDoctor.setNumColegiado(dto.getNumColegiado());

            // Asociar el usuario correspondiente
            Usuario usuario = entityManager.find(Usuario.class, dto.getIdUsuario());
            if (usuario == null) {
                throw new IllegalArgumentException("Usuario no encontrado con ID: " + dto.getIdUsuario());
            }
            infoDoctor.setUsuario(usuario);

            // Mapear y asignar teléfonos
            List<TelefonoDoctor> telefonos = dto.getTelefonos().stream()
                    .map(numero -> {
                        TelefonoDoctor telefonoDoctor = new TelefonoDoctor();
                        telefonoDoctor.setTelefono(numero);
                        telefonoDoctor.setInfoDoctor(infoDoctor);
                        return telefonoDoctor;
                    })
                    .collect(Collectors.toList());
            infoDoctor.setTelefonos(telefonos);

            // Mapear y asignar especialidades
            List<EspecialidadDoctor> especialidades = dto.getEspecialidades().stream()
                    .map(especialidadDTO -> {
                        Especialidad especialidad;

                        if (especialidadDTO.getIdEspecialidad() != null && !especialidadDTO.getIdEspecialidad().toString().isEmpty()) {
                            especialidad = entityManager.find(Especialidad.class, especialidadDTO.getIdEspecialidad());
                        } else if (especialidadDTO.getNombreNueva() != null && !especialidadDTO.getNombreNueva().isBlank()) {
                            List<Especialidad> existentes = entityManager
                                    .createQuery("SELECT e FROM Especialidad e WHERE UPPER(e.nombre) = :nombre", Especialidad.class)
                                    .setParameter("nombre", especialidadDTO.getNombreNueva().toUpperCase())
                                    .getResultList();

                            if (!existentes.isEmpty()) {
                                especialidad = existentes.get(0);
                            } else {
                                especialidad = new Especialidad();
                                especialidad.setNombre(especialidadDTO.getNombreNueva());
                                entityManager.persist(especialidad);
                            }
                        } else {
                            throw new IllegalArgumentException("Especialidad inválida: debe tener ID o nombre.");
                        }

                        EspecialidadDoctor especialidadDoctor = new EspecialidadDoctor();
                        especialidadDoctor.setEspecialidad(especialidad);
                        especialidadDoctor.setUniversidad(especialidadDTO.getUniversidad());

                        LocalDate fecha = LocalDate.parse(especialidadDTO.getFechaGraduacion());
                        especialidadDoctor.setFechaGraduacion(fecha);

                        especialidadDoctor.setFotografiaTitulo(especialidadDTO.getFotografiaTitulo());
                        especialidadDoctor.setInfoDoctor(infoDoctor);

                        return especialidadDoctor;
                    })
                    .collect(Collectors.toList());

            infoDoctor.setEspecialidades(especialidades);

            // Persistir toda la información
            entityManager.persist(infoDoctor);

            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
