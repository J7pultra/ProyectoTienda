package com.boutique.uniformes.controller.rest;

import com.boutique.uniformes.dto.PedidoProveedorDTO;
import com.boutique.uniformes.dto.ProveedorResponseDTO;
import com.boutique.uniformes.domain.Proveedor;
import com.boutique.uniformes.service.ProveedorService;
import com.boutique.uniformes.service.PedidoProveedorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/proveedores")
public class ProveedorRestController {

    private final ProveedorService proveedorService;
    private final PedidoProveedorService pedidoProveedorService;

    /**
     * Buscar proveedor por cédula/NIT para el frontend de pedidos
     */
    @GetMapping("/buscar/{cedula}")
    public ResponseEntity<?> buscarProveedorPorCedula(@PathVariable String cedula) {
        try {
            Proveedor proveedor = proveedorService.obtenerProveedorPorNit(cedula);
            
            if (!proveedor.getActivo()) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "El proveedor está inactivo"));
            }
            
            ProveedorResponseDTO response = new ProveedorResponseDTO(
                proveedor.getNit(),
                proveedor.getNombre(),
                proveedor.getEmail(),
                proveedor.getTelefono()
            );
            response.setId(proveedor.getId());
            response.setDireccion(proveedor.getDireccion());
            response.setCiudad(proveedor.getCiudad());
            response.setContacto(proveedor.getContacto());
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Generar número de pedido único
     */
    @GetMapping("/generar-numero-pedido")
    public ResponseEntity<Map<String, String>> generarNumeroPedido() {
        String numeroPedido = pedidoProveedorService.generarNumeroPedido();
        return ResponseEntity.ok(Map.of("numeroPedido", numeroPedido));
    }

    /**
     * Guardar pedido desde el frontend
     */
    @PostMapping("/pedidos")
    public ResponseEntity<?> guardarPedido(@Valid @RequestBody PedidoProveedorDTO pedidoDTO) {
        try {
            PedidoProveedorDTO savedPedido = pedidoProveedorService.guardarPedidoDesdeDTO(pedidoDTO);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pedido guardado exitosamente");
            response.put("pedido", savedPedido);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al guardar el pedido: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Enviar pedido al proveedor (cambiar estado)
     */
    @PostMapping("/pedidos/{id}/enviar")
    public ResponseEntity<?> enviarPedido(@PathVariable Long id) {
        try {
            pedidoProveedorService.enviarPedido(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Pedido enviado exitosamente al proveedor");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Error al enviar el pedido: " + e.getMessage());
            
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Obtener pedido por ID
     */
    @GetMapping("/pedidos/{id}")
    public ResponseEntity<?> obtenerPedido(@PathVariable Long id) {
        try {
            PedidoProveedorDTO pedido = pedidoProveedorService.obtenerPedidoDTOPorId(id);
            return ResponseEntity.ok(pedido);
            
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Búsqueda inteligente de proveedores
     */
    @GetMapping("/busqueda")
    public ResponseEntity<?> busquedaInteligente(@RequestParam String query) {
        try {
            var proveedores = proveedorService.busquedaInteligente(query);
            return ResponseEntity.ok(proveedores);
            
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error en la búsqueda: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
