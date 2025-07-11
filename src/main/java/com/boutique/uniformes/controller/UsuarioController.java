package com.boutique.uniformes.controller;

import com.boutique.uniformes.domain.Usuario;
import com.boutique.uniformes.service.UsuarioService;
import com.boutique.uniformes.repository.RolRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final RolRepository rolRepository;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String listarUsuarios(Model model, Pageable pageable) {
        Page<Usuario> usuarios = usuarioService.obtenerUsuariosPaginados(pageable);
        model.addAttribute("usuarios", usuarios);
        return "admin/usuarios/lista";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolRepository.findAll());
        return "admin/usuarios/formulario";
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String guardarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
                                 BindingResult result,
                                 @RequestParam List<Long> roles,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("roles", rolRepository.findAll());
            return "admin/usuarios/formulario";
        }
        usuario.setRoles(rolRepository.findAllById(roles));
        usuarioService.guardarUsuario(usuario);
        redirectAttributes.addFlashAttribute("success", "Usuario guardado correctamente");
        return "redirect:/usuarios";
    }

    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        usuarioService.eliminarUsuario(id);
        redirectAttributes.addFlashAttribute("success", "Usuario eliminado correctamente");
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','EMPLEADO')")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", rolRepository.findAll());
        return "admin/usuarios/formulario";
    }

    @PostMapping("/cambiar-estado/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String cambiarEstadoUsuario(@PathVariable Long id, @RequestParam Boolean activo, RedirectAttributes redirectAttributes) {
        usuarioService.cambiarEstadoUsuario(id, activo);
        redirectAttributes.addFlashAttribute("success", "Estado de usuario actualizado correctamente");
        return "redirect:/usuarios";
    }
}
