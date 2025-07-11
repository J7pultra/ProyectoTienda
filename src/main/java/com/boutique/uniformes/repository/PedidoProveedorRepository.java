package com.boutique.uniformes.repository;

import com.boutique.uniformes.domain.PedidoProveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PedidoProveedorRepository extends JpaRepository<PedidoProveedor, Long> {
    List<PedidoProveedor> findByProveedor_Nit(String nit);
    List<PedidoProveedor> findByProveedor_NombreContainingIgnoreCase(String nombre);
    
    // Método para contar pedidos con número que inicia con un patrón
    long countByNumeroPedidoStartingWith(String numeroPedidoPrefix);
    
    // Método para buscar por estado
    List<PedidoProveedor> findByEstado(String estado);
    
    // Método para buscar por número de pedido
    PedidoProveedor findByNumeroPedido(String numeroPedido);
    
    // Consulta personalizada para estadísticas
    @Query("SELECT COUNT(p) FROM PedidoProveedor p WHERE p.estado = :estado")
    long countByEstado(@Param("estado") String estado);
}
