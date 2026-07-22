package com.botica.config;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.botica.repository.UsuarioRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Component
public class ActivityInterceptor implements HandlerInterceptor {

    private final UsuarioRepository usuarioRepository;

    public ActivityInterceptor(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        if (auth != null
                && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getName())) {

            usuarioRepository.findByEmail(auth.getName())
                    .ifPresent(usuario -> {

                        usuario.setUltimaActividad(
                                LocalDateTime.now());

                        usuarioRepository.save(usuario);
                    });
        }

        return true;
    }
}