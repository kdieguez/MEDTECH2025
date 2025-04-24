package com.medtech.hospitales.services;

import com.medtech.hospitales.dtos.FormularioCitaDTO;
import com.medtech.hospitales.models.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FormularioCitaServiceTest {

    @Mock
    EntityManager em;

    @Mock
    EntityTransaction tx;

    @InjectMocks
    FormularioCitaServiceTestable service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(em.getTransaction()).thenReturn(tx);
        service = new FormularioCitaServiceTestable(em);
    }

    @Test
    void guardarFormulario_deberiaGuardarExamenesYHistorial() {
        FormularioCitaDTO dto = new FormularioCitaDTO();
        dto.setIdCita(1L);
        dto.setUrlsResultadosExamenes(Arrays.asList("url1", "url2"));
        dto.setDiagnostico("diagnostico test");
        dto.setPasosSiguientes("pasos test");

        CitaMedica citaMock = new CitaMedica();
        when(em.find(CitaMedica.class, 1L)).thenReturn(citaMock);

        assertDoesNotThrow(() -> service.guardarFormulario(dto));

        verify(em, times(2)).persist(any(ResultadoExamen.class));
        verify(em).persist(any(HistorialServicio.class));
        verify(tx).commit();
    }

    @Test
    void guardarFormulario_deberiaLanzarExcepcionSiNoHayIdCita() {
        FormularioCitaDTO dto = new FormularioCitaDTO();
        dto.setIdCita(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.guardarFormulario(dto));
        assertEquals("idCita es obligatorio y no fue recibido.", ex.getMessage());
    }

    static class FormularioCitaServiceTestable extends FormularioCitaService {
        private final EntityManager em;

        public FormularioCitaServiceTestable(EntityManager em) {
            this.em = em;
        }

        @Override
        public void guardarFormulario(FormularioCitaDTO dto) {
            try {
                EntityTransaction tx = em.getTransaction();
                tx.begin();

                if (dto.getIdCita() == null) throw new IllegalArgumentException("idCita es obligatorio y no fue recibido.");
                CitaMedica cita = em.find(CitaMedica.class, dto.getIdCita());

                if (cita == null) throw new RuntimeException("No se encontró la cita médica con ID: " + dto.getIdCita());

                if (dto.getUrlsResultadosExamenes() != null) {
                    for (String url : dto.getUrlsResultadosExamenes()) {
                        ResultadoExamen resultado = new ResultadoExamen();
                        resultado.setUrl(url);
                        resultado.setCita(cita);
                        em.persist(resultado);
                    }
                }

                if (dto.getDiagnostico() != null || dto.getPasosSiguientes() != null) {
                    HistorialServicio historial = new HistorialServicio();
                    historial.setDiagnostico(dto.getDiagnostico());
                    historial.setPasosSiguientes(dto.getPasosSiguientes());
                    historial.setCita(cita);
                    em.persist(historial);
                }

                tx.commit();
            } catch (Exception e) {
                if (em.getTransaction().isActive()) em.getTransaction().rollback();
                throw e;
            }
        }
    }
}