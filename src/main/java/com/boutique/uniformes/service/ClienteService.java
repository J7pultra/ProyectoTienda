package com.boutique.uniformes.service;

import com.boutique.uniformes.domain.Cliente;
import java.util.List;

public interface ClienteService {
    Cliente guardarCliente(Cliente cliente);
    Cliente actualizarCliente(Long id, Cliente cliente);
    void eliminarCliente(Long id);
    Cliente obtenerClientePorId(Long id);
    boolean existeClientePorDocumento(String documento);
    List<Cliente> obtenerClientesActivos();
    List<Cliente> buscarClientesInteligente(String query);
    List<Object> obtenerHistorialCompras(Long clienteId);
    Cliente buscarPorDocumento(String documento);
}

