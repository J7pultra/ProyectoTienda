/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.repository;

import com.boutique.uniformes.domain.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {
    
    List<DetalleVenta> findByVentaId(Long ventaId);
    
    List<DetalleVenta> findByUniformeId(Long uniformeId);
    
    @Query("SELECT dv FROM DetalleVenta dv WHERE dv.venta.fechaVenta BETWEEN :fechaInicio AND :fechaFin")
    List<DetalleVenta> findByVentaFechaBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                              @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT dv.uniforme.categoria as categoria, SUM(dv.cantidad) as totalVendido, SUM(dv.subtotal) as totalIngresos " +
           "FROM DetalleVenta dv " +
           "WHERE dv.venta.estado = 'COMPLETADA' " +
           "GROUP BY dv.uniforme.categoria " +
           "ORDER BY SUM(dv.subtotal) DESC")
    List<Map<String, Object>> findVentasPorCategoria();
    
    @Query("SELECT dv.uniforme.talla as talla, SUM(dv.cantidad) as totalVendido " +
           "FROM DetalleVenta dv " +
           "WHERE dv.venta.estado = 'COMPLETADA' " +
           "GROUP BY dv.uniforme.talla " +
           "ORDER BY SUM(dv.cantidad) DESC")
    List<Map<String, Object>> findVentasPorTalla();
}
