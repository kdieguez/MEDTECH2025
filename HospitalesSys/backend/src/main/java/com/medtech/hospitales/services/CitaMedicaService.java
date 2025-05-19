package com.medtech.hospitales.services;

import com.medtech.hospitales.dtos.CitaDTO;
import com.medtech.hospitales.dtos.CitaRegistroDTO;
import com.medtech.hospitales.dtos.MedicamentoRecetadoDTO;
import com.medtech.hospitales.dtos.CitaExternaDTO; 
import com.medtech.hospitales.models.*;
import com.medtech.hospitales.utils.JPAUtil;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Map;
import java.sql.Timestamp;



/**
 * Servicio que maneja las operaciones relacionadas con citas médicas.
 */
public class CitaMedicaService {

    /**
     * Registra una nueva cita médica en el sistema.
     *
     * @param dto DTO con los datos de la cita a registrar.
     */
    public void registrarCita(CitaRegistroDTO dto) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            if (existeCitaParaDoctorEnHorario(dto.getIdDoctor(), dto.getFechaHora())) {
                throw new RuntimeException("Ya existe una cita para ese doctor en esa fecha y hora");
            }

            CitaMedica cita = new CitaMedica();
            cita.setPaciente(em.find(PerfilPaciente.class, dto.getIdPaciente()));
            cita.setInfoDoctor(em.find(InfoDoctor.class, dto.getIdDoctor()));
            cita.setSubcategoria(em.find(SubcategoriaServicio.class, dto.getIdSubcategoria()));
            cita.setFechaHora(dto.getFechaHora());

            em.persist(cita);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Error al registrar la cita médica: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * Verifica si ya existe una cita registrada para un doctor en una fecha y hora específicas.
     *
     * @param idDoctor ID del doctor
     * @param fechaHora Fecha y hora de la cita
     * @return true si existe una cita para ese horario, false si está disponible
     */
    public boolean existeCitaParaDoctorEnHorario(Long idDoctor, LocalDateTime fechaHora) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long total = em.createQuery("""
                    SELECT COUNT(c) FROM CitaMedica c
                    WHERE c.infoDoctor.id = :idDoctor AND c.fechaHora = :fechaHora
                """, Long.class)
                .setParameter("idDoctor", idDoctor)
                .setParameter("fechaHora", fechaHora)
                .getSingleResult();
            return total > 0;
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene todas las horas disponibles de un doctor en una fecha específica.
     *
     * @param idDoctor ID del doctor
     * @param fecha Fecha en la que se quiere agendar la cita
     * @return Lista de horas disponibles como String ("08:00", "08:30", etc.)
     */
    public List<String> obtenerHorasDisponiblesParaDoctor(Long idDoctor, LocalDate fecha) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            LocalDateTime inicioDia = fecha.atStartOfDay();
            LocalDateTime finDia = fecha.atTime(23, 59, 59);

            List<LocalDateTime> citasOcupadas = em.createQuery("""
                    SELECT c.fechaHora FROM CitaMedica c
                    WHERE c.infoDoctor.id = :idDoctor
                      AND c.fechaHora BETWEEN :inicio AND :fin
                """, LocalDateTime.class)
                .setParameter("idDoctor", idDoctor)
                .setParameter("inicio", inicioDia)
                .setParameter("fin", finDia)
                .getResultList();

            List<String> todasLasHoras = new ArrayList<>();
            for (int h = 8; h <= 16; h++) {
                todasLasHoras.add(String.format("%02d:00", h));
                todasLasHoras.add(String.format("%02d:30", h));
            }

            Set<String> horasOcupadas = citasOcupadas.stream()
                    .map(h -> h.toLocalTime().withSecond(0).toString().substring(0, 5))
                    .collect(Collectors.toSet());

