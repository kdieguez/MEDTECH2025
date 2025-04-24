package com.medtech.hospitales.controllers;

import com.medtech.hospitales.models.Cargo;
import io.javalin.http.Context;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.mockito.Mockito.*;

public class CargoControllerTest {

    @Mock
    private EntityManager em;

    @Mock
    private TypedQuery<Cargo> query;

    @Mock
    private Context ctx;

    private CargoController cargoController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cargoController = new CargoController(em);
    }

    @Test
    public void testListarCargos() throws Exception {
        // Arrange
        Cargo cargo1 = new Cargo();
        cargo1.setId(1);
        cargo1.setNombre("Doctor");

        Cargo cargo2 = new Cargo();
        cargo2.setId(2);
        cargo2.setNombre("Enfermero");

        List<Cargo> cargosMock = List.of(cargo1, cargo2);

        when(em.createQuery("SELECT c FROM Cargo c", Cargo.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(cargosMock);

        // Act
        cargoController.listarCargos.handle(ctx);

        // Assert
        verify(ctx).json(cargosMock);
    }
}
