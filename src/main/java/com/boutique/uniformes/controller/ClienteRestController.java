package com.boutique.uniformes.controller;

import com.boutique.uniformes.domain.Cliente;
import com.boutique.uniformes.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteRestController {

    private final ClienteService clienteService;

    @GetMapping("/buscar-por-documento")
    public Cliente buscarPorDocumento(@RequestParam String documento) {
        return clienteService.buscarPorDocumento(documento);
    }
}
