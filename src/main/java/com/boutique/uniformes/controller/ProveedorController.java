package com.boutique.uniformes.controller;

import com.boutique.uniformes.domain.Proveedor;
import com.boutique.uniformes.service.ProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    @GetMapping
    public String lista(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Proveedor> proveedores = proveedorService.obtenerProveedoresPaginados(pageable);
        model.addAttribute("proveedores", proveedores);
        model.addAttribute("proveedor", new Proveedor()); // Para el modal
        return "proveedores/proveedores";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("proveedor", new Proveedor());
        model.addAttribute("titulo", "Registrar Proveedor");
        Page<Proveedor> proveedores = proveedorService.obtenerProveedoresPaginados(PageRequest.of(0, 10));
        model.addAttribute("proveedores", proveedores);
        return "proveedores/proveedores";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute Proveedor proveedor,
                          BindingResult result,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Registrar Proveedor");
            Page<Proveedor> proveedores = proveedorService.obtenerProveedoresPaginados(PageRequest.of(0, 10));
            model.addAttribute("proveedores", proveedores);
            return "proveedores/proveedores";
        }
        try {
            proveedorService.guardarProveedor(proveedor);
            redirectAttributes.addFlashAttribute("success", "Proveedor registrado exitosamente");
            return "redirect:/proveedores";
        } catch (Exception e) {
            model.addAttribute("titulo", "Registrar Proveedor");
            model.addAttribute("error", e.getMessage());
            Page<Proveedor> proveedores = proveedorService.obtenerProveedoresPaginados(PageRequest.of(0, 10));
            model.addAttribute("proveedores", proveedores);
            return "proveedores/proveedores";
        }
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Proveedor proveedor = proveedorService.obtenerProveedorPorId(id);
            model.addAttribute("proveedor", proveedor);
            model.addAttribute("titulo", "Editar Proveedor");
            Page<Proveedor> proveedores = proveedorService.obtenerProveedoresPaginados(PageRequest.of(0, 10));
            model.addAttribute("proveedores", proveedores);
            return "proveedores/proveedores";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/proveedores";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            proveedorService.eliminarProveedor(id);
            redirectAttributes.addFlashAttribute("success", "Proveedor eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/proveedores";
    }

    @GetMapping("/buscar")
    @ResponseBody
    public java.util.List<Proveedor> buscarProveedores(@RequestParam String query) {
        return proveedorService.busquedaInteligente(query);
    }

    @GetMapping("/historial/{id}")
    public String historialComprasProveedor(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Proveedor proveedor = proveedorService.obtenerProveedorPorId(id);
            model.addAttribute("proveedor", proveedor);
            model.addAttribute("historial", proveedorService.obtenerHistorialCompras(id));
            return "proveedores/historial";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/proveedores";
        }
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Long id, @ModelAttribute Proveedor proveedor, RedirectAttributes redirectAttributes) {
        try {
            proveedor.setId(id);
            proveedorService.guardarProveedor(proveedor);
            redirectAttributes.addFlashAttribute("success", "Proveedor actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/proveedores";
    }
}
