package com.boutique.uniformes.security;

import com.boutique.uniformes.domain.Usuario;
import com.boutique.uniformes.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Intentando autenticar usuario: {}", username);
        
        Usuario usuario = usuarioRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> {
                    log.warn("Usuario no encontrado: {}", username);
                    return new UsernameNotFoundException("Usuario no encontrado: " + username);
                });

        if (!usuario.getActivo()) {
            log.warn("Usuario inactivo intentó acceder: {}", username);
            throw new UsernameNotFoundException("Usuario inactivo: " + username);
        }

        log.debug("Usuario autenticado exitosamente: {}", username);
        return usuario;
    }
}
