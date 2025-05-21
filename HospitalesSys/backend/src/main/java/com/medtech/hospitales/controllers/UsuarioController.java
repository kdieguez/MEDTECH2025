package com.medtech.hospitales.controllers;

import com.medtech.hospitales.dtos.PacienteDTO;
import com.medtech.hospitales.models.Cargo;
import com.medtech.hospitales.models.Rol;
import com.medtech.hospitales.models.Usuario;
import com.medtech.hospitales.services.UsuarioService;
import com.medtech.hospitales.utils.JPAUtil;
import io.javalin.Javalin;
import io.javalin.http.Context;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador encargado de gestionar las operaciones relacionadas con los usuarios
 * del sistema hospitalario, incluyendo pacientes, roles, cargos, activación y filtrado.
 */
public class UsuarioController {

    /**
     * Servicio que contiene la lógica de negocio para usuarios.
     */
    private final UsuarioService usuarioService = new UsuarioService();

    /**
     * Registra todas las rutas relacionadas a usuarios en la instancia de Javalin.
     *
     * @param app instancia de Javalin.
     */
    public static void addRoutes(Javalin app) {
        UsuarioController controller = new UsuarioController();

        app.get("/usuarios", controller::obtenerUsuarios);
        app.get("/usuarios/{id}", controller::obtenerUsuarioPorId);
        app.post("/usuarios", controller::crearUsuario);
        app.put("/usuarios/{id}", controller::actualizarUsuario);
        app.delete("/usuarios/{id}", controller::eliminarUsuario);

        app.get("/pacientes", controller::obtenerPacientesHabilitados);

        app.get("/admin/usuarios/filtrar", controller::filtrarUsuarios);
        app.get("/admin/usuarios/paginados", controller::obtenerUsuariosPaginados);
        app.put("/admin/usuarios/{id}/activar", controller::activarUsuario);
        app.put("/admin/usuarios/{id}/desactivar", controller::desactivarUsuario);
    }

    /**
     * Devuelve todos los usuarios del sistema.
     *
     * @param ctx contexto HTTP
     */
    public void obtenerUsuarios(Context ctx) {
        List<Usuario> usuarios = usuarioService.obtenerUsuarios();
        ctx.json(usuarios);
    }

    /**
     * Devuelve los datos de un usuario específico por ID.
     *
     * @param ctx contexto HTTP con el ID del usuario en la ruta
     */
    public void obtenerUsuarioPorId(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        if (usuario != null) {
            ctx.json(usuario);
        } else {
            ctx.status(404).json(new ErrorResponse("Usuario no encontrado"));
        }
    }

    /**
     * Crea un nuevo usuario en la base de datos.
     *
     * @param ctx contexto HTTP con el cuerpo del usuario a registrar
     */
    public void crearUsuario(Context ctx) {
        try {
            Usuario usuario = ctx.bodyAsClass(Usuario.class);
            usuario.setIdRol(null);
            usuarioService.crearUsuario(usuario);
            ctx.status(201).json(Map.of("mensaje", "Usuario creado exitosamente"));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(new ErrorResponse("Error al crear usuario"));
        }
    }

    /**
     * Actualiza los datos de un usuario.
     *
     * @param ctx contexto HTTP con el ID y cuerpo del usuario actualizado
     */
    public void actualizarUsuario(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        try {
            Usuario usuario = ctx.bodyAsClass(Usuario.class);
            usuarioService.actualizarUsuario(id, usuario);
            ctx.status(200).json(Map.of("mensaje", "Usuario actualizado correctamente"));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(new ErrorResponse("Error al actualizar usuario"));
        }
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param ctx contexto HTTP
     */
    public void eliminarUsuario(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        usuarioService.eliminarUsuario(id);
        ctx.result("Usuario eliminado");
    }

    /**
     * Filtra los usuarios por correo, rol, y/o rango de fechas de creación.
     *
     * @param ctx contexto HTTP con parámetros de búsqueda
     */
    public void filtrarUsuarios(Context ctx) {
        String correo = ctx.queryParam("correo");
        String rolParam = ctx.queryParam("rol");
        String fechaInicioParam = ctx.queryParam("fechaInicio");
        String fechaFinParam = ctx.queryParam("fechaFin");

        Integer rol = rolParam != null ? Integer.valueOf(rolParam) : null;
        LocalDate fechaInicio = fechaInicioParam != null ? LocalDate.parse(fechaInicioParam) : null;
        LocalDate fechaFin = fechaFinParam != null ? LocalDate.parse(fechaFinParam) : null;

        List<Usuario> usuarios = usuarioService.filtrarUsuarios(correo, rol, fechaInicio, fechaFin);
        ctx.json(usuarios);
    }

    /**
     * Devuelve la lista de usuarios en modo paginado (10 por página).
     *
     * @param ctx contexto HTTP con query param "pagina"
     */
    public void obtenerUsuariosPaginados(Context ctx) {
        String paginaParam = ctx.queryParam("pagina");
        int pagina = (paginaParam != null && !paginaParam.isBlank()) ? Integer.parseInt(paginaParam) : 1;
        List<Usuario> usuarios = usuarioService.obtenerUsuariosPaginados(pagina);
        int totalUsuarios = usuarioService.obtenerUsuarios().size();
        int totalPaginas = Math.max((int) Math.ceil(totalUsuarios / 10.0), 1);

        Map<String, Object> response = new HashMap<>();
        response.put("usuarios", usuarios);
        response.put("totalPaginas", totalPaginas);

        ctx.json(response);
    }

    /**
     * Activa un usuario en el sistema y envía notificación correspondiente.
     *
     * @param ctx contexto HTTP con ID del usuario
     */
    public void activarUsuario(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        try {
            Usuario datos = ctx.bodyAsClass(Usuario.class);
            usuarioService.actualizarUsuario(id, datos);
            usuarioService.activarUsuario(id);
            ctx.result("Usuario activado y notificado");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(new ErrorResponse("Error al activar usuario"));
        }
    }

    /**
     * Desactiva un usuario del sistema.
     *
     * @param ctx contexto HTTP con ID del usuario
     */
    public void desactivarUsuario(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        try {
            usuarioService.desactivarUsuario(id);
            ctx.result("Usuario desactivado y notificado");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(new ErrorResponse("Error al desactivar usuario"));
        }
    }

    /**
     * Devuelve la lista de pacientes habilitados con su información relevante.
     *
     * @param ctx contexto HTTP
     */
    public void obtenerPacientesHabilitados(Context ctx) {
        try {
            List<PacienteDTO> pacientes = usuarioService.obtenerPacientesConPerfilDTO();
            ctx.json(pacientes);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Devuelve la lista de roles existentes en el sistema.
     *
     * @param ctx contexto HTTP
     */
    public void obtenerRoles(Context ctx) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Rol> roles = em.createQuery("SELECT r FROM Rol r", Rol.class).getResultList();
        ctx.json(roles);
        em.close();
    }

    /**
     * Devuelve la lista de cargos existentes en el sistema.
     *
     * @param ctx contexto HTTP
     */
    public void obtenerCargos(Context ctx) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Cargo> cargos = em.createQuery("SELECT c FROM Cargo c", Cargo.class).getResultList();
        ctx.json(cargos);
        em.close();
    }

    /**
     * Clase interna usada para retornar errores en formato JSON.
     *
     * @param details descripción del error
     */
    private record ErrorResponse(String details) {}
}
