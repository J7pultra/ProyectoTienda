/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.controller;


import com.boutique.uniformes.domain.Empleado;
import com.boutique.uniformes.domain.Venta;
import com.boutique.uniformes.service.ClienteService;
import com.boutique.uniformes.service.EmpleadoService;
import com.boutique.uniformes.service.UniformeService;
import com.boutique.uniformes.service.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@RequestMapping("/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;
    private final ClienteService clienteService;
    private final EmpleadoService empleadoService;
    private final UniformeService uniformeService;

    @GetMapping
    public String listarVentas(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "fechaVenta") String sortBy,
                              @RequestParam(defaultValue = "desc") String sortDir,
                              @RequestParam(required = false) String buscar,
                              @RequestParam(required = false) LocalDate fechaInicio,
                              @RequestParam(required = false) LocalDate fechaFin,
                              Model model) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Venta> ventas;
        
        if (fechaInicio != null && fechaFin != null) {
            ventas = ventaService.obtenerVentasEntreFechas(fechaInicio, fechaFin, pageable);
            model.addAttribute("fechaInicio", fechaInicio);
            model.addAttribute("fechaFin", fechaFin);
        } else if (buscar != null && !buscar.trim().isEmpty()) {
            ventas = ventaService.buscarVentas(buscar, pageable);
            model.addAttribute("buscar", buscar);
        } else {
            ventas = ventaService.obtenerVentasPaginadas(pageable);
        }
        
        model.addAttribute("ventas", ventas);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        
        return "ventas/lista";
    }

    @GetMapping("/nueva")
    public String mostrarFormularioNueva(Model model) {
        Venta venta = new Venta();
        venta.setDetalles(new ArrayList<>());
        
        model.addAttribute("venta", venta);
        model.addAttribute("clientes", clienteService.obtenerClientesActivos());
        model.addAttribute("empleados", empleadoService.obtenerEmpleadosActivos());
        model.addAttribute("uniformes", uniformeService.obtenerUniformesDisponibles());
        model.addAttribute("titulo", "Nueva Venta");
        
        return "ventas/factura";
    }

    @PostMapping("/guardar")
    public String guardarVenta(@Valid @ModelAttribute("venta") Venta venta,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.obtenerClientesActivos());
            model.addAttribute("empleados", empleadoService.obtenerEmpleadosActivos());
            model.addAttribute("uniformes", uniformeService.obtenerUniformesDisponibles());
            model.addAttribute("titulo", "Nueva Venta");
            return "ventas/factura";
        }

        try {
            Venta ventaGuardada = ventaService.procesarVenta(venta);
            redirectAttributes.addFlashAttribute("success", 
                "Venta procesada exitosamente. Factura: " + ventaGuardada.getNumeroFactura());
            return "redirect:/ventas/ver/" + ventaGuardada.getId();
        } catch (Exception e) {
            model.addAttribute("error", "Error al procesar venta: " + e.getMessage());
            model.addAttribute("clientes", clienteService.obtenerClientesActivos());
            model.addAttribute("empleados", empleadoService.obtenerEmpleadosActivos());
            model.addAttribute("uniformes", uniformeService.obtenerUniformesDisponibles());
            model.addAttribute("titulo", "Nueva Venta");
            return "ventas/factura";
        }
    }

    @GetMapping("/ver/{id}")
    public String verVenta(@PathVariable Long id, Model model) {
        Venta venta = ventaService.obtenerVentaPorId(id);
        model.addAttribute("venta", venta);
        return "ventas/detalle";
    }

    @GetMapping("/factura/{id}")
    public String imprimirFactura(@PathVariable Long id, Model model) {
        Venta venta = ventaService.obtenerVentaPorId(id);
        model.addAttribute("venta", venta);
        return "ventas/factura";
    }

    @PostMapping("/cancelar/{id}")
    public String cancelarVenta(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            ventaService.cancelarVenta(id);
            redirectAttributes.addFlashAttribute("success", "Venta cancelada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cancelar venta: " + e.getMessage());
        }
        return "redirect:/ventas";
    }
    


@PostMapping("/api/ventas/guardar")
@ResponseBody
public ResponseEntity<Map<String, Object>> guardarFactura(@RequestBody Venta venta) {
    try {
        // Obtener usuario actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        
        // Buscar empleado por email/username si tienes esa relación
        // Si no, puedes usar un empleado por defecto o el primer empleado activo
        List<Empleado> empleados = empleadoService.obtenerEmpleadosActivos();
        if (!empleados.isEmpty()) {
            venta.setVendedor(empleados.get(0)); // Usar primer empleado como vendedor por defecto
        }
        
        Venta ventaGuardada = ventaService.procesarVenta(venta);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Factura guardada exitosamente");
        response.put("numeroFactura", ventaGuardada.getNumeroFactura());
        response.put("id", ventaGuardada.getId());
        
        return ResponseEntity.ok(response);
    } catch (Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "Error al guardar la factura: " + e.getMessage());
        
        return ResponseEntity.badRequest().body(response);
    }
}

}
