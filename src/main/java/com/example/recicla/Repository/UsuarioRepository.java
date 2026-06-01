package com.example.recicla.Repository;

import com.example.recicla.Entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    List<Usuario> findByEnderecoContainingIgnoreCase(String endereco);
}
