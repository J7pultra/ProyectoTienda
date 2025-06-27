/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.service;

import com.boutique.uniformes.domain.Cliente;
import com.boutique.uniformes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    @Override
    public Cliente guardarCliente(Cliente cliente) {
        if (cliente.getId() == null) {
            if (existeClientePorDocumento(cliente.getDocumento())) {
                throw new RuntimeException("Ya existe un cliente con ese documento");
            }
            cliente.setFechaRegistro(LocalDateTime.now());
            cliente.setActivo(true);
        }
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente obtenerClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
    }

    
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerClientesActivos() {
        return clienteRepository.findByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> obtenerClientesPaginados(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Cliente> buscarClientes(String buscar, Pageable pageable) {
        return clienteRepository.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrDocumentoContainingIgnoreCase(
                buscar, buscar, buscar, pageable);
    }

    @Override
    public void eliminarCliente(Long id) {
        Cliente cliente = obtenerClientePorId(id);
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    @Override
    public void cambiarEstadoCliente(Long id, Boolean activo) {
        Cliente cliente = obtenerClientePorId(id);
        cliente.setActivo(activo);
        clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarClientesActivos() {
        return clienteRepository.countByActivoTrue();
    }

   
    
@Override
@Transactional(readOnly = true)
public Cliente obtenerClientePorDocumento(String documento) {
    return clienteRepository.findByDocumento(documento)
        .orElseThrow(() -> new RuntimeException("Cliente no encontrado con documento: " + documento));
}

@Override
@Transactional(readOnly = true)
public boolean existeClientePorDocumento(String documento) {
    return clienteRepository.existsByDocumento(documento);
}
}
