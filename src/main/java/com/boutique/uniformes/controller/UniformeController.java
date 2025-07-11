/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.controller;

import com.boutique.uniformes.domain.Uniforme;
import com.boutique.uniformes.service.UniformeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/uniformes")
@RequiredArgsConstructor
public class UniformeController {

    private final UniformeService uniformeService;

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

        return "uniformes/uniformes";
    }

    /**
     * Formulario para nuevo uniforme
     */
    @GetMapping("/nuevo")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public String nuevo(Model model) {
        Uniforme uniforme = new Uniforme();
        model.addAttribute("uniforme", uniforme);
        model.addAttribute("titulo", "Nuevo Uniforme");
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
            @ModelAttribute Uniforme uniforme,
            RedirectAttributes redirectAttributes) {

        try {
            uniformeService.guardarUniforme(uniforme);
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
    public List<Map<String, Object>> getUniformesDisponibles() {
        return createMockUniformesDisponibles();
    }

    /**
     * API: Validar código único
     */
    @GetMapping("/api/validate-codigo")
    @ResponseBody
    public Map<String, Object> validateCodigo(@RequestParam String codigo) {
        // Simular validación
        boolean exists = false; // uniformeService.existePorCodigo(codigo);

        return Map.of("exists", exists);
    }

    /**
     * Métodos auxiliares para crear datos simulados
     */
    private Map<String, Object> createMockUniforme(Long id) {
        Map<String, Object> uniforme = new HashMap<>();
        uniforme.put("id", id != null ? id : 1L);
        uniforme.put("nombre", "Camisa Escolar Blanca");
        uniforme.put("codigo", "CAM-001");
        uniforme.put("descripcion", "Camisa escolar de alta calidad, tela 100% algodón");
        uniforme.put("categoria", "Camisas");
        uniforme.put("talla", "M");
        uniforme.put("color", "Blanco");
        uniforme.put("precio", BigDecimal.valueOf(50000));
        uniforme.put("precioCompra", BigDecimal.valueOf(30000));
        uniforme.put("stock", 25);
        uniforme.put("stockMinimo", 5);
        uniforme.put("activo", true);
        uniforme.put("marca", "Uniformes Premium");
        uniforme.put("material", "100% Algodón");
        uniforme.put("proveedor", createMockProveedor());
        uniforme.put("stockActual", 25);
        return uniforme;
    }

    private Map<String, Object> createMockProveedor() {
        Map<String, Object> proveedor = new HashMap<>();
        proveedor.put("id", 1L);
        proveedor.put("nombre", "Textiles Colombia S.A.S");
        proveedor.put("nit", "900.123.456-7");
        return proveedor;
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

    private List<Map<String, Object>> createMockProveedores() {
        List<Map<String, Object>> proveedores = new ArrayList<>();
        proveedores.add(createMockProveedor());
        return proveedores;
    }

    private List<Map<String, Object>> createMockUniformesDisponibles() {
        List<Map<String, Object>> uniformes = new ArrayList<>();
        uniformes.add(createMockUniforme(1L));
        
        Map<String, Object> uniforme2 = new HashMap<>();
        uniforme2.put("id", 2L);
        uniforme2.put("nombre", "Pantalón Escolar Azul");
        uniforme2.put("codigo", "PAN-001");
        uniforme2.put("precio", BigDecimal.valueOf(75000));
        uniforme2.put("stock", 15);
        uniforme2.put("categoria", "Pantalones");
        uniforme2.put("talla", "M");
        uniformes.add(uniforme2);
        return uniformes;
    }

    @GetMapping("/api/uniformes/buscar-por-codigo")
    @ResponseBody
    public ResponseEntity<Uniforme> buscarUniformePorCodigo(@RequestParam String codigo) {
        try {
            Uniforme uniforme = uniformeService.obtenerUniformePorCodigo(codigo);
            return ResponseEntity.ok(uniforme);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/uniformes/disponibles")
    @ResponseBody
    public ResponseEntity<List<Uniforme>> obtenerUniformesDisponibles() {
        List<Uniforme> uniformes = uniformeService.obtenerUniformesDisponibles();
        return ResponseEntity.ok(uniformes);
    }

    @GetMapping("/api/uniformes/buscar")
    @ResponseBody
    public ResponseEntity<List<Uniforme>> buscarUniformes(@RequestParam String q) {
        try {
            Pageable pageable = PageRequest.of(0, 20);
            Page<Uniforme> uniformes = uniformeService.buscarUniformes(q, pageable);
            return ResponseEntity.ok(uniformes.getContent());
        } catch (Exception e) {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    @GetMapping("/alerta-stock-bajo")
    @ResponseBody
    public List<Uniforme> alertaStockBajo() {
        return uniformeService.obtenerUniformesBajoStock();
    }
}
