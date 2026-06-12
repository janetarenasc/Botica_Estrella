package com.botica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.botica.model.Productos;

public interface ProductosRepository extends JpaRepository<Productos, Long>{

    List<Productos> findByDestacadoTrue();
}
