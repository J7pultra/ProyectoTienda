package com.boutique.uniformes.service;

import com.boutique.uniformes.domain.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClienteService {
    
    Cliente guardarCliente(Cliente cliente);
    Cliente obtenerClientePorId(Long id);
    Cliente obtenerClientePorDocumento(String documento);
    List<Cliente> obtenerClientesActivos();
    Page<Cliente> obtenerClientesPaginados(Pageable pageable);
    Page<Cliente> buscarClientes(String buscar, Pageable pageable);
    void eliminarCliente(Long id);
    void cambiarEstadoCliente(Long id, Boolean activo);
    Long contarClientesActivos();
    boolean existeClientePorDocumento(String documento);
}
