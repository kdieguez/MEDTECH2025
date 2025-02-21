package com.hospital.medtech.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class HospitalRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public int crearHospital(String nombreHospital) {
        return (int) entityManager.createNativeQuery(
                "CALL MEDTECH.Hospital_Nuevo(:nombre, :idSalida)")
                .setParameter("nombre", nombreHospital)
                .setParameter("idSalida", 0)
                .executeUpdate();
    }
}
