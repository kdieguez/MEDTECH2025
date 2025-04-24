package com.medtech.hospitales.services;

import com.medtech.hospitales.dao.HeaderFooterDAO;
import com.medtech.hospitales.models.HeaderFooter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class HeaderFooterServiceTest {

    @Mock
    private HeaderFooterDAO headerFooterDAO;

    @InjectMocks
    private HeaderFooterService headerFooterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void obtenerTodos_deberiaRetornarListaDeHeaderFooter() {
        // Arrange
        HeaderFooter h1 = new HeaderFooter();
        HeaderFooter h2 = new HeaderFooter();
        List<HeaderFooter> headersFooters = Arrays.asList(h1, h2);
        when(headerFooterDAO.obtenerTodos()).thenReturn(headersFooters);

        // Act
        List<HeaderFooter> resultado = headerFooterService.obtenerTodos();

        // Assert
        assertEquals(2, resultado.size());
    }
}