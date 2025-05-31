package com.boutique.uniformes.dao.interfaces;

import com.boutique.uniformes.domain.Usuario;

import java.util.List;
import java.util.Optional;


public interface UsuarioDao {
    void save(Usuario usuario);
    Optional<Usuario> findById(Long id);
    List<Usuario> findAll();
    void update(Usuario usuario);
    void delete(Long id);
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByRol(String rol);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    void updateLastAccess(Long id);
    List<Usuario> findByNombreCompletoContaining(String nombre);
    List<Usuario> findEmpleados();
    List<Usuario> findAdministradores(); // Este método debe estar aquí
}
