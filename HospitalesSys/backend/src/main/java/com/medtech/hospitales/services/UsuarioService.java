package com.medtech.hospitales.services;
import com.medtech.hospitales.models.Usuario;
import jakarta.persistence.*;
import com.medtech.hospitales.utils.CorreoUtils;
import com.medtech.hospitales.models.Usuario;
import jakarta.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;

public class UsuarioService {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("HospitalesPU");

    public List<Usuario> obtenerUsuarios() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT u FROM Usuario u";
            return em.createQuery(jpql, Usuario.class).getResultList();
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

    public void actualizarUsuario(Long id, Usuario usuarioActualizado) {
        EntityManager em = emf.createEntityManager();
        try {
            Usuario usuarioExistente = em.find(Usuario.class, id);
            if (usuarioExistente != null) {
                em.getTransaction().begin();
                usuarioActualizado.setId(id);
                em.merge(usuarioActualizado);
                em.getTransaction().commit();
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

    public void crearUsuario(Usuario usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            usuario.setFechaCreacion(LocalDateTime.now());
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

    private void enviarCorreosRegistro(Usuario nuevo) {
        try {
            String mensajeBienvenida = "¡Bienvenido/a " + nuevo.getNombre() + "!\n\nTu cuenta ha sido registrada exitosamente. Pronto te estará llegando un correo para que puedas ingresar al sistema";
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
}