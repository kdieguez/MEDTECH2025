package com.medtech.hospitales.services;

import com.medtech.hospitales.models.Usuario;
import jakarta.persistence.*;

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

    public void crearUsuario(Usuario usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(usuario);
            em.getTransaction().commit();
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
}