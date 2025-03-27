package com.medtech.hospitales.controllers;

import com.medtech.hospitales.models.Usuario;
import com.medtech.hospitales.services.UsuarioService;
import com.medtech.hospitales.services.RolService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

public class UsuarioController {

    private final UsuarioService usuarioService = new UsuarioService();

    public static void addRoutes(Javalin app) {
        UsuarioController controller = new UsuarioController();
    
        app.get("/usuarios", controller::obtenerUsuarios);
        app.get("/usuarios/{id}", controller::obtenerUsuarioPorId);
        app.post("/usuarios", controller::crearUsuario);
        app.put("/usuarios/{id}", controller::actualizarUsuario);
        app.delete("/usuarios/{id}", controller::eliminarUsuario);
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
            ctx.status(404).result("Usuario no encontrado");
        }
    }

    public void crearUsuario(Context ctx) {
        try {
            Usuario usuario = ctx.bodyAsClass(Usuario.class);
    
            // El rol se deja null por defecto
            usuario.setIdRol(null);
    
            usuarioService.crearUsuario(usuario);
            ctx.status(201).result("Usuario creado exitosamente sin rol");
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("Error del servidor: " + e.getMessage());
        }
    }
      

    public void actualizarUsuario(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        Usuario usuario = ctx.bodyAsClass(Usuario.class);
        usuarioService.actualizarUsuario(id, usuario);
        ctx.result("Usuario actualizado exitosamente");
    }

    public void eliminarUsuario(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        usuarioService.eliminarUsuario(id);
        ctx.result("Usuario eliminado exitosamente");
    }
}