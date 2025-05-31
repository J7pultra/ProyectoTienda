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
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/asistencias")
public class AsistenciaController {

    /**
     * Lista de asistencias con filtros y paginación
     */
    @GetMapping
    public String lista(
            Model model,
            @RequestParam(required = false) Long empleadoId,
            @RequestParam(defaultValue = "") String fechaInicio,
            @RequestParam(defaultValue = "") String fechaFin,
            @RequestParam(defaultValue = "") String estado,
            @RequestParam(defaultValue = "fecha") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        // Crear paginación vacía por ahora
        Pageable pageable = PageRequest.of(page, size);
        Page<Object> asistencias = new PageImpl<>(Collections.emptyList(), pageable, 0);
        
        // Estadísticas del día
        model.addAttribute("presentesHoy", 8);
        model.addAttribute("ausentesHoy", 2);
        model.addAttribute("tardanzasHoy", 1);
        model.addAttribute("porcentajeAsistencia", "80%");
        
        // Datos para filtros
        model.addAttribute("asistencias", asistencias);
        model.addAttribute("empleadoId", empleadoId);
        model.addAttribute("fechaInicio", fechaInicio.isEmpty() ? LocalDate.now().toString() : fechaInicio);
        model.addAttribute("fechaFin", fechaFin.isEmpty() ? LocalDate.now().toString() : fechaFin);
        model.addAttribute("estado", estado);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        
        // Lista de empleados para el filtro
        model.addAttribute("empleados", createMockEmpleados());
        
        return "asistencias/lista";
    }

    /**
     * Formulario para nueva asistencia
     */
    @GetMapping("/nueva")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String nueva(Model model) {
        Object asistencia = createMockAsistencia(null);
        
        model.addAttribute("asistencia", asistencia);
        model.addAttribute("titulo", "Registrar Asistencia");
        model.addAttribute("empleados", createMockEmpleados());
        
        return "asistencias/formulario";
    }

    /**
     * Formulario para editar asistencia
     */
    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Object asistencia = createMockAsistencia(id);
            
            model.addAttribute("asistencia", asistencia);
            model.addAttribute("titulo", "Editar Asistencia");
            model.addAttribute("empleados", createMockEmpleados());
            
            return "asistencias/formulario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Asistencia no encontrada");
            return "redirect:/asistencias";
        }
    }

    /**
     * Ver detalle de la asistencia
     */
    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Object asistencia = createMockAsistencia(id);
            
            model.addAttribute("asistencia", asistencia);
            
            return "asistencias/detalle";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Asistencia no encontrada");
            return "redirect:/asistencias";
        }
    }

    /**
     * Guardar asistencia (nueva o editada)
     */
    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String guardar(
            @ModelAttribute Object asistencia,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Aquí implementarías la lógica para guardar
            // asistenciaService.guardar(asistencia);
            
            redirectAttributes.addFlashAttribute("success", "Asistencia registrada exitosamente");
            return "redirect:/asistencias";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar la asistencia: " + e.getMessage());
            return "redirect:/asistencias/nueva";
        }
    }

    /**
     * Marcar salida
     */
    @PostMapping("/marcar-salida/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String marcarSalida(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Aquí implementarías la lógica para marcar salida
            // asistenciaService.marcarSalida(id, LocalTime.now());
            
            redirectAttributes.addFlashAttribute("success", "Salida marcada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al marcar salida: " + e.getMessage());
        }
        
        return "redirect:/asistencias/ver/" + id;
    }

    /**
     * Eliminar asistencia
     */
    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Aquí implementarías la lógica para eliminar
            // asistenciaService.eliminar(id);
            
            redirectAttributes.addFlashAttribute("success", "Asistencia eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la asistencia: " + e.getMessage());
        }
        
        return "redirect:/asistencias";
    }

    /**
     * Exportar asistencias
     */
    @GetMapping("/exportar")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<byte[]> exportar() {
        // Implementar exportación
        return ResponseEntity.ok().build();
    }

    /**
     * Generar reporte de asistencias
     */
    @GetMapping("/reporte")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<byte[]> reporte() {
        // Implementar generación de reportes
        return ResponseEntity.ok().build();
    }

    /**
     * API: Verificar asistencia existente
     */
    @GetMapping("/api/check")
    @ResponseBody
    public Object checkAsistencia(
            @RequestParam Long empleadoId, 
            @RequestParam String fecha) {
        
        // Simular verificación
        boolean exists = false; // asistenciaService.existeAsistencia(empleadoId, fecha);
        
        return new Object() {
            public boolean isExists() { return exists; }
            public Long getAsistenciaId() { return exists ? 1L : null; }
        };
    }

    /**
     * API: Obtener empleados activos
     */
    @GetMapping("/api/empleados/activos")
    @ResponseBody
    public List<Object> getEmpleadosActivos() {
        return createMockEmpleados();
    }

    /**
     * API: Obtener estadísticas de asistencia
     */
    @GetMapping("/api/stats/{empleadoId}")
    @ResponseBody
    public Object getAsistenciaStats(@PathVariable Long empleadoId) {
        return new Object() {
            public int getDiasPresentes() { return 20; }
            public int getTardanzas() { return 2; }
            public int getAusencias() { return 1; }
            public String getPorcentajeAsistencia() { return "91%"; }
        };
    }

    /**
     * Métodos auxiliares para crear datos simulados
     */
    private Object createMockAsistencia(Long id) {
        return new Object() {
            public Long getId() { return id != null ? id : 1L; }
            public LocalDate getFecha() { return LocalDate.now(); }
            public LocalTime getHoraEntrada() { return LocalTime.of(8, 0); }
            public LocalTime getHoraSalida() { return LocalTime.of(17, 0); }
            public String getEstado() { return "PRESENTE"; }
            public String getObservaciones() { return ""; }
            public Object getEmpleado() { return createMockEmpleado(); }
        };
    }

    private Object createMockEmpleado() {
        return new Object() {
            public Long getId() { return 1L; }
            public String getNombre() { return "Ana"; }
            public String getApellido() { return "García"; }
            public String getNombreCompleto() { return "Ana García"; }
            public String getDocumento() { return "12345678"; }
            public String getCargo() { return "Vendedora"; }
            public String getEmail() { return "ana@boutique.com"; }
            public boolean isActivo() { return true; }
        };
    }

    private List<Object> createMockEmpleados() {
        List<Object> empleados = new ArrayList<>();
        
        empleados.add(createMockEmpleado());
        
        empleados.add(new Object() {
            public Long getId() { return 2L; }
            public String getNombre() { return "Carlos"; }
            public String getApellido() { return "Rodríguez"; }
            public String getNombreCompleto() { return "Carlos Rodríguez"; }
            public String getDocumento() { return "87654321"; }
            public String getCargo() { return "Supervisor"; }
            public String getEmail() { return "carlos@boutique.com"; }
            public boolean isActivo() { return true; }
        });
        
        empleados.add(new Object() {
            public Long getId() { return 3L; }
            public String getNombre() { return "María"; }
            public String getApellido() { return "López"; }
            public String getNombreCompleto() { return "María López"; }
            public String getDocumento() { return "11223344"; }
            public String getCargo() { return "Cajera"; }
            public String getEmail() { return "maria@boutique.com"; }
            public boolean isActivo() { return true; }
        });
        
        return empleados;
    }
}
