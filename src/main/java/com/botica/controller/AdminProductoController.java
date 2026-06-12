package com.botica.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

import com.botica.model.Productos;
import com.botica.repository.ProductosRepository;

@Controller
@RequestMapping("/admin/productos")
public class AdminProductoController {

    private final ProductosRepository productosRepo;

    public AdminProductoController(ProductosRepository productosRepo) {
        this.productosRepo = productosRepo;
    }

    // LISTAR
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productosRepo.findAll());
        return "admin/productos";
    }

    // FORM NUEVO PRODUCTO
    @GetMapping("/nuevo")
    public String nuevoProducto(Model model) {
        model.addAttribute("producto", new Productos());
        return "admin/form-producto";
    }

    // GUARDAR PRODUCTO
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Productos producto,
            @RequestParam("file") MultipartFile file) throws IOException {

        String ruta = "C:/botica-uploads/";

        File directorio = new File(ruta);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }

        if (!file.isEmpty()) {

            // 🔥 limpiar nombre del archivo
            String nombreOriginal = file.getOriginalFilename()
                    .replace(" ", "_");

            // 🔥 evitar colisiones
            String nombreArchivo = System.currentTimeMillis() + "_" + nombreOriginal;

            File destino = new File(ruta + File.separator + nombreArchivo);

            file.transferTo(destino);

            producto.setImagen(nombreArchivo);

        } else if (producto.getId() != null) {

            Productos existente = productosRepo.findById(producto.getId())
                    .orElseThrow();

            producto.setImagen(existente.getImagen());
        }

        productosRepo.save(producto);

        return "redirect:/admin/productos";
    }

    // ELIMINAR PRODUCTO
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        Productos p = productosRepo.findById(id)
                .orElseThrow();

        productosRepo.delete(p);
        return "redirect:/admin/productos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {

        Productos producto = productosRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        model.addAttribute("producto", producto);

        return "admin/form-producto";
    }
}