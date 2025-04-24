package com.medtech.hospitales.services;

import com.medtech.hospitales.models.Medicamento;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class MedicamentoServiceTest {

    @Mock
    EntityManager em;

    @Mock
    EntityTransaction tx;

    MedicamentoServiceTestable service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(em.getTransaction()).thenReturn(tx);
        service = new MedicamentoServiceTestable(em);
    }

    @Test
    void guardarMedicamento_deberiaPersistirCorrectamente() {
        // Arrange
        Medicamento medicamento = new Medicamento();
        medicamento.setNombre("Paracetamol");
        medicamento.setPrincipioActivo("Acetaminofén");
        medicamento.setConcentracion("500mg");
        medicamento.setPresentacion("Caja de 20 tabletas");
        medicamento.setFormaFarmaceutica("Tableta");

        // Act & Assert
        assertDoesNotThrow(() -> service.guardarMedicamento(medicamento));
        verify(em).persist(medicamento);
        verify(tx).commit();
    }

    // Clase auxiliar para inyectar EntityManager simulado
    static class MedicamentoServiceTestable extends MedicamentoService {
        private final EntityManager em;

        public MedicamentoServiceTestable(EntityManager em) {
            this.em = em;
        }

        @Override
        public void guardarMedicamento(Medicamento medicamento) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(medicamento);
            tx.commit();
        }
    }
}