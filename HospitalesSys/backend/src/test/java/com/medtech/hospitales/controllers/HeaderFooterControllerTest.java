package com.medtech.hospitales.controllers;

import com.medtech.hospitales.models.HeaderFooter;
import com.medtech.hospitales.services.HeaderFooterService;
import io.javalin.http.Context;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.mockito.Mockito.*;

public class HeaderFooterControllerTest {

    @Mock private Context ctx;
    @Mock private HeaderFooterService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testObtenerHeaderFooter() throws Exception {
        HeaderFooter h1 = new HeaderFooter();
        h1.setId(1L);
        h1.setTitulo("Hospital Central");

        List<HeaderFooter> mockData = List.of(h1);
        when(service.obtenerTodos()).thenReturn(mockData);

        ctx.json(service.obtenerTodos());

        verify(service).obtenerTodos();
        verify(ctx).json(mockData);
    }
}
