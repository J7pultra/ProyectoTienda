///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.boutique.uniformes.controller;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.util.Locale;
//
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import org.springframework.web.bind.annotation.PostMapping;
//
//@Controller
//public class RegistroController {
//
////    @GetMapping("/registro/alt")
//    public String registro(
//            @RequestParam(value = "lang", required = false) String lang,
//            Model model,
//
//            HttpServletRequest request) {
//        
//        // Configurar idioma
//        if (lang != null) {
//            Locale locale = new Locale(lang);
//            request.getSession().setAttribute("org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE", locale);
//        }
//        
//        // Crear objeto usuario vacío para el formulario
//        model.addAttribute("usuario", new Object());
//        
//        return "auth/registro";
//    }
//
//    @PostMapping("/registro")
//    public String procesarRegistro(
//            // Aquí procesarías el formulario
//            RedirectAttributes redirectAttributes) {
//        
//        try {
//            // Lógica para guardar el usuario
//            redirectAttributes.addFlashAttribute("success", "Usuario registrado exitosamente");
//            return "redirect:/login";
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("error", "Error al registrar usuario: " + e.getMessage());
//            return "redirect:/registro";
//        }
//    }
//}
