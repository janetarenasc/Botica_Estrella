package com.botica.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.botica.model.DetalleCarrito;

public interface DetalleCarritoRepository extends JpaRepository<DetalleCarrito, Long> {

    List<DetalleCarrito> findByCarritoId(Long carritoId);

    Optional<DetalleCarrito> findByCarritoIdAndProductoId(Long carritoId, Long productoId);

    void deleteByCarritoId(Long carritoId);

    void deleteById(Long id);
}