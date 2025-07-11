package com.boutique.uniformes.service;

import com.boutique.uniformes.domain.Cliente;
import com.boutique.uniformes.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    @Transactional
    public Cliente guardarCliente(Cliente cliente) {
        if (clienteRepository.existsByDocumento(cliente.getDocumento())) {
            throw new RuntimeException("Ya existe un cliente con ese documento");
        }
        cliente.setActivo(true);
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional
    public Cliente actualizarCliente(Long id, Cliente cliente) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        if (!clienteExistente.getDocumento().equals(cliente.getDocumento()) &&
            clienteRepository.existsByDocumento(cliente.getDocumento())) {
            throw new RuntimeException("Ya existe un cliente con ese documento");
        }
        clienteExistente.setNombre(cliente.getNombre());
        clienteExistente.setApellido(cliente.getApellido());
        clienteExistente.setDocumento(cliente.getDocumento());
        clienteExistente.setEmail(cliente.getEmail());
        return clienteRepository.save(clienteExistente);
    }

    @Override
    @Transactional
    public void eliminarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        clienteRepository.delete(cliente);
    }

    @Override
    public Cliente obtenerClientePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    @Override
    public boolean existeClientePorDocumento(String documento) {
        return clienteRepository.existsByDocumento(documento);
    }

    @Override
    public List<Cliente> obtenerClientesActivos() {
        return clienteRepository.findAll().stream()
                .filter(Cliente::isActivo)
                .toList();
    }

    @Override
    public List<Cliente> buscarClientesInteligente(String query) {
        return clienteRepository.busquedaInteligente(query);
    }

    @Override
    public List<Object> obtenerHistorialCompras(Long clienteId) {
        // Implementar consulta real de historial de compras (ventas) por cliente
        // Ejemplo: return ventaRepository.findByClienteId(clienteId);
        return List.of(); // Placeholder
    }

    @Override
    public Cliente buscarPorDocumento(String documento) {
        Cliente cliente = clienteRepository.findByDocumentoAndActivoTrue(documento);
        if (cliente == null) {
            throw new RuntimeException("Cliente no encontrado");
        }
        return cliente;
    }
}