            return todasLasHoras.stream()
                    .filter(hora -> !horasOcupadas.contains(hora))
                    .collect(Collectors.toList());
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene todas las citas registradas en el sistema.
     *
     * @return Lista de CitaDTO con toda la información necesaria.
     */
    public List<CitaDTO> obtenerTodasLasCitas() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("""
                    SELECT new com.medtech.hospitales.dtos.CitaDTO(
                        c.id,
                        CONCAT(pu.nombre, ' ', pu.apellido),
                        p.documentoIdentificacion,
                        CONCAT(du.nombre, ' ', du.apellido),
                        s.nombre,
                        sc.nombre,
                        c.fechaHora
                    )
                    FROM CitaMedica c
                    JOIN PerfilPaciente p ON c.paciente.id = p.id
                    JOIN Usuario pu ON pu.id = p.usuario.id
                    JOIN InfoDoctor d ON c.infoDoctor.id = d.id
                    JOIN Usuario du ON du.id = d.usuario.id
                    JOIN SubcategoriaServicio sc ON c.subcategoria.id = sc.id
                    JOIN ServicioHospitalario s ON sc.servicio.id = s.id
                """, CitaDTO.class)
                .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene todas las citas asociadas a un doctor específico.
     *
     * @param idUsuario ID del usuario del doctor
     * @return Lista de citas de ese doctor
     */
    public List<CitaDTO> obtenerCitasPorDoctor(Long idUsuario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Long> idsDoctor = em.createQuery("""
                    SELECT d.id FROM InfoDoctor d
                    WHERE d.usuario.id = :idUsuario
                """, Long.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();

            if (idsDoctor.isEmpty()) {
                return new ArrayList<>();
            }

            Long idDoctor = idsDoctor.get(0);

            return em.createQuery("""
                    SELECT new com.medtech.hospitales.dtos.CitaDTO(
                        c.id,
                        CONCAT(pu.nombre, ' ', pu.apellido),
                        p.documentoIdentificacion,
                        CONCAT(du.nombre, ' ', du.apellido),
                        s.nombre,
                        sc.nombre,
                        c.fechaHora
                    )
                    FROM CitaMedica c
                    JOIN PerfilPaciente p ON c.paciente.id = p.id
                    JOIN Usuario pu ON pu.id = p.usuario.id
                    JOIN InfoDoctor d ON c.infoDoctor.id = d.id
                    JOIN Usuario du ON du.id = d.usuario.id
                    JOIN SubcategoriaServicio sc ON c.subcategoria.id = sc.id
                    JOIN ServicioHospitalario s ON sc.servicio.id = s.id
                    WHERE c.infoDoctor.id = :idDoctor
                """, CitaDTO.class)
                .setParameter("idDoctor", idDoctor)
                .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Guarda la lista de medicamentos recetados asociados a una cita médica.
     *
     * @param idCita ID de la cita
     * @param medicamentos Lista de medicamentos a guardar
     */
    public void guardarMedicamentosRecetados(Long idReceta, List<MedicamentoRecetadoDTO> medicamentos) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
    
            RecetaMedica receta = em.find(RecetaMedica.class, idReceta);
            if (receta == null) {
                throw new RuntimeException("No se encontró la receta médica con ID: " + idReceta);
            }
    
            for (MedicamentoRecetadoDTO dto : medicamentos) {
                Medicamento medicamento = em.find(Medicamento.class, dto.getIdMedicamento());
                if (medicamento == null) {
                    throw new RuntimeException("No se encontró el medicamento con ID: " + dto.getIdMedicamento());
                }
    
                MedicamentoRecetado nuevo = new MedicamentoRecetado();
                nuevo.setReceta(receta); // este es el nuevo campo mapeado
                nuevo.setMedicamento(medicamento);
                nuevo.setDosis(dto.getDosis());
                nuevo.setFrecuencia(dto.getFrecuencia());
                nuevo.setDuracion(dto.getDuracion());
    
                em.persist(nuevo);
            }
    
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Error al guardar medicamentos recetados: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
    
    public Long registrarCitaExterna(CitaExternaDTO dto) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
        em.getTransaction().begin();

        LocalDateTime fechaHora = LocalDateTime.parse(dto.getFechaHora());

        // Buscar paciente por código de afiliado
        PerfilPaciente paciente = em.createQuery("""
            SELECT p FROM PerfilPaciente p WHERE p.numeroAfiliacion = :codigo
        """, PerfilPaciente.class)
        .setParameter("codigo", dto.getIdAfiliado())
        .setMaxResults(1)
        .getResultStream()
        .findFirst()
        .orElse(null);

        if (paciente == null) {
            // Obtener datos del backend de seguros
            String urlSeguros = "http://localhost:8000/usuarios/paciente/por-afiliado/" + dto.getIdAfiliado();
            var response = new java.net.URL(urlSeguros).openConnection();
            response.setRequestProperty("Accept", "application/json");
            java.io.InputStream is = response.getInputStream();
            java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";

            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            java.util.Map<String, String> data = mapper.readValue(result, Map.class);

            String contrasena = generarContrasenaSegura(10);

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNombre(data.get("nombre"));
            nuevoUsuario.setApellido(data.get("apellido"));
            nuevoUsuario.setCorreo(data.get("correo"));
            nuevoUsuario.setUsuario(data.get("correo"));
            nuevoUsuario.setContrasena(contrasena);
            nuevoUsuario.setFechaCreacion(Timestamp.valueOf(LocalDateTime.now()));
            nuevoUsuario.setHabilitado(1);
            nuevoUsuario.setEstadoCuenta(1);
            nuevoUsuario.setRol(em.find(Rol.class, 3)); // paciente

            em.persist(nuevoUsuario);

            paciente = new PerfilPaciente();
            paciente.setUsuario(nuevoUsuario);
            paciente.setDocumentoIdentificacion(data.get("dpi"));
            paciente.setCodigoAfiliado(data.get("num_afiliacion"));
            paciente.setNumeroCarnet(data.get("num_carnet"));
            paciente.setFotografia(data.get("fotografia"));

            if (data.containsKey("fecha_nacimiento")) {
                paciente.setFechaNacimiento(LocalDate.parse(data.get("fecha_nacimiento")));
            }

            em.persist(paciente);
        }

        //Buscar la subcategoría por nombre
        Object[] resultado = em.createQuery("""
            SELECT s.id, s.servicio.id FROM SubcategoriaServicio s
            WHERE s.nombre = :nombre
        """, Object[].class)
        .setParameter("nombre", dto.getNombreSubcategoria())
        .setMaxResults(1)
        .getSingleResult();

        Long idSub = (Long) resultado[0];
        Long idServicio = (Long) resultado[1];

        //Buscar InfoDoctor desde la tabla de unión SERVICIO_X_DOCTOR
        List<Long> idsDoctores = em.createQuery("""
            SELECT sx.doctor.id FROM ServicioXDoctor sx
            WHERE sx.servicio.id = :idServicio
        """, Long.class)
        .setParameter("idServicio", idServicio)
        .getResultList();

        //Filtrar doctores disponibles
        InfoDoctor doctorDisponible = null;
        for (Long idDoctor : idsDoctores) {
            Long count = em.createQuery("""
                SELECT COUNT(c) FROM CitaMedica c
                WHERE c.infoDoctor.id = :idDoctor AND c.fechaHora = :fecha
            """, Long.class)
            .setParameter("idDoctor", idDoctor)
            .setParameter("fecha", fechaHora)
            .getSingleResult();

            if (count == 0) {
                doctorDisponible = em.find(InfoDoctor.class, idDoctor);
                break;
            }
        }

        if (doctorDisponible == null) {
            throw new RuntimeException("No hay doctores disponibles para ese servicio y horario");
        }

        //Crear y guardar la cita
        CitaMedica cita = new CitaMedica();
        cita.setPaciente(paciente);
        cita.setInfoDoctor(doctorDisponible);
        cita.setSubcategoria(em.find(SubcategoriaServicio.class, idSub));
        cita.setFechaHora(fechaHora);

        em.persist(cita);
        em.getTransaction().commit();
        return cita.getId();

    } catch (Exception e) {
        if (em.getTransaction().isActive()) em.getTransaction().rollback();
        throw new RuntimeException("Error al registrar cita externa: " + e.getMessage(), e);
    } finally {
        em.close();
    }
}


private String generarContrasenaSegura(int longitud) {
    String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    java.util.Random random = new java.util.Random();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < longitud; i++) {
        sb.append(caracteres.charAt(random.nextInt(caracteres.length())));
    }
    return sb.toString();
}

public void actualizarCitaExterna(Long idCita, String nuevaFechaHora, String nuevaSubcategoria) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
        em.getTransaction().begin();

        CitaMedica cita = em.find(CitaMedica.class, idCita);
        if (cita == null) {
            throw new RuntimeException("La cita no existe");
        }

        cita.setFechaHora(LocalDateTime.parse(nuevaFechaHora));

        SubcategoriaServicio nuevaSub = em.createQuery("""
            SELECT s FROM SubcategoriaServicio s
            WHERE s.nombre = :nombre
        """, SubcategoriaServicio.class)
        .setParameter("nombre", nuevaSubcategoria)
        .setMaxResults(1)
        .getSingleResult();

        cita.setSubcategoria(nuevaSub);

        em.getTransaction().commit();
    } catch (Exception e) {
        if (em.getTransaction().isActive()) em.getTransaction().rollback();
        throw new RuntimeException("Error al actualizar la cita: " + e.getMessage(), e);
    } finally {
        em.close();
    }
}



}
