package com.medtech.hospitales.services;

import com.medtech.hospitales.dtos.ServicioRegistroDTO;
import com.medtech.hospitales.dtos.SubcategoriaDTO;
import com.medtech.hospitales.models.InfoDoctor;
import com.medtech.hospitales.models.ServicioHospitalario;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class ServicioHospitalarioServiceTest {

    @Mock
    EntityManager em;

    @Mock
    EntityTransaction tx;

    @InjectMocks
    ServicioHospitalarioService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(em.getTransaction()).thenReturn(tx);
        service = new ServicioHospitalarioService(em);
    }

    @Test
    void registrarServicio_noDeberiaLanzarExcepcion() {
        // Crear DTO
        ServicioRegistroDTO dto = new ServicioRegistroDTO();
        dto.setNombre("Laboratorio");
        dto.setDescripcion("Análisis clínicos");

        // Subcategorías
        SubcategoriaDTO sub1 = new SubcategoriaDTO();
        sub1.setNombre("Hematología");
        sub1.setDescripcion("Estudios de sangre");
        sub1.setPrecio(250.0);

        SubcategoriaDTO sub2 = new SubcategoriaDTO();
        sub2.setNombre("Química clínica");
        sub2.setDescripcion("Estudios bioquímicos");
        sub2.setPrecio(300.0);

        dto.setSubcategorias(Arrays.asList(sub1, sub2));

        // Doctores
        Long idDoctor = 1L;
        dto.setIdDoctores(Collections.singletonList(idDoctor));

        InfoDoctor mockDoctor = mock(InfoDoctor.class);
        when(em.find(InfoDoctor.class, idDoctor)).thenReturn(mockDoctor);

        assertDoesNotThrow(() -> service.registrarServicio(dto));
        verify(em).persist(any(ServicioHospitalario.class));
    }

    @Test
    void actualizarServicio_noDeberiaLanzarExcepcion() {
        // Datos iniciales
        Long servicioId = 10L;

        ServicioHospitalario servicioExistente = new ServicioHospitalario();
        servicioExistente.setNombre("Antiguo");
        servicioExistente.setDescripcion("Antigua descripción");
        servicioExistente.setSubcategorias(new java.util.ArrayList<>());
        servicioExistente.setDoctores(new java.util.ArrayList<>());

        when(em.getTransaction()).thenReturn(tx);
        when(em.find(ServicioHospitalario.class, servicioId)).thenReturn(servicioExistente);

        // Crear DTO actualizado
        ServicioRegistroDTO dto = new ServicioRegistroDTO();
        dto.setNombre("Nuevo Servicio");
        dto.setDescripcion("Descripción actualizada");

        SubcategoriaDTO subcategoria = new SubcategoriaDTO();
        subcategoria.setNombre("Nueva Subcategoría");
        subcategoria.setDescripcion("Subcategoría de ejemplo");
        subcategoria.setPrecio(200.0);
        dto.setSubcategorias(Collections.singletonList(subcategoria));

        Long idDoctor = 2L;
        InfoDoctor doctorMock = mock(InfoDoctor.class);
        dto.setIdDoctores(Collections.singletonList(idDoctor));
        when(em.find(InfoDoctor.class, idDoctor)).thenReturn(doctorMock);

        assertDoesNotThrow(() -> service.actualizarServicio(servicioId, dto));
        verify(tx).commit();
    }
}