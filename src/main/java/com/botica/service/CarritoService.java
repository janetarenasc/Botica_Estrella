package com.botica.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.botica.model.Carrito;
import com.botica.model.Compra;
import com.botica.model.DetalleCarrito;
import com.botica.model.DetalleCompra;
import com.botica.model.Productos;
import com.botica.model.Usuario;
import com.botica.repository.CarritoRepository;
import com.botica.repository.CompraRepository;
import com.botica.repository.DetalleCarritoRepository;
import com.botica.repository.DetalleCompraRepository;
import com.botica.repository.ProductosRepository;
import com.botica.repository.UsuarioRepository;

@Service
public class CarritoService {

    private final CarritoRepository carritoRepo;
    private final DetalleCarritoRepository detalleRepo;
    private final ProductosRepository productosRepo;
    private final UsuarioRepository usuarioRepo;
    private final CompraRepository compraRepo;
    private final DetalleCompraRepository detalleCompraRepo;

    public CarritoService(CarritoRepository carritoRepo,
            DetalleCarritoRepository detalleRepo,
            ProductosRepository productosRepo,
            UsuarioRepository usuarioRepo,
            CompraRepository compraRepo,
            DetalleCompraRepository detalleCompraRepo) {

        this.carritoRepo = carritoRepo;
        this.detalleRepo = detalleRepo;
        this.productosRepo = productosRepo;
        this.usuarioRepo = usuarioRepo;
        this.compraRepo = compraRepo;
        this.detalleCompraRepo = detalleCompraRepo;
    }

    public void agregarProducto(String emailUsuario, Long productoId) {

        Usuario usuario = usuarioRepo.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Carrito carrito = carritoRepo.findByUsuarioId(usuario.getId());

        if (carrito == null) {
            carrito = new Carrito();
            carrito.setUsuario(usuario);
            carrito = carritoRepo.save(carrito);
        }

        Productos producto = productosRepo.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Optional<DetalleCarrito> existente = detalleRepo.findByCarritoIdAndProductoId(carrito.getId(), productoId);

        int cantidadEnCarrito = existente.map(DetalleCarrito::getCantidad).orElse(0);

        // 🔥 VALIDACIÓN REAL
        if (cantidadEnCarrito + 1 > producto.getStock()) {
            throw new RuntimeException("No hay suficiente stock disponible");
        }

        if (existente.isPresent()) {

            DetalleCarrito item = existente.get();
            item.setCantidad(item.getCantidad() + 1);
            detalleRepo.save(item);

        } else {

            DetalleCarrito detalle = new DetalleCarrito();
            detalle.setCarrito(carrito);
            detalle.setProducto(producto);
            detalle.setCantidad(1);
            detalle.setPrecio(producto.getPrecio());

            detalleRepo.save(detalle);
        }
    }

    public List<DetalleCarrito> obtenerItemsCarrito(String email) {

        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow();

        Carrito carrito = carritoRepo.findByUsuarioId(usuario.getId());

        if (carrito == null) {
            return new ArrayList<>();
        }

        return detalleRepo.findByCarritoId(carrito.getId());
    }

    public void sumarCantidad(Long detalleId) {

        DetalleCarrito item = detalleRepo.findById(detalleId)
                .orElseThrow();

        item.setCantidad(item.getCantidad() + 1);

        detalleRepo.save(item);
    }

    public void restarCantidad(Long detalleId) {

        DetalleCarrito item = detalleRepo.findById(detalleId)
                .orElseThrow();

        if (item.getCantidad() > 1) {

            item.setCantidad(item.getCantidad() - 1);

            detalleRepo.save(item);

        } else {

            detalleRepo.delete(item);
        }
    }

    public void eliminarItem(Long detalleId) {

        detalleRepo.deleteById(detalleId);
    }

    public void vaciarCarrito(String email) {

        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow();

        Carrito carrito = carritoRepo.findByUsuarioId(usuario.getId());

        if (carrito != null) {

            List<DetalleCarrito> items = detalleRepo.findByCarritoId(carrito.getId());

            detalleRepo.deleteAll(items);
        }
    }

    @Transactional
    public void finalizarCompra(String email) {

        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow();

        Carrito carrito = carritoRepo.findByUsuarioId(usuario.getId());
        if (carrito == null)
            return;

        List<DetalleCarrito> items = detalleRepo.findByCarritoId(carrito.getId());
        if (items.isEmpty())
            return;

        // 1. VALIDAR STOCK PRIMERO
        for (DetalleCarrito i : items) {
            if (i.getProducto().getStock() < i.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para " + i.getProducto().getNombre());
            }
        }

        // 2. CREAR COMPRA
        Compra compra = new Compra();
        compra.setUsuario(usuario);
        compra.setFecha(LocalDateTime.now());

        double total = items.stream()
                .mapToDouble(i -> i.getPrecio() * i.getCantidad())
                .sum();

        compra.setTotal(total);
        compra = compraRepo.save(compra);

        // 3. DETALLES + DESCUENTO STOCK
        for (DetalleCarrito i : items) {

            Productos producto = i.getProducto();

            producto.setStock(producto.getStock() - i.getCantidad());
            productosRepo.save(producto);

            DetalleCompra dc = new DetalleCompra();
            dc.setCompra(compra);
            dc.setProducto(producto);
            dc.setCantidad(i.getCantidad());
            dc.setPrecio(i.getPrecio());

            detalleCompraRepo.save(dc);
        }

        // 4. LIMPIAR CARRITO
        detalleRepo.deleteByCarritoId(carrito.getId());

    }

    public int obtenerCantidadTotal(String email) {

        Usuario usuario = usuarioRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Carrito carrito = carritoRepo.findByUsuarioId(usuario.getId());

        if (carrito == null) {
            return 0;
        }

        List<DetalleCarrito> items = detalleRepo.findByCarritoId(carrito.getId());

        return items.stream()
                .mapToInt(DetalleCarrito::getCantidad)
                .sum();
    }
}