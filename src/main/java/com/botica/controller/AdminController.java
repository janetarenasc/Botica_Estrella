package com.botica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.botica.repository.CompraRepository;
import com.botica.repository.ProductosRepository;
import com.botica.repository.UsuarioRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ProductosRepository productosRepo;
    private final UsuarioRepository usuarioRepo;
    private final CompraRepository compraRepo;

    public AdminController(
            ProductosRepository productosRepo,
            UsuarioRepository usuarioRepo,
            CompraRepository compraRepo) {

        this.productosRepo = productosRepo;
        this.usuarioRepo = usuarioRepo;
        this.compraRepo = compraRepo;
    }

    @GetMapping
    public String dashboard(Model model) {

        model.addAttribute("totalProductos",
                productosRepo.count());

        model.addAttribute("totalUsuarios",
                usuarioRepo.count());

        model.addAttribute("totalCompras",
                compraRepo.count());

        return "admin/dashboard";
    }

}