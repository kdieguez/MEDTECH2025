package com.medtech.hospitales.services;

import com.medtech.hospitales.models.SeccionPendiente;
import com.medtech.hospitales.utils.CorreoUtils;
import com.medtech.hospitales.utils.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

/**
 * Servicio que gestiona los cambios pendientes en las secciones de páginas,
 * incluyendo guardado, listado, aprobación, rechazo y notificaciones por correo.
 */
public class SeccionPendienteService {

    /**
     * Guarda un cambio pendiente en la base de datos.
     *
     * @param seccion Objeto SeccionPendiente con los datos a guardar.
     */
    public void guardarCambio(SeccionPendiente seccion) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(seccion);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * Lista todos los cambios pendientes que están en estado "PENDIENTE".
     *
     * @return Lista de SeccionPendiente pendientes.
     */
    public List<SeccionPendiente> listarPendientes() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<SeccionPendiente> query = em.createQuery(
                "SELECT s FROM SeccionPendiente s " +
                "JOIN FETCH s.pagina " +
                "JOIN FETCH s.usuarioSolicitante " +
                "WHERE s.estadoAprobacion = 'PENDIENTE'",
                SeccionPendiente.class
            );
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Aprueba un cambio pendiente, actualizando o creando la sección correspondiente.
     * También asigna la fecha y usuario aprobador.
     *
     * @param id ID del cambio pendiente a aprobar.
     * @param idAprobador ID del usuario que aprueba el cambio.
     */
    public void aprobarCambio(Long id, Long idAprobador) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            SeccionPendiente sp = em.find(SeccionPendiente.class, id);

            if (sp != null) {
                sp.setEstadoAprobacion("APROBADO");
                sp.setFechaAprobacion(new java.sql.Timestamp(System.currentTimeMillis()));
                sp.setUsuarioAprobador(em.getReference(com.medtech.hospitales.models.Usuario.class, idAprobador));

                if ("EDICION".equalsIgnoreCase(sp.getTipo()) && sp.getIdSeccionOriginal() != null) {
                    com.medtech.hospitales.models.SeccionPagina original = em.find(
                        com.medtech.hospitales.models.SeccionPagina.class,
                        sp.getIdSeccionOriginal()
                    );
                    if (original != null) {
                        em.remove(original);
                    }
                }
                com.medtech.hospitales.models.SeccionPagina nueva = new com.medtech.hospitales.models.SeccionPagina();
                nueva.setPagina(sp.getPagina());
                nueva.setTitulo(sp.getTitulo());
                nueva.setContenido(sp.getContenido());
                nueva.setImagenUrl(sp.getImagenUrl());
                nueva.setOrden(sp.getOrden());
                em.persist(nueva);
            }

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * Rechaza un cambio pendiente y envía un correo al solicitante con el motivo.
     *
     * @param id ID del cambio pendiente a rechazar.
     * @param idAprobador ID del usuario que rechaza el cambio.
     * @param comentario Comentario que indica el motivo del rechazo.
     */
    public void rechazarCambio(Long id, Long idAprobador, String comentario) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            SeccionPendiente sp = em.find(SeccionPendiente.class, id);
            if (sp != null) {
                sp.setEstadoAprobacion("RECHAZADO");
                sp.setFechaAprobacion(new java.sql.Timestamp(System.currentTimeMillis()));
                sp.setUsuarioAprobador(em.getReference(com.medtech.hospitales.models.Usuario.class, idAprobador));
                sp.setComentarioRechazo(comentario);

                String correoDestino = sp.getUsuarioSolicitante().getEmail();
                String nombreUsuario = sp.getUsuarioSolicitante().getNombre();
                String nombrePagina = sp.getPagina().getNombre();
                String titulo = sp.getTitulo();

                String asunto = "Tu propuesta de cambio fue rechazada";

                String cuerpo = """
                    <p>Hola <strong>%s</strong>,</p>
                    <p>Tu propuesta para la sección <strong>%s</strong> en la página <strong>%s</strong> ha sido <span style='color:red;'>rechazada</span>.</p>
                    <p><strong>Motivo del rechazo:</strong> %s</p>
                    <p>Puedes corregir tu propuesta y volver a enviarla desde el sistema.</p>
                    <p>Saludos,<br>Equipo Hospitales La Paz</p>
                    """.formatted(nombreUsuario, titulo, nombrePagina, comentario);

                CorreoUtils.enviarCorreoHtml(correoDestino, asunto, cuerpo);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            System.err.println("Error al rechazar cambio o enviar correo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene un cambio pendiente por su ID.
     *
     * @param id ID del cambio pendiente
     * @return Objeto SeccionPendiente o null si no existe.
     */
    public SeccionPendiente obtenerPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(SeccionPendiente.class, id);
        } finally {
            em.close();
        }
    }
}
