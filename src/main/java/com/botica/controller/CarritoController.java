package com.botica.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.botica.model.DetalleCarrito;
import com.botica.service.CarritoService;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    private final CarritoService carritoService;

    public CarritoController(CarritoService carritoService) {
        this.carritoService = carritoService;
    }

    @PostMapping("/agregar/{id}")
    public String agregarProducto(@PathVariable Long id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();

        carritoService.agregarProducto(email, id);

        return "redirect:/carrito";
    }

    @GetMapping
    public String verCarrito(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();

        List<DetalleCarrito> items = carritoService.obtenerItemsCarrito(email);

        double total = items.stream()
                .mapToDouble(i -> i.getPrecio() * i.getCantidad())
                .sum();

        model.addAttribute("items", items);
        model.addAttribute("total", total);

        return "carrito";
    }

    @PostMapping("/sumar/{detalleId}")
    public String sumarCantidad(@PathVariable Long detalleId) {

        carritoService.sumarCantidad(detalleId);

        return "redirect:/carrito";
    }

    @PostMapping("/restar/{detalleId}")
    public String restarCantidad(@PathVariable Long detalleId) {

        carritoService.restarCantidad(detalleId);

        return "redirect:/carrito";
    }

    @PostMapping("/eliminar/{detalleId}")
    public String eliminarProducto(@PathVariable Long detalleId) {

        carritoService.eliminarItem(detalleId);

        return "redirect:/carrito";
    }

    @PostMapping("/finalizar")
    public String finalizarCompra(RedirectAttributes redirectAttributes) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        carritoService.finalizarCompra(auth.getName());

        redirectAttributes.addFlashAttribute(
                "mensaje",
                "Compra realizada con éxito. Gracias por tu pedido.");

        return "redirect:/carrito";
    }
}