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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

@Controller
@RequestMapping("/uniformes")
public class UniformeController {

    /**
     * Lista de uniformes con filtros y paginación
     */
    @GetMapping
    public String lista(
            Model model,
            @RequestParam(defaultValue = "") String buscar,
            @RequestParam(defaultValue = "") String categoria,
            @RequestParam(defaultValue = "") String talla,
            @RequestParam(defaultValue = "") String estado,
            @RequestParam(defaultValue = "false") boolean bajoStock,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        
        // Crear paginación vacía por ahora
        Pageable pageable = PageRequest.of(page, size);
        Page<Object> uniformes = new PageImpl<>(Collections.emptyList(), pageable, 0);
        
        // Estadísticas para las tarjetas superiores
        model.addAttribute("totalUniformes", 0);
        model.addAttribute("uniformesActivos", 0);
        model.addAttribute("uniformesBajoStock", 0);
        model.addAttribute("categorias", 5);
        
        // Datos para filtros
        model.addAttribute("uniformes", uniformes);
        model.addAttribute("buscar", buscar);
        model.addAttribute("categoria", categoria);
        model.addAttribute("talla", talla);
        model.addAttribute("estado", estado);
        model.addAttribute("bajoStock", bajoStock);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        
        // Listas para filtros
        model.addAttribute("categorias", createMockCategorias());
        model.addAttribute("tallas", createMockTallas());
        
        return "uniformes/lista";
    }

    /**
     * Formulario para nuevo uniforme
     */
    @GetMapping("/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String nuevo(Model model) {
        Object uniforme = createMockUniforme(null);
        
        model.addAttribute("uniforme", uniforme);
        model.addAttribute("titulo", "Nuevo Uniforme");
        
        // Listas para los selects
        model.addAttribute("categorias", createMockCategorias());
        model.addAttribute("tallas", createMockTallas());
        model.addAttribute("colores", createMockColores());
        model.addAttribute("proveedores", createMockProveedores());
        
        return "uniformes/formulario";
    }

