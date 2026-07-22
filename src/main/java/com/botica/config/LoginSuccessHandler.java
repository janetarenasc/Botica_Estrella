package com.botica.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.botica.model.Usuario;
import com.botica.repository.UsuarioRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioRepository usuarioRepository;

    public LoginSuccessHandler(
            UsuarioRepository usuarioRepository) {

        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        String email = authentication.getName();

        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElse(null);

        if (usuario != null) {

            usuario.setUltimaActividad(
                    LocalDateTime.now());

            usuarioRepository.save(usuario);
        }

        response.sendRedirect("/index");
    }
}