package com.medtech.hospitales.services;

import com.medtech.hospitales.models.Pagina;
import com.medtech.hospitales.models.SeccionPagina;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class PaginaService {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("HospitalesPU");

    // Obtener todas las páginas
    public List<Pagina> obtenerTodasLasPaginas() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pagina p", Pagina.class).getResultList();
        } finally {
            em.close();
        }
    }

    // Obtener todas las secciones de una página específica
    public List<SeccionPagina> obtenerSeccionesDePagina(Long idPagina) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                "SELECT s FROM SeccionPagina s WHERE s.pagina.id = :idPagina ORDER BY s.orden ASC",
                SeccionPagina.class
            )
            .setParameter("idPagina", idPagina)
            .getResultList();
        } finally {
            em.close();
        }
    }

    // Actualizar una sección existente
    public void actualizarSeccion(Long idSeccion, String nuevoTitulo, String nuevoContenido, String nuevaImagenUrl) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            SeccionPagina seccion = em.find(SeccionPagina.class, idSeccion);
            if (seccion != null) {
                seccion.setTitulo(nuevoTitulo);
                seccion.setContenido(nuevoContenido);
                seccion.setImagenUrl(nuevaImagenUrl);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // Agregar una nueva sección a una página
    public void agregarSeccion(Long idPagina, String titulo, String contenido, String imagenUrl, Integer orden) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Pagina pagina = em.find(Pagina.class, idPagina);
            if (pagina != null) {
                SeccionPagina nuevaSeccion = new SeccionPagina();
                nuevaSeccion.setPagina(pagina);
                nuevaSeccion.setTitulo(titulo);
                nuevaSeccion.setContenido(contenido);
                nuevaSeccion.setImagenUrl(imagenUrl);
                nuevaSeccion.setOrden(orden);

                em.persist(nuevaSeccion);
                em.flush(); 
                em.refresh(nuevaSeccion);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    // Eliminar una sección
    public void eliminarSeccion(Long idSeccion) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            SeccionPagina seccion = em.find(SeccionPagina.class, idSeccion);
            if (seccion != null) {
                em.remove(seccion);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}