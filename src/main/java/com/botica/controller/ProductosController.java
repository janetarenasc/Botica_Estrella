package com.botica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.botica.service.ProductosService;

@Controller
@RequestMapping("/productos")
public class ProductosController {

    private final ProductosService service;

    public ProductosController(ProductosService service) {
        this.service = service;
    }

    @GetMapping
    public String mostrarProductos(Model model) {

        model.addAttribute(
                "productos",
                service.listarTodos());

        return "productos";
    }
}