package com.medtech.hospitales.services;

import com.medtech.hospitales.models.Cargo;
import com.medtech.hospitales.models.UsuarioCargo;
import com.medtech.hospitales.models.Usuario;
import com.medtech.hospitales.utils.CorreoUtils;
import com.medtech.hospitales.utils.JPAUtil;
import jakarta.mail.MessagingException;
import jakarta.persistence.*;

import com.medtech.hospitales.dtos.PacienteDTO;
import com.medtech.hospitales.dtos.UsuarioConCargo;

import java.time.LocalDate;
import java.util.List;

public class UsuarioService {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("HospitalesPU");

    public List<Usuario> obtenerUsuarios() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Usuario obtenerUsuarioPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public void crearUsuario(Usuario usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            usuario.setFechaCreacion(new java.sql.Timestamp(System.currentTimeMillis()));
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();

            System.out.println("Usuario creado: " + usuario);

            enviarCorreosRegistro(usuario);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

public void actualizarUsuario(Long id, Usuario usuarioActualizado) {
    EntityManager em = emf.createEntityManager();
    try {
        Usuario usuarioExistente = em.find(Usuario.class, id);
        if (usuarioExistente != null) {
            em.getTransaction().begin();

            System.out.println("Actualizando usuario ID: " + id);
            System.out.println("Datos recibidos: " + usuarioActualizado);

            if (usuarioActualizado.getNombre() != null)
                usuarioExistente.setNombre(usuarioActualizado.getNombre());

            if (usuarioActualizado.getApellido() != null)
                usuarioExistente.setApellido(usuarioActualizado.getApellido());

            if (usuarioActualizado.getEmail() != null)
                usuarioExistente.setEmail(usuarioActualizado.getEmail());

            if (usuarioActualizado.getUsuario() != null)
                usuarioExistente.setUsuario(usuarioActualizado.getUsuario());

            if (usuarioActualizado.getIdRol() != null)
                usuarioExistente.setIdRol(usuarioActualizado.getIdRol());

            if (usuarioActualizado.getHabilitado() != null)
                usuarioExistente.setHabilitado(usuarioActualizado.getHabilitado());

            em.merge(usuarioExistente);

            // Verifica si es un UsuarioConCargo (para rol Empleado)
            if (usuarioActualizado.getIdRol() != null && usuarioActualizado.getIdRol() == 2
                    && usuarioActualizado instanceof UsuarioConCargo usuarioConCargo) {

                // Buscar si ya existe relación con algún cargo
                UsuarioCargo usuarioCargoExistente = em.createQuery(
                        "SELECT uc FROM UsuarioCargo uc WHERE uc.usuario.id = :id", UsuarioCargo.class)
                        .setParameter("id", id)
                        .getResultStream()
                        .findFirst()
                        .orElse(null);

                // Buscar el cargo indicado
                Cargo cargo = em.find(Cargo.class, usuarioConCargo.getIdCargo());

                if (cargo != null) {
                    if (usuarioCargoExistente == null) {
                        usuarioCargoExistente = new UsuarioCargo();
                        usuarioCargoExistente.setUsuario(usuarioExistente);
                    }
                    usuarioCargoExistente.setCargo(cargo);
                    em.merge(usuarioCargoExistente);
                } else {
                    System.out.println("No se encontró el cargo con ID: " + usuarioConCargo.getIdCargo());
                }
            }

            em.getTransaction().commit();
        } else {
            System.err.println("Usuario no encontrado con ID: " + id);
        }
    } catch (Exception e) {
        System.err.println("Error al actualizar el usuario: " + e.getMessage());
        e.printStackTrace();
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    } finally {
        em.close();
    }
}

    public void eliminarUsuario(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Usuario usuario = em.find(Usuario.class, id);
            if (usuario != null) {
                em.getTransaction().begin();
                em.remove(usuario);
                em.getTransaction().commit();
            }
        } finally {
            em.close();
        }
    }

    public Usuario login(String username, String password) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.usuario = :user AND u.password = :pass", Usuario.class)
                    .setParameter("user", username)
                    .setParameter("pass", password)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

