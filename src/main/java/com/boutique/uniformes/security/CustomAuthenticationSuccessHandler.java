package com.boutique.uniformes.security;

import com.boutique.uniformes.domain.Usuario;
import com.boutique.uniformes.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;


@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioService usuarioService;

    // Usar @Lazy para romper la dependencia circular
    @Autowired
    public CustomAuthenticationSuccessHandler(@Lazy UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
        
        String username = authentication.getName();
        
        try {
            Usuario usuario = usuarioService.obtenerUsuarioPorUsername(username).orElse(null);
            if (usuario != null) {
                usuario.setUltimoAcceso(LocalDateTime.now());
                usuarioService.guardarUsuario(usuario);
                log.debug("Último acceso actualizado para usuario: {}", username);
            }
        } catch (Exception e) {
            log.error("Error actualizando último acceso para {}: {}", username, e.getMessage());
        } // ✅ BLOQUE CATCH CERRADO CORRECTAMENTE

        String redirectUrl = determinarUrlRedireccion(authentication);
        log.debug("Redirigiendo usuario {} a: {}", username, redirectUrl);
        response.sendRedirect(redirectUrl);
    } // ✅ MÉTODO onAuthenticationSuccess CERRADO CORRECTAMENTE

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
    } // ✅ MÉTODO determinarUrlRedireccion CERRADO CORRECTAMENTE

} // ✅ CLASE CERRADA CORRECTAMENTE
