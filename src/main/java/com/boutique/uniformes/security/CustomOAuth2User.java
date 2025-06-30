package com.boutique.uniformes.security;

import com.boutique.uniformes.domain.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {
    
    private final OAuth2User oauth2User;
    private final Usuario usuario;

    public CustomOAuth2User(OAuth2User oauth2User, Usuario usuario) {
        this.oauth2User = oauth2User;
        this.usuario = usuario;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oauth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return usuario.getAuthorities();
    }

    @Override
    public String getName() {
        return oauth2User.getAttribute("email");
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String getEmail() {
        return oauth2User.getAttribute("email");
    }

    public String getPicture() {
        return oauth2User.getAttribute("picture");
    }
}
