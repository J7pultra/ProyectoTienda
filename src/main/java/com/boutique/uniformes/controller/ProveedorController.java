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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    /**
     * Lista de proveedores con filtros y paginación
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String lista(
            Model model,
            @RequestParam(defaultValue = "") String buscar,
            @RequestParam(defaultValue = "") String ciudad,
            @RequestParam(defaultValue = "") String estado,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        // Crear paginación vacía por ahora
        Pageable pageable = PageRequest.of(page, size);
        Page<Object> proveedores = new PageImpl<>(Collections.emptyList(), pageable, 0);
        
        // Estadísticas para las tarjetas superiores
        model.addAttribute("totalProveedores", 8);
        model.addAttribute("proveedoresActivos", 6);
        model.addAttribute("proveedoresInactivos", 2);
        model.addAttribute("nuevosProveedores", 1);
        
        // Datos para filtros
        model.addAttribute("proveedores", proveedores);
        model.addAttribute("buscar", buscar);
        model.addAttribute("ciudad", ciudad);
        model.addAttribute("estado", estado);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        
        // Lista de ciudades para el filtro
        model.addAttribute("ciudades", createMockCiudades());
        
        return "proveedores/lista";
    }

    /**
     * Formulario para nuevo proveedor
     */
    @GetMapping("/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String nuevo(Model model) {
        Object proveedor = createMockProveedor(null);
        
        model.addAttribute("proveedor", proveedor);
        model.addAttribute("titulo", "Nuevo Proveedor");
        model.addAttribute("ciudades", createMockCiudades());
        model.addAttribute("categorias", createMockCategorias());
        
        return "proveedores/formulario";
    }

    /**
     * Formulario para editar proveedor
     */
    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Object proveedor = createMockProveedor(id);
            
            model.addAttribute("proveedor", proveedor);
            model.addAttribute("titulo", "Editar Proveedor");
            model.addAttribute("ciudades", createMockCiudades());
            model.addAttribute("categorias", createMockCategorias());
            
            return "proveedores/formulario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Proveedor no encontrado");
            return "redirect:/proveedores";
        }
    }

    /**
     * Ver detalle del proveedor
     */
    @GetMapping("/ver/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String ver(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Object proveedor = createMockProveedor(id);
            
            model.addAttribute("proveedor", proveedor);
            model.addAttribute("productosProveedor", createMockProductosProveedor());
            model.addAttribute("comprasRecientes", Collections.emptyList());
            
            return "proveedores/detalle";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Proveedor no encontrado");
            return "redirect:/proveedores";
        }
    }

    /**
     * Guardar proveedor (nuevo o editado)
     */
    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String guardar(
            @ModelAttribute Object proveedor,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Aquí implementarías la lógica para guardar
            // proveedorService.guardar(proveedor);
            
            redirectAttributes.addFlashAttribute("success", "Proveedor guardado exitosamente");
            return "redirect:/proveedores";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el proveedor: " + e.getMessage());
            return "redirect:/proveedores/nuevo";
        }
    }

    /**
     * Cambiar estado del proveedor (activar/desactivar)
     */
    @PostMapping("/cambiar-estado/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String cambiarEstado(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Aquí implementarías la lógica para cambiar estado
            // proveedorService.cambiarEstado(id);
            
            redirectAttributes.addFlashAttribute("success", "Estado del proveedor actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar el estado: " + e.getMessage());
        }
        
        return "redirect:/proveedores";
    }

    /**
     * Eliminar proveedor
     */
    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Aquí implementarías la lógica para eliminar
            // proveedorService.eliminar(id);
            
            redirectAttributes.addFlashAttribute("success", "Proveedor eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el proveedor: " + e.getMessage());
        }
        
        return "redirect:/proveedores";
    }

    /**
     * Exportar proveedores
     */
    @GetMapping("/exportar")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<byte[]> exportar() {
        // Implementar exportación
        return ResponseEntity.ok().build();
    }

    /**
     * Generar reporte de proveedores
     */
    @GetMapping("/reporte")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<byte[]> reporte() {
        // Implementar generación de reportes
        return ResponseEntity.ok().build();
    }

    /**
     * API: Validar NIT único
     */
    @GetMapping("/api/validate-nit")
    @ResponseBody
    public Object validateNit(@RequestParam String nit) {
        // Simular validación
        boolean exists = false; // proveedorService.existePorNit(nit);
        
        return new Object() {
            public boolean isExists() { return exists; }
        };
    }

    /**
     * API: Obtener proveedores activos
     */
    @GetMapping("/api/activos")
    @ResponseBody
    public List<Object> getProveedoresActivos() {
        return createMockProveedoresActivos();
    }

    /**
     * API: Buscar proveedores por término
     */
    @GetMapping("/api/buscar")
    @ResponseBody
    public List<Object> buscarProveedores(@RequestParam String q) {
        // Simular búsqueda
        return createMockProveedoresActivos();
    }

    /**
     * Métodos auxiliares para crear datos simulados
     */
    private Object createMockProveedor(Long id) {
        return new Object() {
            public Long getId() { return id != null ? id : 1L; }
            public String getNombre() { return "Textiles Colombia S.A.S"; }
            public String getNit() { return "900.123.456-7"; }
            public String getEmail() { return "ventas@textilescolombia.com"; }
            public String getTelefono() { return "+57 (1) 234-5678"; }
            public String getDireccion() { return "Calle Industrial #123-45"; }
            public String getCiudad() { return "Bogotá"; }
            public String getContactoPrincipal() { return "María González"; }
            public String getTelefonoContacto() { return "+57 300 123 4567"; }
            public String getCategoria() { return "Textiles"; }
            public String getSitioWeb() { return "www.textilescolombia.com"; }
            public boolean isActivo() { return true; }
            public LocalDateTime getFechaRegistro() { return LocalDateTime.now().minusMonths(3); }
            public String getObservaciones() { return "Proveedor confiable con entregas puntuales"; }
        };
    }

    private List<String> createMockCiudades() {
        List<String> ciudades = new ArrayList<>();
        ciudades.add("Bogotá");
        ciudades.add("Medellín");
        ciudades.add("Cali");
        ciudades.add("Barranquilla");
        ciudades.add("Cartagena");
        ciudades.add("Bucaramanga");
        return ciudades;
    }

    private List<String> createMockCategorias() {
        List<String> categorias = new ArrayList<>();
        categorias.add("Textiles");
        categorias.add("Confecciones");
        categorias.add("Accesorios");
        categorias.add("Calzado");
        categorias.add("Materiales");
        categorias.add("Insumos");
        return categorias;
    }

    private List<Object> createMockProveedoresActivos() {
        List<Object> proveedores = new ArrayList<>();
        
        proveedores.add(createMockProveedor(1L));
        
        proveedores.add(new Object() {
            public Long getId() { return 2L; }
            public String getNombre() { return "Confecciones del Norte Ltda"; }
            public String getNit() { return "800.987.654-3"; }
            public String getContactoPrincipal() { return "Carlos Martínez"; }
            public String getCiudad() { return "Medellín"; }
            public boolean isActivo() { return true; }
        });
        
        proveedores.add(new Object() {
            public Long getId() { return 3L; }
            public String getNombre() { return "Uniformes Escolares S.A"; }
            public String getNit() { return "900.555.777-1"; }
            public String getContactoPrincipal() { return "Ana Rodríguez"; }
            public String getCiudad() { return "Cali"; }
            public boolean isActivo() { return true; }
        });
        
        return proveedores;
    }

    private List<Object> createMockProductosProveedor() {
        List<Object> productos = new ArrayList<>();
        
        productos.add(new Object() {
            public String getNombre() { return "Camisa Escolar Blanca"; }
            public String getCodigo() { return "CAM-001"; }
            public String getCategoria() { return "Camisas"; }
            public String getEstado() { return "Activo"; }
        });
        
        productos.add(new Object() {
            public String getNombre() { return "Pantalón Escolar Azul"; }
            public String getCodigo() { return "PAN-001"; }
            public String getCategoria() { return "Pantalones"; }
            public String getEstado() { return "Activo"; }
        });
        
        return productos;
    }
}

