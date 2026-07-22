package com.botica.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.botica.model.EntradaStock;
import com.botica.model.Productos;

public interface EntradaStockRepository
                extends JpaRepository<EntradaStock, Long> {

        List<EntradaStock> findByProductoOrderByFechaIngresoAsc(
                        Productos producto);

        List<EntradaStock> findByProductoIdOrderByFechaIngresoDesc(Long productoId);

        List<EntradaStock> findByProductoIdAndStockDisponibleGreaterThanOrderByFechaIngresoAsc(
                        Long productoId,
                        Integer stock);

}