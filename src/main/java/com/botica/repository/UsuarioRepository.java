package com.botica.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.botica.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

}