    /**
     * Formulario para editar uniforme
     */
    @GetMapping("/editar/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Object uniforme = createMockUniforme(id);
            
            model.addAttribute("uniforme", uniforme);
            model.addAttribute("titulo", "Editar Uniforme");
            model.addAttribute("categorias", createMockCategorias());
            model.addAttribute("tallas", createMockTallas());
            model.addAttribute("colores", createMockColores());
            model.addAttribute("proveedores", createMockProveedores());
            
            return "uniformes/formulario";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Uniforme no encontrado");
            return "redirect:/uniformes";
        }
    }

    /**
     * Ver detalle del uniforme
     */
    @GetMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Object uniforme = createMockUniforme(id);
            
            model.addAttribute("uniforme", uniforme);
            model.addAttribute("ventasRecientes", Collections.emptyList());
            model.addAttribute("stockHistorial", Collections.emptyList());
            
            return "uniformes/detalle";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Uniforme no encontrado");
            return "redirect:/uniformes";
        }
    }

    /**
     * Guardar uniforme (nuevo o editado)
     */
    @PostMapping("/guardar")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String guardar(
            @ModelAttribute Object uniforme,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Aquí implementarías la lógica para guardar
            // uniformeService.guardar(uniforme);
            
            redirectAttributes.addFlashAttribute("success", "Uniforme guardado exitosamente");
            return "redirect:/uniformes";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al guardar el uniforme: " + e.getMessage());
            return "redirect:/uniformes/nuevo";
        }
    }

    /**
     * Eliminar uniforme
     */
    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            // Aquí implementarías la lógica para eliminar
            // uniformeService.eliminar(id);
            
            redirectAttributes.addFlashAttribute("success", "Uniforme eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el uniforme: " + e.getMessage());
        }
        
        return "redirect:/uniformes";
    }

    /**
     * Actualizar stock del uniforme
     */
    @PostMapping("/actualizar-stock/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String actualizarStock(
            @PathVariable Long id,
            @RequestParam int nuevoStock,
            @RequestParam(required = false) String motivo,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Aquí implementarías la lógica para actualizar stock
            // uniformeService.actualizarStock(id, nuevoStock, motivo);
            
            redirectAttributes.addFlashAttribute("success", "Stock actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar stock: " + e.getMessage());
        }
        
        return "redirect:/uniformes/ver/" + id;
    }

    /**
     * Exportar uniformes
     */
    @GetMapping("/exportar")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<byte[]> exportar() {
        // Implementar exportación
        return ResponseEntity.ok().build();
    }

    /**
     * Generar código de barras
     */
    @GetMapping("/codigo-barras/{id}")
    public ResponseEntity<byte[]> generarCodigoBarras(@PathVariable Long id) {
        // Implementar generación de código de barras
        return ResponseEntity.ok().build();
    }

    /**
     * API: Obtener uniformes disponibles
     */
    @GetMapping("/api/disponibles")
    @ResponseBody
    public List<Object> getUniformesDisponibles() {
        return createMockUniformesDisponibles();
    }

    /**
     * API: Buscar uniformes por término
     */
    @GetMapping("/api/buscar")
    @ResponseBody
    public List<Object> buscarUniformes(@RequestParam String q) {
        // Simular búsqueda
        return createMockUniformesDisponibles();
    }

    /**
     * API: Validar código único
     */
    @GetMapping("/api/validate-codigo")
    @ResponseBody
    public Object validateCodigo(@RequestParam String codigo) {
        // Simular validación
        boolean exists = false; // uniformeService.existePorCodigo(codigo);
        
        return new Object() {
            public boolean isExists() { return exists; }
        };
    }

    /**
     * Métodos auxiliares para crear datos simulados
     */
    private Object createMockUniforme(Long id) {
        return new Object() {
            public Long getId() { return id != null ? id : 1L; }
            public String getNombre() { return "Camisa Escolar Blanca"; }
            public String getCodigo() { return "CAM-001"; }
            public String getDescripcion() { return "Camisa escolar de alta calidad, tela 100% algodón"; }
            public String getCategoria() { return "Camisas"; }
            public String getTalla() { return "M"; }
            public String getColor() { return "Blanco"; }
            public BigDecimal getPrecio() { return BigDecimal.valueOf(50000); }
            public BigDecimal getPrecioCompra() { return BigDecimal.valueOf(30000); }
            public int getStock() { return 25; }
            public int getStockMinimo() { return 5; }
            public boolean isActivo() { return true; }
            public String getMarca() { return "Uniformes Premium"; }
            public String getMaterial() { return "100% Algodón"; }
            public Object getProveedor() { return createMockProveedor(); }
        };
    }

    private Object createMockProveedor() {
        return new Object() {
            public Long getId() { return 1L; }
            public String getNombre() { return "Textiles Colombia S.A.S"; }
            public String getNit() { return "900.123.456-7"; }
        };
    }

    private List<String> createMockCategorias() {
        List<String> categorias = new ArrayList<>();
        categorias.add("Camisas");
        categorias.add("Pantalones");
        categorias.add("Faldas");
        categorias.add("Zapatos");
        categorias.add("Accesorios");
        categorias.add("Deportivo");
        return categorias;
    }

    private List<String> createMockTallas() {
        List<String> tallas = new ArrayList<>();
        tallas.add("XS");
        tallas.add("S");
        tallas.add("M");
        tallas.add("L");
        tallas.add("XL");
        tallas.add("XXL");
        return tallas;
    }

    private List<String> createMockColores() {
        List<String> colores = new ArrayList<>();
        colores.add("Blanco");
        colores.add("Azul");
        colores.add("Gris");
        colores.add("Negro");
        colores.add("Verde");
        colores.add("Rojo");
        return colores;
    }

    private List<Object> createMockProveedores() {
        List<Object> proveedores = new ArrayList<>();
        proveedores.add(createMockProveedor());
        return proveedores;
    }

    private List<Object> createMockUniformesDisponibles() {
        List<Object> uniformes = new ArrayList<>();
        uniformes.add(createMockUniforme(1L));
        uniformes.add(new Object() {
            public Long getId() { return 2L; }
            public String getNombre() { return "Pantalón Escolar Azul"; }
            public String getCodigo() { return "PAN-001"; }
            public BigDecimal getPrecio() { return BigDecimal.valueOf(75000); }
            public int getStock() { return 15; }
            public String getCategoria() { return "Pantalones"; }
            public String getTalla() { return "M"; }
        });
        return uniformes;
    }
}
