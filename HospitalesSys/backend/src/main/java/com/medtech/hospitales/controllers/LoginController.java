package com.medtech.hospitales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medtech.hospitales.dtos.LoginRequest;
import com.medtech.hospitales.models.Usuario;
import com.medtech.hospitales.utils.JWTUtil;
import com.medtech.hospitales.utils.JPAUtil;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Controlador encargado del proceso de autenticación de usuarios en el sistema hospitalario.
 * Verifica las credenciales y genera el token JWT si son válidas.
 */
public class LoginController {

    /**
     * Handler de Javalin encargado de realizar el proceso de login del usuario.
     * <p>
     * Verifica el identificador (correo o usuario), contraseña, si el usuario está activo,
     * y determina si debe mostrar el formulario para completar datos del perfil de doctor.
     * </p>
     */
    public Handler login = ctx -> {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String rawBody = ctx.body();
            ObjectMapper objectMapper = new ObjectMapper();
            LoginRequest body = objectMapper.readValue(rawBody, LoginRequest.class);

            List<Usuario> usuarios = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.email = :identificador OR u.usuario = :identificador",
                    Usuario.class
            )
            .setParameter("identificador", body.getIdentificador())
            .getResultList();

            if (usuarios.isEmpty()) {
                ctx.status(401).result("Correo o usuario no encontrado");
                return;
            }

            Usuario user = usuarios.get(0);

            if (!user.getPassword().equals(body.getContrasena())) {
                ctx.status(401).result("Contraseña incorrecta");
                return;
            }

            if (user.getHabilitado() == 0 && user.getIdRol() == null) {
                ctx.status(403).result("Tu cuenta aún no ha sido activada por el administrador.");
                return;
            }

            if (user.getHabilitado() == 0) {
                ctx.status(403).result("Tu cuenta está inactiva. Contacta con el administrador.");
                return;
            }

            Integer idCargo = 0;
            try {
                idCargo = em.createQuery(
                        "SELECT uc.cargo.id FROM UsuarioCargo uc WHERE uc.usuario.id = :idUsuario",
                        Integer.class
                )
                .setParameter("idUsuario", user.getId())
                .setMaxResults(1)
                .getSingleResult();
            } catch (Exception e) {
                System.out.println("No tiene cargo asociado");
            }

            boolean mostrarFormularioDoctor = (user.getIdRol() == 2 && idCargo == 1);

            boolean yaTienePerfil = !em.createQuery(
                    "SELECT d FROM InfoDoctor d WHERE d.usuario.id = :idUsuario", Usuario.class)
                    .setParameter("idUsuario", user.getId())
                    .getResultList()
                    .isEmpty();

            mostrarFormularioDoctor = mostrarFormularioDoctor && !yaTienePerfil;

            String token = JWTUtil.generateToken(
                    user.getId(),
                    user.getIdRol(),
                    idCargo,
                    user.getNombre(),
                    user.getEmail()
            );

            ctx.json(new LoginResponse(token, mostrarFormularioDoctor));

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("Error en el servidor: " + e.getMessage());
        } finally {
            em.close();
        }
    };

    /**
     * Clase interna que representa la respuesta enviada al cliente después de un login exitoso.
     */
    static class LoginResponse {
        /** Token JWT generado tras autenticación exitosa. */
        public String token;
        
        /** Indica si debe mostrarse el formulario para completar información del perfil de doctor. */
        public boolean mostrarFormularioDoctor;
        
        /**
         * Constructor para la respuesta del Login.
         *
         * @param token token JWT generado
         * @param mostrarFormularioDoctor bandera que indica si debe mostrarse el formulario de doctor
         */
        public LoginResponse(String token, boolean mostrarFormularioDoctor) {
            this.token = token;
            this.mostrarFormularioDoctor = mostrarFormularioDoctor;
        }
    }
}
