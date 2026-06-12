package com.botica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.botica.model.Carrito;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Carrito findByUsuarioId(Long usuarioId);
}