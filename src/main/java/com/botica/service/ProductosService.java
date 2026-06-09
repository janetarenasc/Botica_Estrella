package com.botica.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.botica.model.Productos;
import com.botica.repository.ProductosRepository;

@Service
public class ProductosService {
    @Autowired
    private ProductosRepository repository;

    public List<Productos> listarTodos() {
        return repository.findAll();
    }
}
