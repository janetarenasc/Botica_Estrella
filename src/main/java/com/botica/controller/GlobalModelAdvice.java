package com.botica.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.botica.repository.UsuarioRepository;
import com.botica.service.CarritoService;

@ControllerAdvice
public class GlobalModelAdvice {

    private final CarritoService carritoService;
    private final UsuarioRepository usuarioRepo;

    public GlobalModelAdvice(CarritoService carritoService,
                             UsuarioRepository usuarioRepo) {
        this.carritoService = carritoService;
        this.usuarioRepo = usuarioRepo;
    }

    @ModelAttribute("cartCount")
    public int cartCount(Authentication auth) {

        if (auth == null) return 0;

        String email = auth.getName();

        return carritoService.obtenerCantidadTotal(email);
    }
}
