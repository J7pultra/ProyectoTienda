package com.boutique.uniformes.security;

import com.boutique.uniformes.domain.Usuario;
import com.boutique.uniformes.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioService usuarioService;

    public CustomAuthenticationSuccessHandler(@Lazy UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {

        String username = authentication.getName();
        Usuario usuario = null;

        try {
            // Verificar si es OAuth2 o login tradicional
            if (authentication.getPrincipal() instanceof CustomOAuth2User) {
                CustomOAuth2User oauth2User = (CustomOAuth2User) authentication.getPrincipal();
                usuario = oauth2User.getUsuario();
                username = usuario.getEmail();
                log.debug("Login OAuth2 exitoso para: {}", username);
            } else if (authentication.getPrincipal() instanceof Usuario) {
                usuario = (Usuario) authentication.getPrincipal();
                username = usuario.getUsername();
                log.debug("Login tradicional exitoso para: {}", username);
            } else {
                // Buscar usuario por username para login tradicional
                usuario = usuarioService.obtenerUsuarioPorUsername(username).orElse(null);
                log.debug("Login tradicional (fallback) para: {}", username);
            }

            // Actualizar último acceso
            if (usuario != null) {
                usuarioService.guardarUsuario(usuario);
            }

        } catch (Exception e) {
            log.error("Error actualizando último acceso para {}: {}", username, e.getMessage());
        }

        String redirectUrl = determinarUrlRedireccion(authentication);
        log.debug("Redirigiendo usuario {} a: {}", username, redirectUrl);
        response.sendRedirect(redirectUrl);
    }

    private String determinarUrlRedireccion(Authentication authentication) {
        boolean esAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean esVendedor = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_VENDEDOR"));

        if (esAdmin) {
            return "/dashboard?welcome=admin";
        } else if (esVendedor) {
            return "/ventas?welcome=vendedor";
        } else {
            return "/dashboard?welcome=empleado";
        }
    }
}
