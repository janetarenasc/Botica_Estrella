package com.botica.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.botica.model.Usuario;
import com.botica.repository.UsuarioRepository;

@Controller
@RequestMapping("/admin/usuarios")
public class AdminUsuarioController {

    private final UsuarioRepository usuarioRepo;

    public AdminUsuarioController(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @GetMapping
    public String listar(Model model) {

        List<Usuario> usuarios = usuarioRepo.findAll();

        model.addAttribute("usuarios", usuarios);

        return "admin/usuarios";
    }
}
