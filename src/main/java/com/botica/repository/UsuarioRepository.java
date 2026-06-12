package com.botica.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.botica.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
}