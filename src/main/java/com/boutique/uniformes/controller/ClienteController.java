package com.boutique.uniformes.controller;

import com.boutique.uniformes.domain.Cliente;
import com.boutique.uniformes.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public String listaClientes(Model model) {
        List<Cliente> clientes = clienteService.obtenerClientesActivos();
        model.addAttribute("clientes", clientes);
        model.addAttribute("cliente", new Cliente()); // Para el modal
        return "clientes/clientes";
    }

    @GetMapping("/nuevo")
    public String nuevoCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/clientes"; // Usar el mismo html unificado
    }

    @PostMapping("/guardar")
    public String guardarCliente(@Valid @ModelAttribute("cliente") Cliente cliente,
                                BindingResult result,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        if (result.hasErrors()) {
            List<Cliente> clientes = clienteService.obtenerClientesActivos();
            model.addAttribute("clientes", clientes); // Para recargar la lista
            return "clientes/clientes";
        }
        try {
            clienteService.guardarCliente(cliente);
            redirectAttributes.addFlashAttribute("success", "Cliente registrado exitosamente");
            return "redirect:/clientes";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            List<Cliente> clientes = clienteService.obtenerClientesActivos();
            model.addAttribute("clientes", clientes);
            return "clientes/clientes";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Cliente cliente = clienteService.obtenerClientePorId(id);
            model.addAttribute("cliente", cliente);
            List<Cliente> clientes = clienteService.obtenerClientesActivos();
            model.addAttribute("clientes", clientes);
            return "clientes/clientes";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/clientes";
        }
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarCliente(@PathVariable Long id,
                                   @Valid @ModelAttribute("cliente") Cliente cliente,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        if (result.hasErrors()) {
            List<Cliente> clientes = clienteService.obtenerClientesActivos();
            model.addAttribute("clientes", clientes);
            return "clientes/clientes";
        }
        try {
            clienteService.actualizarCliente(id, cliente);
            redirectAttributes.addFlashAttribute("success", "Cliente actualizado exitosamente");
            return "redirect:/clientes";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            List<Cliente> clientes = clienteService.obtenerClientesActivos();
            model.addAttribute("clientes", clientes);
            return "clientes/clientes";
        }
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.eliminarCliente(id);
            redirectAttributes.addFlashAttribute("success", "Cliente eliminado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/clientes";
    }

    @GetMapping("/buscar")
    @ResponseBody
    public List<Cliente> buscarClientes(@RequestParam String query) {
        return clienteService.buscarClientesInteligente(query);
    }

    @GetMapping("/historial/{id}")
    public String historialCompras(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Cliente cliente = clienteService.obtenerClientePorId(id);
            model.addAttribute("cliente", cliente);
            model.addAttribute("historial", clienteService.obtenerHistorialCompras(id));
            return "clientes/historial";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/clientes";
        }
    }
}

