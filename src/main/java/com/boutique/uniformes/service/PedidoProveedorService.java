package com.boutique.uniformes.service;

import com.boutique.uniformes.domain.PedidoProveedor;
import com.boutique.uniformes.domain.DetallePedidoProveedor;
import com.boutique.uniformes.dto.PedidoProveedorDTO;
import java.util.List;

public interface PedidoProveedorService {
    // Métodos existentes
    PedidoProveedor guardarPedido(PedidoProveedor pedido, List<DetallePedidoProveedor> detalles);
    List<PedidoProveedor> listarPedidos();
    PedidoProveedor obtenerPedidoPorId(Long id);
    void marcarComoCompletado(Long id);
    List<PedidoProveedor> buscarPorNitProveedor(String nit);
    List<PedidoProveedor> buscarPorNombreProveedor(String nombre);
    
    // Nuevos métodos para el frontend
    String generarNumeroPedido();
    PedidoProveedorDTO guardarPedidoDesdeDTO(PedidoProveedorDTO pedidoDTO);
    PedidoProveedorDTO obtenerPedidoDTOPorId(Long id);
    void enviarPedido(Long id);
    void cancelarPedido(Long id);
    List<PedidoProveedorDTO> listarPedidosDTO();
}