    private void enviarCorreosRegistro(Usuario nuevo) {
        try {
            String mensajeBienvenida = "¡Bienvenido/a " + nuevo.getNombre() + "!\n\nTu cuenta ha sido registrada exitosamente. Pronto te estará llegando un correo para que puedas ingresar al sistema.";
            CorreoUtils.enviarCorreo(nuevo.getEmail(), "Bienvenido a Hospital Base", mensajeBienvenida);

            EntityManager em = emf.createEntityManager();
            List<String> correosAdmins = em.createQuery(
                    "SELECT u.email FROM Usuario u WHERE u.idRol = 1 AND u.habilitado = 1", String.class
            ).getResultList();

            String mensajeAdmin = "Se ha registrado un nuevo usuario:\n\n" +
                    "Nombre: " + nuevo.getNombre() + " " + nuevo.getApellido() + "\n" +
                    "Correo: " + nuevo.getEmail() + "\n" +
                    "Usuario: " + nuevo.getUsuario() + "\n" +
                    "Fecha de registro: " + nuevo.getFechaCreacion();

            for (String correoAdmin : correosAdmins) {
                CorreoUtils.enviarCorreo(correoAdmin, "Nuevo usuario registrado", mensajeAdmin);
            }

            em.close();
        } catch (MessagingException e) {
            System.err.println("Error al enviar correos: " + e.getMessage());
        }
    }

    public List<Usuario> filtrarUsuarios(String correo, Integer idRol, LocalDate fechaInicio, LocalDate fechaFin) {
        EntityManager em = emf.createEntityManager();
        try {
            StringBuilder query = new StringBuilder("SELECT u FROM Usuario u WHERE 1=1");

            if (correo != null && !correo.isBlank()) {
                query.append(" AND LOWER(u.email) LIKE LOWER(:correo)");
            }
            if (idRol != null) {
                query.append(" AND u.idRol = :rol");
            }
            if (fechaInicio != null) {
                query.append(" AND u.fechaCreacion >= :inicio");
            }
            if (fechaFin != null) {
                query.append(" AND u.fechaCreacion <= :fin");
            }

            TypedQuery<Usuario> q = em.createQuery(query.toString(), Usuario.class);

            if (correo != null && !correo.isBlank()) {
                q.setParameter("correo", "%" + correo + "%");
            }
            if (idRol != null) {
                q.setParameter("rol", idRol);
            }
            if (fechaInicio != null) {
                q.setParameter("inicio", fechaInicio.atStartOfDay());
            }
            if (fechaFin != null) {
                q.setParameter("fin", fechaFin.atTime(23, 59, 59));
            }

            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Usuario> obtenerUsuariosPaginados(int pagina) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u ORDER BY u.fechaCreacion DESC", Usuario.class)
                    .setFirstResult((pagina - 1) * 10)
                    .setMaxResults(10)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void activarUsuario(Long id) throws MessagingException {
        EntityManager em = emf.createEntityManager();
        try {
            Usuario u = em.find(Usuario.class, id);
            if (u != null && u.getHabilitado() == 0) {
                em.getTransaction().begin();
                u.setHabilitado(1);
                em.merge(u);
                em.getTransaction().commit();

                String msg = "¡Hola " + u.getNombre() + "!\n\nTu cuenta ha sido activada y ya puedes ingresar al sistema.";
                CorreoUtils.enviarCorreo(u.getEmail(), "Tu cuenta ha sido activada", msg);
            }
        } finally {
            em.close();
        }
    }

    public void desactivarUsuario(Long id) throws MessagingException {
        EntityManager em = emf.createEntityManager();
        try {
            Usuario u = em.find(Usuario.class, id);
            if (u != null && u.getHabilitado() == 1) {
                em.getTransaction().begin();
                u.setHabilitado(0);
                em.merge(u);
                em.getTransaction().commit();

                String msg = "Hola " + u.getNombre() + ",\n\nTu cuenta ha sido desactivada por el administrador.";
                CorreoUtils.enviarCorreo(u.getEmail(), "Cuenta desactivada", msg);
            }
        } finally {
            em.close();
        }
    }

    public List<PacienteDTO> obtenerPacientesConPerfilDTO() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT new com.medtech.hospitales.dtos.PacienteDTO(" +
                "p.id, u.nombre, u.apellido, p.documentoIdentificacion) " +
                "FROM PerfilPaciente p JOIN p.usuario u WHERE u.habilitado = 1",
                PacienteDTO.class
            ).getResultList();
        } finally {
            em.close();
        }
    }
    
}