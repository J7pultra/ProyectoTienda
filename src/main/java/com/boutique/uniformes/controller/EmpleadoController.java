/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

    /**
     * Lista de empleados con filtros y paginación
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String lista(
            Model model,
            @RequestParam(defaultValue = "") String buscar,
            @RequestParam(defaultValue = "") String cargo,
            @RequestParam(defaultValue = "") String estado,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        // Crear paginación vacía por ahora
        Pageable pageable = PageRequest.of(page, size);
        Page<Object> empleados = new PageImpl<>(Collections.emptyList(), pageable, 0);
        
        // Estadísticas para las tarjetas superiores
        model.addAttribute("totalEmpleados", 12);
        model.addAttribute("empleadosActivos", 10);
        model.addAttribute("empleadosInactivos", 2);
        model.addAttribute("nuevosMes", 2);
        
        // Datos para filtros
        model.addAttribute("empleados", empleados);
        model.addAttribute("buscar", buscar);
        model.addAttribute("cargo", cargo);
        model.addAttribute("estado", estado);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        
        // Lista de cargos para el filtro
        model.addAttribute("cargos", createMockCargos());
        
        return "empleados/lista";
    }

    /**
     * Formulario para nuevo empleado
     */
    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public String nuevo(Model model) {
        Object empleado = createMockEmpleado(null);
        
        model.addAttribute("empleado", empleado);
        model.addAttribute("titulo", "Nuevo Empleado");
        model.addAttribute("cargos", createMockCargos());
        model.addAttribute("departamentos", createMockDepartamentos());
        
        return "empleados/formulario";
    }

    /**
     * Formulario para editar empleado
     */
    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Object empleado = createMockEmpleado(id);
            
            model.addAttribute("empleado", empleado);
            model.addAttribute("titulo", "Editar Empleado");
            model.addAttribute("cargos", createMockCargos());
            model.addAttribute("departamentos", createMockDepartamentos());
            
            return "empleados/formulario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Empleado no encontrado");
            return "redirect:/empleados";
        }
    }

    /**
     * Ver detalle del empleado
     */
    @GetMapping("/ver/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String ver(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Object empleado = createMockEmpleado(id);
            
            model.addAttribute("empleado", empleado);
            model.addAttribute("asistenciasRecientes", createMockAsistenciasRecientes());
            model.addAttribute("ventasRealizadas", Collections.emptyList());
            
            return "empleados/detalle";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Empleado no encontrado");
            return "redirect:/empleados";
        }
    }

    /**
     * Guardar empleado (nuevo o editado)
     */
    @PostMapping("/guardar")
    @PreAuthorize("hasRole('ADMIN')")
    public String guardar(
            @ModelAttribute Object empleado,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Aquí implementarías la lógica para guardar
            // empleadoService.guardar(empleado);
            
            redirectAttributes.addFlashAttribute("success", "Empleado guardado exitosamente");
            return "redirect:/empleados";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el empleado: " + e.getMessage());
            return "redirect:/empleados/nuevo";
        }
    }

    /**
     * Cambiar estado del empleado (activar/desactivar)
     */
    @PostMapping("/cambiar-estado/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String cambiarEstado(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Aquí implementarías la lógica para cambiar estado
            // empleadoService.cambiarEstado(id);
            
            redirectAttributes.addFlashAttribute("success", "Estado del empleado actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar el estado: " + e.getMessage());
        }
        
        return "redirect:/empleados";
    }

    /**
     * Eliminar empleado
     */
    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Aquí implementarías la lógica para eliminar
            // empleadoService.eliminar(id);
            
            redirectAttributes.addFlashAttribute("success", "Empleado eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el empleado: " + e.getMessage());
        }
        
        return "redirect:/empleados";
    }

    /**
     * Exportar empleados
     */
    @GetMapping("/exportar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportar() {
        // Implementar exportación
        return ResponseEntity.ok().build();
    }

    /**
     * Generar reporte de empleados
     */
    @GetMapping("/reporte")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> reporte() {
        // Implementar generación de reportes
        return ResponseEntity.ok().build();
    }

    /**
     * API: Validar documento único
     */
    @GetMapping("/api/validate-documento")
    @ResponseBody
    public Object validateDocumento(@RequestParam String documento) {
        // Simular validación
        boolean exists = false; // empleadoService.existePorDocumento(documento);
        
        return new Object() {
            public boolean isExists() { return exists; }
        };
    }

    /**
     * API: Obtener empleados activos
     */
    @GetMapping("/api/activos")
    @ResponseBody
    public List<Object> getEmpleadosActivos() {
        return createMockEmpleadosActivos();
    }

    /**
     * API: Obtener empleados por cargo
     */
    @GetMapping("/api/por-cargo/{cargo}")
    @ResponseBody
    public List<Object> getEmpleadosPorCargo(@PathVariable String cargo) {
        return createMockEmpleadosActivos();
    }

    /**
     * Métodos auxiliares para crear datos simulados
     */
    private Object createMockEmpleado(Long id) {
        return new Object() {
            public Long getId() { return id != null ? id : 1L; }
            public String getNombre() { return "Ana"; }
            public String getApellido() { return "García"; }
            public String getNombreCompleto() { return "Ana García"; }
            public String getDocumento() { return "12345678"; }
            public String getEmail() { return "ana@boutique.com"; }
            public String getTelefono() { return "123-456-7890"; }
            public String getDireccion() { return "Calle 123 #45-67"; }
            public String getCargo() { return "Vendedora"; }
            public String getDepartamento() { return "Ventas"; }
            public BigDecimal getSalario() { return BigDecimal.valueOf(1500000); }
            public LocalDate getFechaIngreso() { return LocalDate.now().minusMonths(6); }
            public boolean isActivo() { return true; }
            public String getEstadoCivil() { return "Soltero"; }
            public String getContactoEmergencia() { return "María García - 987-654-3210"; }
        };
    }

    private List<String> createMockCargos() {
        List<String> cargos = new ArrayList<>();
        cargos.add("Administrador");
        cargos.add("Vendedor");
        cargos.add("Cajero");
        cargos.add("Supervisor");
        cargos.add("Bodeguero");
        cargos.add("Contador");
        return cargos;
    }

    private List<String> createMockDepartamentos() {
        List<String> departamentos = new ArrayList<>();
        departamentos.add("Administración");
        departamentos.add("Ventas");
        departamentos.add("Bodega");
        departamentos.add("Contabilidad");
        departamentos.add("Recursos Humanos");
        return departamentos;
    }

    private List<Object> createMockEmpleadosActivos() {
        List<Object> empleados = new ArrayList<>();
        
        empleados.add(createMockEmpleado(1L));
        
        empleados.add(new Object() {
            public Long getId() { return 2L; }
            public String getNombre() { return "Carlos"; }
            public String getApellido() { return "Rodríguez"; }
            public String getNombreCompleto() { return "Carlos Rodríguez"; }
            public String getDocumento() { return "87654321"; }
            public String getCargo() { return "Supervisor"; }
            public boolean isActivo() { return true; }
        });
        
        empleados.add(new Object() {
            public Long getId() { return 3L; }
            public String getNombre() { return "María"; }
            public String getApellido() { return "López"; }
            public String getNombreCompleto() { return "María López"; }
            public String getDocumento() { return "11223344"; }
            public String getCargo() { return "Cajera"; }
            public boolean isActivo() { return true; }
        });
        
        return empleados;
    }

    private List<Object> createMockAsistenciasRecientes() {
        List<Object> asistencias = new ArrayList<>();
        
        asistencias.add(new Object() {
            public LocalDate getFecha() { return LocalDate.now(); }
            public String getEstado() { return "PRESENTE"; }
            public String getHoraEntrada() { return "08:00"; }
            public String getHoraSalida() { return "17:00"; }
        });
        
        asistencias.add(new Object() {
            public LocalDate getFecha() { return LocalDate.now().minusDays(1); }
            public String getEstado() { return "TARDANZA"; }
            public String getHoraEntrada() { return "08:15"; }
            public String getHoraSalida() { return "17:00"; }
        });
        
        return asistencias;
    }
}
