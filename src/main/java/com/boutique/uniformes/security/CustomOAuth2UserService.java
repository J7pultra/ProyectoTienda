package com.boutique.uniformes.security;

import com.boutique.uniformes.domain.Usuario;
import com.boutique.uniformes.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UsuarioService usuarioService;

    public CustomOAuth2UserService(@Lazy UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        
        try {
            return processOAuth2User(oauth2User);
        } catch (Exception e) {
            log.error("Error procesando usuario OAuth2: {}", e.getMessage());
            throw new OAuth2AuthenticationException("Error procesando usuario OAuth2: " + e.getMessage());
        }
    }

    private OAuth2User processOAuth2User(OAuth2User oauth2User) {
        String email = oauth2User.getAttribute("email");
        String nombre = oauth2User.getAttribute("given_name");
        String apellido = oauth2User.getAttribute("family_name");

        log.info("Procesando usuario OAuth2: {}", email);

        Optional<Usuario> usuarioExistente = usuarioService.obtenerUsuarioPorEmail(email);
        
        Usuario usuario;
        if (usuarioExistente.isPresent()) {
            // Usuario existente - actualizar información
            usuario = usuarioExistente.get();
            
            // Actualizar información si es necesario
            if (nombre != null && !nombre.equals(usuario.getNombre())) {
                usuario.setNombre(nombre);
            }
            if (apellido != null && !apellido.equals(usuario.getApellido())) {
                usuario.setApellido(apellido);
            }
            
            usuarioService.guardarUsuario(usuario);
            log.info("Usuario OAuth2 existente actualizado: {}", email);
        } else {
            // Crear nuevo usuario
            usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setUsername(email); // Usar email como username
            usuario.setNombre(nombre != null ? nombre : "Usuario");
            usuario.setApellido(apellido != null ? apellido : "Google");
            usuario.setPassword("OAUTH2_USER"); // Password placeholder
            // Corrección: Asignar el rol por defecto usando el enum de Rol.NombreRol y el método setRol(String nombreRol)
            usuario.setRol("EMPLEADO"); // Rol por defecto
            usuario.setActivo(true);
            usuario.setFechaCreacion(LocalDateTime.now());
            
            usuarioService.registrarUsuario(usuario);
            log.info("Nuevo usuario OAuth2 creado: {}", email);
        }

        return new CustomOAuth2User(oauth2User, usuario);
    }
}
