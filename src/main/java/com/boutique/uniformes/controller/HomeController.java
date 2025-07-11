/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class HomeController {

    /**
     * Página principal - redirige al dashboard
     */
    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    /**
     * Página de perfil del usuario
     */
    @GetMapping("/perfil")
    public String perfil(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        model.addAttribute("usuario", username);
        model.addAttribute("titulo", "Mi Perfil");
        
        return "auth/perfil";
    }

    /**
     * Página de configuración del sistema
     */
    @GetMapping("/configuracion")
    public String configuracion(Model model) {
        model.addAttribute("titulo", "Configuración del Sistema");
        return "admin/configuracion";
    }

    /**
     * Página de ayuda
     */
    @GetMapping("/ayuda")
    public String ayuda(Model model) {
        model.addAttribute("titulo", "Centro de Ayuda");
        return "ayuda/index";
    }

    /**
     * Página de búsqueda global
     */
    @GetMapping("/buscar")
    public String buscar(Model model, String q) {
        model.addAttribute("query", q != null ? q : "");
        model.addAttribute("resultados", java.util.Collections.emptyList());
        model.addAttribute("titulo", "Resultados de Búsqueda");
        
        return "busqueda/resultados";
    }
}
