package com.medtech.hospitales.services;

import com.medtech.hospitales.dtos.DoctorRegistroDTO;
import com.medtech.hospitales.dtos.EspecialidadDTO;
import com.medtech.hospitales.models.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorServiceTest {

    @Mock
    EntityManager em;

    @Mock
    EntityTransaction tx;

    @InjectMocks
    DoctorService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(em.getTransaction()).thenReturn(tx);
        service = new DoctorService(em);
    }

    @Test
    void registrarPerfilDoctor_deberiaPersistirInfoDoctorCorrectamente() {
        // Arrange
        DoctorRegistroDTO dto = new DoctorRegistroDTO();
        dto.setIdUsuario(1L);
        dto.setFotografia("foto.png");
        dto.setNumColegiado("12345");
        dto.setTelefonos(List.of("12345678", "87654321"));

        EspecialidadDTO esp = new EspecialidadDTO();
        esp.setNombreNueva("Cardiología");
        esp.setUniversidad("USAC");
        esp.setFechaGraduacion(LocalDate.now().toString());
        esp.setFotografiaTitulo("titulo.png");
        dto.setEspecialidades(Collections.singletonList(esp));

        Usuario mockUsuario = new Usuario();
        when(em.find(Usuario.class, 1L)).thenReturn(mockUsuario);

        TypedQuery<Especialidad> typedQuery = mock(TypedQuery.class);
        when(em.createQuery(anyString(), eq(Especialidad.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.emptyList());

        // Act
        assertDoesNotThrow(() -> service.registrarPerfilDoctor(dto));

        // Assert
        verify(em).persist(any(InfoDoctor.class));
        verify(tx).commit();
    }

    @Test
    void registrarPerfilDoctor_deberiaFallarSiUsuarioNoExiste() {
        // Arrange
        DoctorRegistroDTO dto = new DoctorRegistroDTO();
        dto.setIdUsuario(999L);

        when(em.find(Usuario.class, 999L)).thenReturn(null);

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.registrarPerfilDoctor(dto));
        assertEquals("Usuario no encontrado con ID: 999", ex.getMessage());
        verify(tx, never()).commit();
    }
}