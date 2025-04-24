package com.medtech.hospitales.services;

import com.medtech.hospitales.dtos.CitaRegistroDTO;
import com.medtech.hospitales.models.CitaMedica;
import com.medtech.hospitales.models.InfoDoctor;
import com.medtech.hospitales.models.PerfilPaciente;
import com.medtech.hospitales.models.SubcategoriaServicio;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CitaMedicaServiceTest {

    @Mock
    EntityManager em;

    @Mock
    EntityTransaction tx;

    @InjectMocks
    CitaMedicaService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(em.getTransaction()).thenReturn(tx);
    }

    @Test
    void registrarCita_deberiaPersistirCitaCuandoNoHayConflicto() {
        CitaMedicaService service = new CitaMedicaService() {
            @Override
            public boolean existeCitaParaDoctorEnHorario(Long idDoctor, LocalDateTime fechaHora) {
                return false;
            }

            @Override
            public void registrarCita(CitaRegistroDTO dto) {
                EntityTransaction tx = em.getTransaction();
                tx.begin();

                CitaMedica cita = new CitaMedica();
                cita.setPaciente(new PerfilPaciente());
                cita.setInfoDoctor(new InfoDoctor());
                cita.setSubcategoria(new SubcategoriaServicio());
                cita.setFechaHora(dto.getFechaHora());

                em.persist(cita);
                tx.commit();
            }
        };

        CitaRegistroDTO dto = new CitaRegistroDTO();
        dto.setIdDoctor(1L);
        dto.setIdPaciente(2L);
        dto.setIdSubcategoria(3L);
        dto.setFechaHora(LocalDateTime.of(2025, 5, 1, 10, 0));

        assertDoesNotThrow(() -> service.registrarCita(dto));
    }

    @Test
    void registrarCita_deberiaLanzarExcepcionCuandoYaExisteCita() {
        CitaMedicaService service = new CitaMedicaService() {
            @Override
            public boolean existeCitaParaDoctorEnHorario(Long idDoctor, LocalDateTime fechaHora) {
                return true;
            }
        };

        CitaRegistroDTO dto = new CitaRegistroDTO();
        dto.setIdDoctor(1L);
        dto.setFechaHora(LocalDateTime.now());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.registrarCita(dto));
        assertTrue(ex.getMessage().contains("Ya existe una cita"));
    }
}