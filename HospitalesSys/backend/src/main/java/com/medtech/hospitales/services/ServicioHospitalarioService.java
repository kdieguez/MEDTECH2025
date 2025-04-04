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

public class ServicioHospitalarioService {

    private final EntityManager entityManager;

    public ServicioHospitalarioService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

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

    public List<ServicioHospitalario> listarServicios() {
        return entityManager
            .createQuery("SELECT s FROM ServicioHospitalario s", ServicioHospitalario.class)
            .getResultList();
    }

    public ServicioHospitalario buscarServicioPorId(Long id) {
        return entityManager.find(ServicioHospitalario.class, id);
    }
}
