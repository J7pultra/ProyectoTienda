/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.repository;


import com.boutique.uniformes.domain.Uniforme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UniformeRepository extends JpaRepository<Uniforme, Long> {
    
    Optional<Uniforme> findByCodigo(String codigo);
    
    List<Uniforme> findByActivoTrue();
    
    List<Uniforme> findByActivoTrueAndStockActualGreaterThan(Integer stock);
    
    Page<Uniforme> findByCodigoContainingIgnoreCaseOrNombreContainingIgnoreCaseOrCategoriaContainingIgnoreCase(
            String codigo, String nombre, String categoria, Pageable pageable);
    
    Page<Uniforme> findByCategoriaContainingIgnoreCase(String categoria, Pageable pageable);
    
    @Query("SELECT u FROM Uniforme u WHERE u.stockActual <= u.stockMinimo")
    Page<Uniforme> findByStockActualLessThanEqualStockMinimo(Pageable pageable);
    
    @Query("SELECT COUNT(u) FROM Uniforme u WHERE u.stockActual <= u.stockMinimo")
    Long countByStockActualLessThanEqualStockMinimo();
    
    Long countByActivoTrue();
    
    boolean existsByCodigo(String codigo);
    
    List<Uniforme> findByCategoria(String categoria);
    
    List<Uniforme> findByTalla(String talla);
    
    List<Uniforme> findByColor(String color);
    
    @Query("SELECT u FROM Uniforme u WHERE (u.codigo = :query OR LOWER(u.nombre) LIKE LOWER(CONCAT('%',:query,'%')) OR LOWER(u.categoria) LIKE LOWER(CONCAT('%',:query,'%'))) AND u.activo = true")
    List<Uniforme> busquedaInteligente(@Param("query") String query);
    
    @Query("SELECT u FROM Uniforme u WHERE u.precio BETWEEN :precioMin AND :precioMax AND u.activo = true")
    List<Uniforme> findByPrecioBetween(@Param("precioMin") BigDecimal precioMin, 
                                       @Param("precioMax") BigDecimal precioMax);
    
    @Query("SELECT DISTINCT u.categoria FROM Uniforme u WHERE u.activo = true ORDER BY u.categoria")
    List<String> findDistinctCategorias();
    
    @Query("SELECT DISTINCT u.talla FROM Uniforme u WHERE u.activo = true ORDER BY u.talla")
    List<String> findDistinctTallas();
    
    @Query("SELECT DISTINCT u.color FROM Uniforme u WHERE u.activo = true ORDER BY u.color")
    List<String> findDistinctColores();
    
    @Query("SELECT u.proveedor.id as proveedorId, u.proveedor.nombre as proveedorNombre, COUNT(u) as totalUniformes " +
           "FROM Uniforme u WHERE u.activo = true GROUP BY u.proveedor ORDER BY COUNT(u) DESC")
    List<Map<String, Object>> findUniformesPorProveedor();
    
    @Query(value = "SELECT u.id, u.nombre, u.codigo, SUM(dv.cantidad) as totalVendido " +
                   "FROM uniformes u " +
                   "INNER JOIN detalle_ventas dv ON u.id = dv.uniforme_id " +
                   "INNER JOIN ventas v ON dv.venta_id = v.id " +
                   "WHERE v.estado = 'COMPLETADA' " +
                   "GROUP BY u.id, u.nombre, u.codigo " +
                   "ORDER BY totalVendido DESC", nativeQuery = true)
    List<Map<String, Object>> findUniformesMasVendidos(Pageable pageable);
}
