package com.boutique.uniformes.controller;

import com.boutique.uniformes.domain.Usuario;
import com.boutique.uniformes.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                       @RequestParam(value = "logout", required = false) String logout,
                       @RequestParam(value = "expired", required = false) String expired,
                       Model model) {
        
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
        }
        if (logout != null) {
            model.addAttribute("success", "Sesión cerrada exitosamente");
        }
        if (expired != null) {
            model.addAttribute("warning", "Su sesión ha expirado");
        }
        
        return "auth/login";
    }

    @GetMapping("/registro")
    @Transactional  // ← Añade esta anotación
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "auth/registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
        
        if (result.hasErrors()) {
            return "auth/registro";
        }

        try {
            usuarioService.registrarUsuario(usuario);
            redirectAttributes.addFlashAttribute("success", 
                "Usuario registrado exitosamente. Puede iniciar sesión.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar usuario: " + e.getMessage());
            return "auth/registro";
        }
    }
}
