package com.boutique.uniformes.controller;

import com.boutique.uniformes.domain.PedidoProveedor;
import com.boutique.uniformes.dto.PedidoProveedorDTO;
import com.boutique.uniformes.service.PedidoProveedorService;
import com.boutique.uniformes.service.ProveedorService;
import com.boutique.uniformes.service.UniformeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/proveedores/pedidos")
public class PedidoProveedorController {

    @Autowired
    private PedidoProveedorService pedidoProveedorService;
    @Autowired
    private ProveedorService proveedorService;
    @Autowired
    private UniformeService uniformeService;

    /**
     * Página del formulario de pedidos (la que acabamos de crear)
     */
    @GetMapping
    public String mostrarFormularioPedidos(Model model) {
        // Agregar datos necesarios para el formulario
        model.addAttribute("numeroPedido", pedidoProveedorService.generarNumeroPedido());
        return "proveedores/pedidos";
    }

    /**
     * Métodos existentes mantenidos para compatibilidad
     */
    @GetMapping("/lista")
    public String listarPedidos(Model model) {
        List<PedidoProveedorDTO> pedidos = pedidoProveedorService.listarPedidosDTO();
        model.addAttribute("pedidos", pedidos);
        return "proveedores/pedidos-lista";
    }

    @GetMapping("/pedidos-proveedor/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("pedido", new PedidoProveedor());
        model.addAttribute("proveedores", proveedorService.obtenerProveedoresActivos());
        model.addAttribute("uniformes", uniformeService.obtenerUniformesActivos());
        return "proveedores/pedido-form";
    }

    @PostMapping("/pedidos-proveedor/guardar")
    public String guardarPedido(@ModelAttribute PedidoProveedor pedido, 
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            pedidoProveedorService.guardarPedido(pedido, pedido.getDetalles());
            redirectAttributes.addFlashAttribute("success", "Pedido guardado exitosamente");
            return "redirect:/proveedores/pedidos-proveedor";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("pedido", pedido);
            model.addAttribute("proveedores", proveedorService.obtenerProveedoresActivos());
            model.addAttribute("uniformes", uniformeService.obtenerUniformesActivos());
            model.addAttribute("detalles", pedido.getDetalles());
            return "proveedores/pedido-form";
        }
    }

    @PostMapping("/pedidos-proveedor/completar/{id}")
    public String marcarComoCompletado(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            pedidoProveedorService.marcarComoCompletado(id);
            redirectAttributes.addFlashAttribute("success", "Pedido marcado como completado");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al completar el pedido: " + e.getMessage());
        }
        return "redirect:/proveedores/pedidos-proveedor";
    }

    @GetMapping("/pedidos-proveedor/{id}")
    public String verDetalle(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            PedidoProveedorDTO pedido = pedidoProveedorService.obtenerPedidoDTOPorId(id);
            model.addAttribute("pedido", pedido);
            return "proveedores/pedido-detalle";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Pedido no encontrado");
            return "redirect:/proveedores/pedidos-proveedor";
        }
    }

    @GetMapping("/pedidos-proveedor/buscar")
    public String buscarPedidos(@RequestParam(required = false) String nit,
                               @RequestParam(required = false) String nombre,
                               Model model) {
        List<PedidoProveedor> pedidos;
        if (nit != null && !nit.isEmpty()) {
            pedidos = pedidoProveedorService.buscarPorNitProveedor(nit);
        } else if (nombre != null && !nombre.isEmpty()) {
            pedidos = pedidoProveedorService.buscarPorNombreProveedor(nombre);
        } else {
            pedidos = pedidoProveedorService.listarPedidos();
        }
        model.addAttribute("pedidos", pedidos);
        return "proveedores/pedidos-lista";
    }
}
