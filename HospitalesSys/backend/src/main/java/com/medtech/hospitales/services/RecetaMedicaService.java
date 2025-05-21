package com.medtech.hospitales.services;

import com.medtech.hospitales.models.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

/**
 * Servicio que maneja la creación y gestión de recetas médicas,
 * incluyendo la asociación con medicamentos recetados y cálculo del total.
 */
public class RecetaMedicaService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Constructor que inyecta el EntityManager.
     *
     * @param entityManager instancia para manejo de persistencia
     */
    public RecetaMedicaService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Crea una nueva receta médica asociada a una cita y lista de medicamentos,
     * calcula el total basado en el precio de los medicamentos y guarda la receta.
     *
     * @param idCita       ID de la cita médica asociada
     * @param medicamentos lista de medicamentos recetados con dosis, frecuencia, etc.
     * @param anotaciones  anotaciones o comentarios para la receta médica
     * @return RecetaMedica persistida con ID generado
     */
    @Transactional
    public RecetaMedica crearReceta(Long idCita, List<MedicamentoRecetado> medicamentos, String anotaciones) {
        CitaMedica cita = entityManager.find(CitaMedica.class, idCita);
        if (cita == null) {
            throw new RuntimeException("Cita no encontrada");
        }

        RecetaMedica receta = new RecetaMedica();
        receta.setCitaMedica(cita);
        receta.setAnotaciones(anotaciones);

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

        return receta;
    }
}
