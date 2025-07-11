package com.boutique.uniformes.controller;

import com.boutique.uniformes.domain.Empleado;
import com.boutique.uniformes.service.EmpleadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    @GetMapping
    public String lista(@RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Empleado> empleados = empleadoService.obtenerEmpleadosPaginados(pageable);
        model.addAttribute("empleados", empleados);
        model.addAttribute("empleado", new Empleado()); // Para el modal
        return "empleados/empleados";
    }

    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String nuevo(Model model) {
        model.addAttribute("empleado", new Empleado());
        model.addAttribute("titulo", "Registrar Empleado");
        Page<Empleado> empleados = empleadoService.obtenerEmpleadosPaginados(PageRequest.of(0, 10));
        model.addAttribute("empleados", empleados);
        return "empleados/empleados";
    }

    @PostMapping("/guardar")
    @PreAuthorize("hasRole('ADMIN')")
    public String guardar(@Valid @ModelAttribute Empleado empleado,
                          BindingResult result,
                          RedirectAttributes redirectAttributes,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Registrar Empleado");
            Page<Empleado> empleados = empleadoService.obtenerEmpleadosPaginados(PageRequest.of(0, 10));
            model.addAttribute("empleados", empleados);
            return "empleados/empleados";
        }
        try {
            empleadoService.guardarEmpleado(empleado);
            redirectAttributes.addFlashAttribute("success", "Empleado registrado exitosamente");
            return "redirect:/empleados";
        } catch (Exception e) {
            model.addAttribute("titulo", "Registrar Empleado");
            model.addAttribute("error", e.getMessage());
            Page<Empleado> empleados = empleadoService.obtenerEmpleadosPaginados(PageRequest.of(0, 10));
            model.addAttribute("empleados", empleados);
            return "empleados/empleados";
        }
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Empleado empleado = empleadoService.obtenerEmpleadoPorId(id);
            model.addAttribute("empleado", empleado);
            model.addAttribute("titulo", "Editar Empleado");
            Page<Empleado> empleados = empleadoService.obtenerEmpleadosPaginados(PageRequest.of(0, 10));
            model.addAttribute("empleados", empleados);
            return "empleados/empleados";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/empleados";
        }
    }

    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            empleadoService.eliminarEmpleado(id);
            redirectAttributes.addFlashAttribute("success", "Empleado eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/empleados";
    }

    @GetMapping("/buscar")
    @ResponseBody
    public java.util.List<Empleado> buscarEmpleados(@RequestParam String query) {
        return empleadoService.busquedaInteligente(query);
    }

    @GetMapping("/asistencia/{cedula}")
    @ResponseBody
    public Empleado buscarPorCedulaParaAsistencia(@PathVariable String cedula) {
        return empleadoService.buscarPorCedulaParaAsistencia(cedula);
    }
}
