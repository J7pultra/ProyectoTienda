package com.boutique.uniformes.service;

import com.boutique.uniformes.domain.Rol;
import com.boutique.uniformes.domain.Usuario;
import com.boutique.uniformes.repository.RolRepository;
import com.boutique.uniformes.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;

    // Usar @Lazy para romper la dependencia circular
    public UsuarioServiceImpl(UsuarioRepository usuarioRepository,
                             @Lazy PasswordEncoder passwordEncoder,
                             RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
    }

   @Override
public Usuario guardarUsuario(Usuario usuario) {
    if (usuario.getId() == null) {
        return registrarUsuario(usuario);
    }

    Usuario usuarioExistente = obtenerUsuarioPorId(usuario.getId());
    usuarioExistente.setNombre(usuario.getNombre());
    usuarioExistente.setApellido(usuario.getApellido());
    usuarioExistente.setEmail(usuario.getEmail());
    usuarioExistente.setRol(usuario.getRol());

    // Solo actualizar password si no es OAuth2 y si cambió
    if (!"OAUTH2_USER".equals(usuario.getPassword()) &&
        !usuario.getPassword().equals(usuarioExistente.getPassword())) {
        usuarioExistente.setPassword(passwordEncoder.encode(usuario.getPassword()));
    }

    return usuarioRepository.save(usuarioExistente);
}

@Override
public Usuario registrarUsuario(Usuario usuario) {
    // Si el usuario viene con un String en el campo rol (por ejemplo, desde el formulario),
    // buscar el objeto Rol correspondiente y asignarlo
    if (usuario.getRol() == null && usuario.getRoles() == null || usuario.getRoles().isEmpty()) {
        // Intentar obtener el nombre del rol desde un campo temporal (por ejemplo, usuario.rolNombre)
        try {
            String rolNombre = (String) usuario.getClass().getDeclaredField("rolNombre").get(usuario);
            if (rolNombre != null && !rolNombre.isBlank()) {
                Rol.NombreRol nombreRol = Rol.NombreRol.valueOf(rolNombre.toUpperCase());
                Rol rol = rolRepository.findByNombre(nombreRol)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolNombre));
                usuario.setRol(rol);
            }
        } catch (Exception ignored) {}
    }
    if (existeUsuarioPorUsername(usuario.getUsername())) {
        throw new RuntimeException("Ya existe un usuario con ese nombre de usuario");
    }
    if (existeUsuarioPorEmail(usuario.getEmail())) {
        throw new RuntimeException("Ya existe un usuario con ese email");
    }
    // Solo encriptar si no es un usuario OAuth2
    if (!"OAUTH2_USER".equals(usuario.getPassword())) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
    }
    usuario.setFechaCreacion(LocalDateTime.now());
    usuario.setActivo(true);
    return usuarioRepository.save(usuario);
}


    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerUsuariosActivos() {
        return usuarioRepository.findByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> obtenerUsuariosPaginados(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Usuario> buscarUsuarios(String buscar, Pageable pageable) {
        return usuarioRepository.findByUsernameContainingIgnoreCaseOrNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(
                buscar, buscar, buscar, pageable);
    }

    @Override
    public void eliminarUsuario(Long id) {
        Usuario usuario = obtenerUsuarioPorId(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Override
    public void cambiarEstadoUsuario(Long id, Boolean activo) {
        Usuario usuario = obtenerUsuarioPorId(id);
        usuario.setActivo(activo);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarUsuariosActivos() {
        return usuarioRepository.countByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeUsuarioPorUsername(String username) {
        return usuarioRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeUsuarioPorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }
}
