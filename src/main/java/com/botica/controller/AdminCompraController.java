package com.botica.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.botica.model.Compra;
import com.botica.repository.CompraRepository;

@Controller
@RequestMapping("/admin")
public class AdminCompraController {

    private final CompraRepository compraRepo;

    public AdminCompraController(CompraRepository compraRepo) {
        this.compraRepo = compraRepo;
    }

    @GetMapping("/compras")
    public String compras(Model model) {

        List<Compra> compras = compraRepo.findAll();

        model.addAttribute("compras", compras);

        return "admin/compras";
    }
}