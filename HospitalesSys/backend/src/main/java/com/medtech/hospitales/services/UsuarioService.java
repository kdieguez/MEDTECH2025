package com.medtech.hospitales.services;

import com.medtech.hospitales.dtos.PacienteDTO;
import com.medtech.hospitales.dtos.UsuarioConCargo;
import com.medtech.hospitales.models.Cargo;
import com.medtech.hospitales.models.Usuario;
import com.medtech.hospitales.models.UsuarioCargo;
import com.medtech.hospitales.utils.CorreoUtils;
import com.medtech.hospitales.utils.JPAUtil;
import jakarta.mail.MessagingException;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio que maneja las operaciones relacionadas con los usuarios
 * en el sistema hospitalario, incluyendo creación, edición, activación
 * y desactivación de usuarios, así como filtrado y paginación.
 */
public class UsuarioService {

    /**
     * EntityManagerFactory para conexión a la base de datos.
     */
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("HospitalesPU");

    /**
     * Obtiene todos los usuarios del sistema.
     *
     * @return Lista de usuarios.
     */
    public List<Usuario> obtenerUsuarios() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id ID del usuario.
     * @return Usuario encontrado, o null si no existe.
     */
    public Usuario obtenerUsuarioPorId(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Crea un nuevo usuario en el sistema y envía correos de notificación.
     *
     * @param usuario Objeto usuario a crear.
     */
    public void crearUsuario(Usuario usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            usuario.setFechaCreacion(new java.sql.Timestamp(System.currentTimeMillis()));
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
            enviarCorreosRegistro(usuario);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Actualiza la información de un usuario.
     *
     * @param id ID del usuario a actualizar.
     * @param usuarioActualizado Datos actualizados del usuario.
     */
    public void actualizarUsuario(Long id, Usuario usuarioActualizado) {
        EntityManager em = emf.createEntityManager();
        try {
            Usuario usuarioExistente = em.find(Usuario.class, id);
            if (usuarioExistente != null) {
                em.getTransaction().begin();

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

                if (usuarioActualizado.getIdRol() != null && usuarioActualizado.getIdRol() == 2
                        && usuarioActualizado instanceof UsuarioConCargo usuarioConCargo) {
                    UsuarioCargo usuarioCargoExistente = em.createQuery(
                            "SELECT uc FROM UsuarioCargo uc WHERE uc.usuario.id = :id", UsuarioCargo.class)
                            .setParameter("id", id)
                            .getResultStream()
                            .findFirst()
                            .orElse(null);

                    Cargo cargo = em.find(Cargo.class, usuarioConCargo.getIdCargo());
                    if (cargo != null) {
                        if (usuarioCargoExistente == null) {
                            usuarioCargoExistente = new UsuarioCargo();
                            usuarioCargoExistente.setUsuario(usuarioExistente);
                        }
                        usuarioCargoExistente.setCargo(cargo);
                        em.merge(usuarioCargoExistente);
                    }
                }

                em.getTransaction().commit();
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Elimina un usuario del sistema por su ID.
     *
     * @param id ID del usuario a eliminar.
     */
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

    /**
     * Realiza el login de un usuario usando su usuario y contraseña.
     *
     * @param username Nombre de usuario.
     * @param password Contraseña.
     * @return Usuario si las credenciales son correctas, null si no.
     */
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

    /**
     * Envía correos de notificación al nuevo usuario y a los administradores.
     *
     * @param nuevo Nuevo usuario registrado.
     */
    private void enviarCorreosRegistro(Usuario nuevo) {
        try {
            String mensajeBienvenida = "¡Bienvenido/a " + nuevo.getNombre() + "!\n\nTu cuenta ha sido registrada exitosamente.";
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

    /**
     * Filtra usuarios basados en correo, rol y rango de fechas de creación.
     *
     * @param correo Filtro por correo (opcional).
     * @param idRol Filtro por ID de rol (opcional).
     * @param fechaInicio Fecha de inicio (opcional).
     * @param fechaFin Fecha de fin (opcional).
     * @return Lista filtrada de usuarios.
     */
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

    /**
     * Obtiene usuarios paginados de 10 en 10.
     *
     * @param pagina Número de página.
     * @return Lista de usuarios en esa página.
     */
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

    /**
     * Activa un usuario y envía correo de notificación.
     *
     * @param id ID del usuario.
     * @throws MessagingException en caso de error de correo.
     */
    public void activarUsuario(Long id) throws MessagingException {
        EntityManager em = emf.createEntityManager();
        try {
            Usuario u = em.find(Usuario.class, id);
            if (u != null && u.getHabilitado() == 0) {
                em.getTransaction().begin();
                u.setHabilitado(1);
                em.merge(u);
                em.getTransaction().commit();

                // Buscar nombre del rol desde la tabla de roles
                String nombreRol = em.createQuery(
                    "SELECT r.nombreRol FROM Rol r WHERE r.id = :idRol", String.class)
                    .setParameter("idRol", u.getIdRol())
                    .getSingleResult();

                if ("paciente".equalsIgnoreCase(nombreRol)) {
                    CorreoUtils.enviarCorreoBienvenidaPacienteConContrasena(
                        u.getEmail(),
                        u.getNombre() + " " + u.getApellido(),
                        u.getPassword() // asegúrate que aún no esté hasheado
                    );
                } else {
                    String msg = "¡Hola " + u.getNombre() + "!\n\nTu cuenta ha sido activada y ya puedes ingresar al sistema.";
                    CorreoUtils.enviarCorreo(u.getEmail(), "Tu cuenta ha sido activada", msg);
                }
            }
        } finally {
            em.close();
        }
    }

    /**
     * Desactiva un usuario y envía correo de notificación.
     *
     * @param id ID del usuario.
     * @throws MessagingException en caso de error de correo.
     */
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

    /**
     * Obtiene la lista de pacientes habilitados en formato DTO.
     *
     * @return Lista de pacientes con perfil.
     */
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
