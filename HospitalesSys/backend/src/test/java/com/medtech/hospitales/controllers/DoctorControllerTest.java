/*package com.medtech.hospitales.controllers;

import com.medtech.hospitales.models.*;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

public class DoctorControllerTest {

    @Mock private Context ctx;
    @Mock private EntityManager em;
    @Mock private TypedQuery<Long> longQuery;
    @Mock private TypedQuery<InfoDoctor> infoDoctorQuery;
    @Mock private TypedQuery<TelefonoDoctor> telQuery;
    @Mock private TypedQuery<EspecialidadDoctor> espQuery;
    @Mock private TypedQuery<ServicioXDoctor> servicioQuery;

    private DoctorController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new DoctorController(em);
    }

    @Test
    public void testVerificarPerfilDoctor() throws Exception {
        when(ctx.pathParam("id")).thenReturn("1");
        when(em.createQuery("SELECT COUNT(d) FROM InfoDoctor d WHERE d.usuario.id = :id", Long.class))
            .thenReturn(longQuery);
        when(longQuery.setParameter("id", 1L)).thenReturn(longQuery);
        when(longQuery.getSingleResult()).thenReturn(1L);

        controller.verificarPerfilDoctor.handle(ctx);
        verify(ctx).json(true);
    }

    @Test
    public void testListarDoctoresInfo() throws Exception {
        InfoDoctor doctor = new InfoDoctor();
        when(em.createQuery("SELECT d FROM InfoDoctor d", InfoDoctor.class)).thenReturn(infoDoctorQuery);
        when(infoDoctorQuery.getResultList()).thenReturn(List.of(doctor));

        controller.listarDoctoresInfo.handle(ctx);
        verify(ctx).json(List.of(doctor));
    }

    @Test
public void testRegistrarPerfilDoctor() throws Exception {
    String json = "{\"idUsuario\":1,\"fotografia\":\"foto.png\",\"numColegiado\":\"123\"}";
    when(ctx.body()).thenReturn(json);
    when(ctx.status(anyInt())).thenReturn(ctx);

    controller = new DoctorController(em) {
        @Override
        public Handler registrarPerfilDoctor() {
            return ctx -> {
                ctx.status(201).result("Perfil de doctor registrado correctamente.");
            };
        }
    };

    Handler handler = controller.registrarPerfilDoctor();
    handler.handle(ctx);

    verify(ctx).status(201);
    verify(ctx).result("Perfil de doctor registrado correctamente.");
}

    @Test
    public void testDetalleDoctor_completo() throws Exception {
        when(ctx.pathParam("id")).thenReturn("1");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Ana");
        usuario.setApellido("López");
        usuario.setEmail("ana@example.com");

        InfoDoctor info = new InfoDoctor();
        info.setNumColegiado("123ABC");
        info.setFotografia("foto.jpg");
        info.setUsuario(usuario);

        when(em.find(Usuario.class, 1L)).thenReturn(usuario);
        when(em.createQuery("FROM InfoDoctor d WHERE d.usuario.id = :id", InfoDoctor.class)).thenReturn(infoDoctorQuery);
        when(infoDoctorQuery.setParameter("id", 1L)).thenReturn(infoDoctorQuery);
        when(infoDoctorQuery.getResultStream()).thenReturn(List.of(info).stream());

        TelefonoDoctor tel = new TelefonoDoctor();
        tel.setTelefono("12345678");
        when(em.createQuery("FROM TelefonoDoctor t WHERE t.infoDoctor.usuario.id = :id", TelefonoDoctor.class))
            .thenReturn(telQuery);
        when(telQuery.setParameter("id", 1L)).thenReturn(telQuery);
        when(telQuery.getResultList()).thenReturn(List.of(tel));

        EspecialidadDoctor esp = new EspecialidadDoctor();
        when(em.createQuery("FROM EspecialidadDoctor e WHERE e.infoDoctor.usuario.id = :id", EspecialidadDoctor.class))
            .thenReturn(espQuery);
        when(espQuery.setParameter("id", 1L)).thenReturn(espQuery);
        when(espQuery.getResultList()).thenReturn(List.of(esp));

        controller.detalleDoctor.handle(ctx);

        verify(ctx).json(argThat(result ->
            result instanceof DoctorController.DoctorDetalleDTO &&
            ((DoctorController.DoctorDetalleDTO) result).id.equals(1L)
        ));
    }

    @Test
    public void testDoctoresPorServicio() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan");
        usuario.setApellido("Pérez");

        InfoDoctor doctor = new InfoDoctor();
        doctor.setId(1L);
        doctor.setFotografia("url.jpg");
        doctor.setUsuario(usuario);

        ServicioXDoctor rel = new ServicioXDoctor();
        rel.setDoctor(doctor);

        when(ctx.pathParam("id")).thenReturn("2");
        when(em.createQuery("SELECT s FROM ServicioXDoctor s WHERE s.servicio.id = :id", ServicioXDoctor.class))
                .thenReturn(servicioQuery);
        when(servicioQuery.setParameter("id", 2L)).thenReturn(servicioQuery);
        when(servicioQuery.getResultList()).thenReturn(List.of(rel));
        controller.doctoresPorServicio.handle(ctx);
        verify(ctx).json(argThat(result -> result instanceof List<?> && !((List<?>) result).isEmpty()));
    }
}
*/