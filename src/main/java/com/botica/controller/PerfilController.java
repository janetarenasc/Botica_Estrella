package com.botica.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.botica.model.Compra;
import com.botica.model.Usuario;
import com.botica.repository.UsuarioRepository;
import com.botica.repository.CompraRepository;

@Controller
public class PerfilController {

    private final UsuarioRepository usuarioRepo;
    private final CompraRepository compraRepo;

    public PerfilController(UsuarioRepository usuarioRepo,
                            CompraRepository compraRepo) {
        this.usuarioRepo = usuarioRepo;
        this.compraRepo = compraRepo;
    }

    @GetMapping("/perfil")
    public String perfil(Model model, Authentication auth) {

        String email = auth.getName();

        Usuario usuario = usuarioRepo.findByEmail(email).orElseThrow();

        List<Compra> compras = compraRepo.findByUsuarioId(usuario.getId());

        model.addAttribute("usuario", usuario);
        model.addAttribute("compras", compras);

        return "perfil";
    }
}