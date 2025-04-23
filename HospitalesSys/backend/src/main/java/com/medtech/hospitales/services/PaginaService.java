package com.medtech.hospitales.services;

import com.medtech.hospitales.models.Pagina;
import com.medtech.hospitales.models.SeccionPagina;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

/**
 * Servicio que maneja las operaciones CRUD relacionadas con las páginas
 * informativas y sus secciones en el sistema hospitalario.
 */
public class PaginaService {

    /**
     * EntityManagerFactory para gestionar conexiones a la base de datos.
     */
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("HospitalesPU");

    /**
     * Obtiene todas las páginas registradas en el sistema.
     *
     * @return lista de objetos Pagina.
     */
    public List<Pagina> obtenerTodasLasPaginas() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pagina p", Pagina.class).getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene todas las secciones asociadas a una página específica.
     *
     * @param idPagina ID de la página a consultar.
     * @return lista de secciones de la página.
     */
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

    /**
     * Actualiza el contenido de una sección existente.
     *
     * @param idSeccion ID de la sección a actualizar.
     * @param nuevoTitulo Nuevo título de la sección.
     * @param nuevoContenido Nuevo contenido de la sección.
     * @param nuevaImagenUrl Nueva URL de la imagen de la sección.
     */
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

    /**
     * Agrega una nueva sección a una página existente.
     *
     * @param idPagina ID de la página a la cual agregar la sección.
     * @param titulo Título de la nueva sección.
     * @param contenido Contenido de la nueva sección.
     * @param imagenUrl URL de la imagen asociada a la sección.
     * @param orden Orden en el que se debe mostrar la sección.
     */
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

    /**
     * Elimina una sección específica de una página.
     *
     * @param idSeccion ID de la sección a eliminar.
     */
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
