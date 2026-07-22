package com.botica.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.botica.model.EntradaStock;
import com.botica.model.Productos;
import com.botica.repository.EntradaStockRepository;
import com.botica.repository.ProductosRepository;

@Controller
@RequestMapping("/admin/stock")
public class StockController {

        private final ProductosRepository productoRepo;
        private final EntradaStockRepository entradaRepo;

        public StockController(
                        ProductosRepository productoRepo,
                        EntradaStockRepository entradaRepo) {

                this.productoRepo = productoRepo;
                this.entradaRepo = entradaRepo;
        }

        @GetMapping("/{id}")
        public String formulario(
                        @PathVariable Long id,
                        Model model) {

                Productos producto = productoRepo.findById(id)
                                .orElseThrow();

                model.addAttribute("producto", producto);

                return "admin/ingresar-stock";
        }

        @PostMapping("/guardar")
        public String guardar(
                        @RequestParam Long productoId,
                        @RequestParam Integer cantidad,
                        @RequestParam BigDecimal precioCompra,
                        @RequestParam Double precioVenta) {

                Productos producto = productoRepo.findById(productoId)
                                .orElseThrow();

                EntradaStock entrada = new EntradaStock();

                entrada.setProducto(producto);
                entrada.setCantidad(cantidad);
                entrada.setStockDisponible(cantidad);
                entrada.setPrecioCompra(precioCompra);
                entrada.setPrecioVenta(
                                BigDecimal.valueOf(precioVenta));
                entrada.setFechaIngreso(LocalDateTime.now());

                entradaRepo.save(entrada);

                producto.setStock(
                                producto.getStock() + cantidad);

                /* producto.setPrecio(precioVenta); */

                productoRepo.save(producto);

                return "redirect:/admin/productos";
        }

        @GetMapping("/historial/{id}")
        public String historial(
                        @PathVariable Long id,
                        Model model) {

                Productos producto = productoRepo.findById(id)
                                .orElseThrow();

                model.addAttribute("producto", producto);

                model.addAttribute(
                                "entradas",
                                entradaRepo.findByProductoIdOrderByFechaIngresoDesc(id));

                return "admin/historial-stock";
        }
}