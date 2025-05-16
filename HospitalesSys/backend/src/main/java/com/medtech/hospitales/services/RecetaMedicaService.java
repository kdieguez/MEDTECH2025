package com.medtech.hospitales.services;

import com.medtech.hospitales.models.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

public class RecetaMedicaService {

    @PersistenceContext
    private EntityManager entityManager;

    public RecetaMedicaService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public RecetaMedica crearReceta(Long idCita, List<MedicamentoRecetado> medicamentos, String anotaciones) {
        CitaMedica cita = entityManager.find(CitaMedica.class, idCita);
        if (cita == null) {
            throw new RuntimeException("Cita no encontrada");
        }

        RecetaMedica receta = new RecetaMedica();
        receta.setCitaMedica(cita);
        receta.setAnotaciones(anotaciones);

        // Código temporal: HOS-SEG-ID_CITA
        String codigo = "HOS-SEG-" + cita.getId();
        receta.setCodigoReceta(codigo);

        double total = 0.0;
        for (MedicamentoRecetado medRec : medicamentos) {
            Medicamento med = entityManager.find(Medicamento.class, medRec.getMedicamento().getIdMedicamento());
            if (med == null) {
                throw new RuntimeException("Medicamento no encontrado con ID: " + medRec.getMedicamento().getIdMedicamento());
            }
            medRec.setMedicamento(med);
            medRec.setReceta(receta);
            total += med.getPrecio();
        }

        receta.setTotal(total);
        receta.setMedicamentos(medicamentos);

        entityManager.persist(receta);
        // Medicamentos se guardan automáticamente por cascade

        return receta;
    }
}
