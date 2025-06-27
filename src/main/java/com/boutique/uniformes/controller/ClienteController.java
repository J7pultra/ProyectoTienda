package com.boutique.uniformes.controller;

import com.boutique.uniformes.domain.Cliente;
import com.boutique.uniformes.service.ClienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

@Controller
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private ClienteService clienteService;

    @GetMapping
    public String lista(
            Model model,
            @RequestParam(defaultValue = "") String buscar,
            @RequestParam(defaultValue = "") String estado,
            @RequestParam(defaultValue = "") String ciudad,
            @RequestParam(defaultValue = "nombre") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page clientes = new PageImpl<>(Collections.emptyList(), pageable, 0);

        model.addAttribute("totalClientes", 0);
        model.addAttribute("clientesActivos", 0);
        model.addAttribute("clientesNuevos", 0);
        model.addAttribute("clientesConCompras", 0);

        model.addAttribute("clientes", clientes);
        model.addAttribute("buscar", buscar);
        model.addAttribute("estado", estado);
        model.addAttribute("ciudad", ciudad);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        List ciudades = new ArrayList<>();
        ciudades.add("Bogotá");
        ciudades.add("Medellín");
        ciudades.add("Cali");
        ciudades.add("Barranquilla");
        model.addAttribute("ciudades", ciudades);

        model.addAttribute("currentPage", "clientes");

        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("currentPage", "clientes");
        return "clientes/formulario";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("currentPage", "clientes");
        return "clientes/detalle";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("currentPage", "clientes");
        return "clientes/formulario";
    }

    @PostMapping
    public String guardar() {
        return "redirect:/clientes";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id) {
        return "redirect:/clientes";
    }

    @DeleteMapping("/{id}")
    public String eliminar(@PathVariable Long id) {
        return "redirect:/clientes";
    }

// Agregar estos métodos a tu ClienteController existente
    @GetMapping("/api/clientes/buscar-por-documento")
    @ResponseBody
    public ResponseEntity<Cliente> buscarClientePorDocumento(@RequestParam String documento) {
        try {
            Cliente cliente = clienteService.obtenerClientePorDocumento(documento);
            return ResponseEntity.ok(cliente);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/api/clientes/validar-documento")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> validarDocumento(@RequestParam String documento) {
        boolean existe = clienteService.existeClientePorDocumento(documento);
        Map<String, Boolean> response = new HashMap<>();
        response.put("existe", existe);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/clientes/activos")
    @ResponseBody
    public ResponseEntity<List<Cliente>> obtenerClientesActivos() {
        List<Cliente> clientes = clienteService.obtenerClientesActivos();
        return ResponseEntity.ok(clientes);
    }
}
