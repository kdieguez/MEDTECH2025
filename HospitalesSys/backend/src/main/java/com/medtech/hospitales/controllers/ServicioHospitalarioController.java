package com.medtech.hospitales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medtech.hospitales.dtos.ServicioDTO;
import com.medtech.hospitales.dtos.ServicioRegistroDTO;
import com.medtech.hospitales.dtos.ServicioSimpleDTO;
import com.medtech.hospitales.dtos.SubcategoriaDTO;
import com.medtech.hospitales.models.ServicioHospitalario;
import com.medtech.hospitales.models.ServicioXDoctor;
import com.medtech.hospitales.models.SubcategoriaServicio;
import com.medtech.hospitales.services.ServicioHospitalarioService;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManager;

import java.util.*;
import java.util.stream.Collectors;

public class ServicioHospitalarioController {

    private ServicioHospitalarioService servicioService = null;
    private final ObjectMapper mapper = new ObjectMapper();
    private EntityManager entityManager = null;

    public ServicioHospitalarioController(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.servicioService = new ServicioHospitalarioService(entityManager);
    }

    public Handler registrarServicio = ctx -> {
        try {
            ServicioRegistroDTO dto = mapper.readValue(ctx.body(), ServicioRegistroDTO.class);
            servicioService.registrarServicio(dto);
            ctx.status(201).result("Servicio hospitalario registrado exitosamente.");
        } catch (Exception e) {
            ctx.status(400).result("Error al registrar servicio: " + e.getMessage());
        }
    };

    public Handler listarServicios = ctx -> {
        List<ServicioHospitalario> servicios = servicioService.listarServicios();
        ctx.json(servicios);
    };

    public Handler detalleServicio = ctx -> {
        Long id = Long.parseLong(ctx.pathParam("id"));

        ServicioHospitalario servicio = servicioService.buscarServicioPorId(id);

        if (servicio == null) {
            ctx.status(404).result("Servicio no encontrado con ID: " + id);
            return;
        }

        ctx.json(servicio);
    };

    public Handler actualizarServicio = ctx -> {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            ServicioRegistroDTO dto = mapper.readValue(ctx.body(), ServicioRegistroDTO.class);
            servicioService.actualizarServicio(id, dto);
            ctx.status(200).result("Servicio actualizado exitosamente.");
        } catch (Exception e) {
            ctx.status(400).result("Error al actualizar servicio: " + e.getMessage());
        }
    };

    public Handler serviciosPorDoctor = ctx -> {
        Long idDoctor = Long.parseLong(ctx.pathParam("id"));
        List<ServicioXDoctor> relaciones = entityManager.createQuery(
            "SELECT s FROM ServicioXDoctor s WHERE s.doctor.id = :id",
            ServicioXDoctor.class
        )
        .setParameter("id", idDoctor)
        .getResultList();

        List<ServicioSimpleDTO> servicios = relaciones.stream()
            .map(sxd -> new ServicioSimpleDTO(
                sxd.getServicio().getId(),
                sxd.getServicio().getNombre()
            ))
            .collect(Collectors.toList());

        ctx.json(servicios);
    };

    public Handler subcategoriasPorServicio = ctx -> {
        Long idServicio = Long.parseLong(ctx.pathParam("id"));

        List<SubcategoriaServicio> subcategorias = entityManager.createQuery(
            "SELECT s FROM SubcategoriaServicio s WHERE s.servicio.id = :id",
            SubcategoriaServicio.class
        )
        .setParameter("id", idServicio)
        .getResultList();

        List<SubcategoriaDTO> dtos = subcategorias.stream()
            .map(s -> new SubcategoriaDTO(
                s.getId(),
                s.getNombre(),
                s.getDescripcion(),
                s.getPrecio()
            ))
            .collect(Collectors.toList());

        ctx.json(dtos);
    };

    public Handler serviciosParaSeguros = ctx -> {
        List<Object[]> resultados = entityManager.createNativeQuery(
            "SELECT sh.ID_SERVICIO, sh.NOMBRE_SERVICIO, sh.DESCRIPCION, " +
            "sc.ID_SUBCATEGORIA, sc.NOMBRE_SUBCATEGORIA, sc.DESCRIPCION, sc.PRECIO, " +
            "LISTAGG(sd.ID_INFO_DOCTOR, ',') WITHIN GROUP (ORDER BY sd.ID_INFO_DOCTOR) AS IDS_DOCTORES " +
            "FROM SERVICIOS_HOSPITALARIOS sh " +
            "LEFT JOIN SUBCATEGORIAS_SERVICIO sc ON sh.ID_SERVICIO = sc.ID_SERVICIO " +
            "LEFT JOIN SERVICIO_X_DOCTOR sd ON sh.ID_SERVICIO = sd.ID_SERVICIO " +
            "GROUP BY sh.ID_SERVICIO, sh.NOMBRE_SERVICIO, sh.DESCRIPCION, " +
            "sc.ID_SUBCATEGORIA, sc.NOMBRE_SUBCATEGORIA, sc.DESCRIPCION, sc.PRECIO " +
            "ORDER BY sh.ID_SERVICIO, sc.ID_SUBCATEGORIA"
        ).getResultList();

        List<ServicioDTO> servicios = new ArrayList<>();

        for (Object[] fila : resultados) {
            ServicioDTO dto = new ServicioDTO();
            dto.setId_servicio(((Number) fila[0]).longValue());
            dto.setNombre_servicio((String) fila[1]);
            dto.setDescripcion_servicio((String) fila[2]);
            dto.setId_subcategoria(((Number) fila[3]).longValue());
            dto.setNombre_subcategoria((String) fila[4]);
            dto.setDescripcion_subcategoria((String) fila[5]);
            dto.setPrecio(((Number) fila[6]).doubleValue());

            String ids = (String) fila[7];
            if (ids != null && !ids.isEmpty()) {
                List<Long> doctores = Arrays.stream(ids.split(","))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
                dto.setId_info_doctor(doctores);
            } else {
                dto.setId_info_doctor(new ArrayList<>());
            }

            servicios.add(dto);
        }

        ctx.json(servicios);
    };
}
