package com.medtech.hospitales.services;

import com.medtech.hospitales.dtos.ServicioRegistroDTO;
import com.medtech.hospitales.dtos.SubcategoriaDTO;
import com.medtech.hospitales.models.InfoDoctor;
import com.medtech.hospitales.models.ServicioHospitalario;
import com.medtech.hospitales.models.SubcategoriaServicio;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio que maneja las operaciones CRUD para los servicios hospitalarios,
 * incluyendo subcategorías y asociación de doctores.
 */
public class ServicioHospitalarioService {

    /**
     * EntityManager para la gestión de operaciones en la base de datos.
     */
    private final EntityManager entityManager;

    /**
     * Constructor del servicio de servicios hospitalarios.
     *
     * @param entityManager EntityManager para operaciones de persistencia.
     */
    public ServicioHospitalarioService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Registra un nuevo servicio hospitalario, junto con sus subcategorías y doctores asociados.
     *
     * @param dto Objeto DTO que contiene la información del nuevo servicio.
     * @throws RuntimeException si ocurre un error en la persistencia.
     */
    public void registrarServicio(ServicioRegistroDTO dto) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();

            ServicioHospitalario servicio = new ServicioHospitalario();
            servicio.setNombre(dto.getNombre());
            servicio.setDescripcion(dto.getDescripcion());

            List<SubcategoriaServicio> subcategorias = new ArrayList<>();
            for (SubcategoriaDTO subDTO : dto.getSubcategorias()) {
                SubcategoriaServicio sub = new SubcategoriaServicio();
                sub.setNombre(subDTO.getNombre());
                sub.setDescripcion(subDTO.getDescripcion());
                sub.setPrecio(subDTO.getPrecio());
                sub.setServicio(servicio);
                subcategorias.add(sub);
            }
            servicio.setSubcategorias(subcategorias);

            List<InfoDoctor> doctores = new ArrayList<>();
            for (Long idDoctor : dto.getIdDoctores()) {
                InfoDoctor doctor = entityManager.find(InfoDoctor.class, idDoctor);
                if (doctor != null) {
                    doctores.add(doctor);
                } else {
                    throw new IllegalArgumentException("Doctor con ID " + idDoctor + " no encontrado");
                }
            }
            servicio.setDoctores(doctores);

            entityManager.persist(servicio);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al registrar servicio hospitalario: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene la lista de todos los servicios hospitalarios registrados.
     *
     * @return Lista de objetos ServicioHospitalario.
     */
    public List<ServicioHospitalario> listarServicios() {
        return entityManager
            .createQuery("SELECT s FROM ServicioHospitalario s", ServicioHospitalario.class)
            .getResultList();
    }

    /**
     * Busca un servicio hospitalario por su ID.
     *
     * @param id ID del servicio a buscar.
     * @return El servicio hospitalario encontrado, o null si no existe.
     */
    public ServicioHospitalario buscarServicioPorId(Long id) {
        return entityManager.find(ServicioHospitalario.class, id);
    }

    /**
     * Actualiza un servicio hospitalario existente, reemplazando sus datos,
     * sus subcategorías y sus doctores asociados.
     *
     * @param id ID del servicio a actualizar.
     * @param dto Objeto DTO con la nueva información.
     * @throws RuntimeException si ocurre un error durante la actualización.
     */
    public void actualizarServicio(Long id, ServicioRegistroDTO dto) {
        EntityTransaction tx = entityManager.getTransaction();
        try {
            tx.begin();

            ServicioHospitalario servicio = entityManager.find(ServicioHospitalario.class, id);
            if (servicio == null) {
                throw new IllegalArgumentException("Servicio con ID " + id + " no encontrado");
            }

            servicio.setNombre(dto.getNombre());
            servicio.setDescripcion(dto.getDescripcion());

            // Limpiar subcategorías anteriores
            servicio.getSubcategorias().clear();
            entityManager.flush();

            for (SubcategoriaDTO subDTO : dto.getSubcategorias()) {
                SubcategoriaServicio sub = new SubcategoriaServicio();
                sub.setNombre(subDTO.getNombre());
                sub.setDescripcion(subDTO.getDescripcion());
                sub.setPrecio(subDTO.getPrecio());
                sub.setServicio(servicio);
                servicio.getSubcategorias().add(sub);
            }

            // Limpiar y actualizar doctores
            servicio.getDoctores().clear();
            List<InfoDoctor> doctores = new ArrayList<>();

            for (Long idDoctor : dto.getIdDoctores()) {
                InfoDoctor doctor = entityManager.find(InfoDoctor.class, idDoctor);
                if (doctor == null) {
                    throw new IllegalArgumentException("Doctor con ID " + idDoctor + " no encontrado");
                }
                doctores.add(doctor);
            }
            servicio.setDoctores(doctores);

            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw new RuntimeException("Error al actualizar servicio hospitalario: " + e.getMessage(), e);
        }
    }
}
