/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.service;


import com.boutique.uniformes.domain.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario registrarUsuario(Usuario usuario);
    Usuario guardarUsuario(Usuario usuario);
    Usuario obtenerUsuarioPorId(Long id);
    Optional<Usuario> obtenerUsuarioPorUsername(String username);
    Optional<Usuario> obtenerUsuarioPorEmail(String email);
    List<Usuario> obtenerUsuariosActivos();
    Page<Usuario> obtenerUsuariosPaginados(Pageable pageable);
    Page<Usuario> buscarUsuarios(String buscar, Pageable pageable);
    void eliminarUsuario(Long id);
    void cambiarEstadoUsuario(Long id, Boolean activo);
    Long contarUsuariosActivos();
    boolean existeUsuarioPorUsername(String username);
    boolean existeUsuarioPorEmail(String email);
}
