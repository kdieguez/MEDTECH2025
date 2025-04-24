package com.medtech.hospitales.controllers;

import com.medtech.hospitales.dtos.NuevaEspecialidadDTO;
import com.medtech.hospitales.models.Especialidad;
import io.javalin.http.Context;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EspecialidadControllerTest {

    @Mock private Context ctx;
    @Mock private EntityManager em;
    @Mock private TypedQuery<Especialidad> query;
    @Mock private EntityTransaction tx;

    private EspecialidadController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new EspecialidadController(em);
    }

    @Test
    public void testObtenerEspecialidades() throws Exception {
        Especialidad e1 = new Especialidad();
        e1.setNombre("Cardiología");
        Especialidad e2 = new Especialidad();
        e2.setNombre("Neurología");

        when(em.createQuery("SELECT e FROM Especialidad e", Especialidad.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(e1, e2));

        controller.obtenerEspecialidades.handle(ctx);

        verify(ctx).json(List.of(e1, e2));
    }

    @Test
    public void testAgregarEspecialidad() throws Exception {
        NuevaEspecialidadDTO dto = new NuevaEspecialidadDTO();
        dto.setNombre("Pediatría");

        when(ctx.bodyAsClass(NuevaEspecialidadDTO.class)).thenReturn(dto);
        when(em.getTransaction()).thenReturn(tx);
        when(ctx.status(anyInt())).thenReturn(ctx);
        when(ctx.result(any(String.class))).thenReturn(ctx);

        controller.agregarEspecialidad.handle(ctx);

        verify(em).persist(any(Especialidad.class));
        verify(tx).begin();
        verify(tx).commit();
        verify(ctx).status(201);
        verify(ctx).result("Especialidad creada exitosamente.");
    }
}
