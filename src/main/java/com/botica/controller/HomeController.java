package com.botica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.botica.service.ProductosService;

@Controller
public class HomeController {
    private final ProductosService productosService;

    public HomeController(ProductosService productosService) {
        this.productosService = productosService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/index")
    public String home(Model model) {

        model.addAttribute(
                "destacados",
                productosService.listarDestacados());

        return "index";
    }

    @GetMapping("/nosotros")
    public String nosotros() {
        return "nosotros";
    }
}
