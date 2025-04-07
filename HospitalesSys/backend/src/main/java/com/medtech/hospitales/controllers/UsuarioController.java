package com.medtech.hospitales.controllers;

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

public class UsuarioController {

    private final UsuarioService usuarioService = new UsuarioService();

    public static void addRoutes(Javalin app) {
        UsuarioController controller = new UsuarioController();

        app.get("/usuarios", controller::obtenerUsuarios);
        app.get("/usuarios/{id}", controller::obtenerUsuarioPorId);
        app.post("/usuarios", controller::crearUsuario);
        app.put("/usuarios/{id}", controller::actualizarUsuario);
        app.delete("/usuarios/{id}", controller::eliminarUsuario);

        // ADMINISTRACIÓN
        app.get("/admin/usuarios/filtrar", controller::filtrarUsuarios);
        app.get("/admin/usuarios/paginados", controller::obtenerUsuariosPaginados);
        app.put("/admin/usuarios/{id}/activar", controller::activarUsuario);
        app.put("/admin/usuarios/{id}/desactivar", controller::desactivarUsuario);

    }

    public void obtenerUsuarios(Context ctx) {
        List<Usuario> usuarios = usuarioService.obtenerUsuarios();
        ctx.json(usuarios);
    }

    public void obtenerUsuarioPorId(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        if (usuario != null) {
            ctx.json(usuario);
        } else {
            ctx.status(404).json(new ErrorResponse("Usuario no encontrado"));
        }
    }

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

    public void actualizarUsuario(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        try {
            Usuario usuario = ctx.bodyAsClass(Usuario.class);
            System.out.println("==> Actualizando usuario con ID: " + id);
            System.out.println("==> Datos recibidos: " + usuario);
            usuarioService.actualizarUsuario(id, usuario);
            ctx.status(200).json(Map.of("mensaje", "Usuario actualizado correctamente"));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(new ErrorResponse("Error al actualizar usuario"));
        }
    }

    public void eliminarUsuario(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        usuarioService.eliminarUsuario(id);
        ctx.result("Usuario eliminado");
    }

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

    private record ErrorResponse(String details) {}
    public void obtenerRoles(Context ctx) {
    EntityManager em = JPAUtil.getEntityManager();
    List<Rol> roles = em.createQuery("SELECT r FROM Rol r", Rol.class).getResultList();
    ctx.json(roles);
    em.close();
    }

    public void obtenerCargos(Context ctx) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Cargo> cargos = em.createQuery("SELECT c FROM Cargo c", Cargo.class).getResultList();
        ctx.json(cargos);
        em.close();
    }

}