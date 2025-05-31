/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.repository;


import com.boutique.uniformes.domain.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    
    Optional<Venta> findByNumeroFactura(String numeroFactura);
    
    Page<Venta> findByNumeroFacturaContainingIgnoreCaseOrClienteNombreContainingIgnoreCaseOrClienteApellidoContainingIgnoreCase(
            String numeroFactura, String clienteNombre, String clienteApellido, Pageable pageable);
    
    Page<Venta> findByFechaVentaBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin, Pageable pageable);
    
    List<Venta> findByClienteId(Long clienteId);
    
    List<Venta> findByVendedorId(Long vendedorId);
    
    List<Venta> findByEstado(Venta.EstadoVenta estado);
    
    @Query("SELECT SUM(v.total) FROM Venta v WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin AND v.estado = 'COMPLETADA'")
    BigDecimal sumTotalByFechaVentaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                          @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT COUNT(v) FROM Venta v WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin AND v.estado = 'COMPLETADA'")
    Long countByFechaVentaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                 @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT AVG(v.total) FROM Venta v WHERE v.estado = 'COMPLETADA'")
    BigDecimal findPromedioVentas();
    
    @Query("SELECT v.vendedor.id as vendedorId, v.vendedor.nombre as vendedorNombre, " +
           "v.vendedor.apellido as vendedorApellido, SUM(v.total) as totalVentas, COUNT(v) as numeroVentas " +
           "FROM Venta v WHERE v.estado = 'COMPLETADA' " +
           "GROUP BY v.vendedor ORDER BY SUM(v.total) DESC")
    List<Map<String, Object>> findVentasPorVendedor();
    
    @Query("SELECT v.cliente.id as clienteId, v.cliente.nombre as clienteNombre, " +
           "v.cliente.apellido as clienteApellido, SUM(v.total) as totalCompras, COUNT(v) as numeroCompras " +
           "FROM Venta v WHERE v.estado = 'COMPLETADA' " +
           "GROUP BY v.cliente ORDER BY SUM(v.total) DESC")
    List<Map<String, Object>> findComprasPorCliente();
    
    @Query(value = "SELECT DATE(fecha_venta) as fecha, COUNT(*) as numeroVentas, SUM(total) as totalVentas " +
                   "FROM ventas " +
                   "WHERE fecha_venta >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) AND estado = 'COMPLETADA' " +
                   "GROUP BY DATE(fecha_venta) " +
                   "ORDER BY fecha DESC", nativeQuery = true)
    List<Map<String, Object>> findVentasUltimos7Dias();
    
    @Query(value = "SELECT MONTH(fecha_venta) as mes, YEAR(fecha_venta) as anio, " +
                   "COUNT(*) as numeroVentas, SUM(total) as totalVentas " +
                   "FROM ventas " +
                   "WHERE estado = 'COMPLETADA' " +
                   "GROUP BY YEAR(fecha_venta), MONTH(fecha_venta) " +
                   "ORDER BY anio DESC, mes DESC " +
                   "LIMIT 12", nativeQuery = true)
    List<Map<String, Object>> findVentasPorMes();
}
