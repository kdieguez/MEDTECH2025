/*package com.medtech.hospitales.services;

import com.medtech.hospitales.models.CitaMedica;
import com.medtech.hospitales.models.PerfilPaciente;
import com.medtech.hospitales.models.InfoDoctor;
import com.medtech.hospitales.models.RecetaMedicaHeader;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class RecetaMedicaServiceTest {

    EntityManager em;
    EntityTransaction tx;
    RecetaMedicaService service;

    @BeforeEach
    void setUp() {
        em = mock(EntityManager.class);
        tx = mock(EntityTransaction.class);

        when(em.getTransaction()).thenReturn(tx);

       /* service = new RecetaMedicaService() {
            @Override
            public void crearEncabezadoReceta(Long idCita) {
               tx.begin();

                CitaMedica citaMock = mock(CitaMedica.class);
                PerfilPaciente pacienteMock = mock(PerfilPaciente.class);
                InfoDoctor doctorMock = mock(InfoDoctor.class);

                when(em.find(CitaMedica.class, idCita)).thenReturn(citaMock);
                when(citaMock.getPaciente()).thenReturn(pacienteMock);
                when(citaMock.getInfoDoctor()).thenReturn(doctorMock);

                RecetaMedicaHeader receta = new RecetaMedicaHeader();
                receta.setFechaReceta(LocalDate.now());
                receta.setCodigoReceta("PAZ000-" + idCita);
                receta.setPaciente(pacienteMock);
                receta.setInfoDoctor(doctorMock);

                em.persist(receta);

                tx.commit();
            }
        };
    }

    @Test
    void crearEncabezadoReceta_deberiaGuardarSinErrores() {
        assertDoesNotThrow(() -> service.crearEncabezadoReceta(1L));
    }
} */