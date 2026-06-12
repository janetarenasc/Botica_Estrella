package com.botica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.botica.model.DetalleCompra;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Long> {
}