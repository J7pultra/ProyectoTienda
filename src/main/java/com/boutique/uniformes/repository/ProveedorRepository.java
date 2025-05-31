/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.repository;



import com.boutique.uniformes.domain.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    
    Optional<Proveedor> findByNit(String nit);
    
    List<Proveedor> findByActivoTrue();
    
    Page<Proveedor> findByNombreContainingIgnoreCaseOrNitContainingIgnoreCaseOrContactoContainingIgnoreCase(
            String nombre, String nit, String contacto, Pageable pageable);
    
    Long countByActivoTrue();
    
    boolean existsByNit(String nit);
    
    List<Proveedor> findByCiudad(String ciudad);
    
    @Query("SELECT p FROM Proveedor p WHERE p.fechaRegistro BETWEEN :fechaInicio AND :fechaFin")
    List<Proveedor> findByFechaRegistroBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                             @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT p.id as proveedorId, p.nombre as proveedorNombre, COUNT(u) as totalUniformes " +
           "FROM Proveedor p LEFT JOIN p.uniformes u " +
           "WHERE p.activo = true " +
           "GROUP BY p.id, p.nombre " +
           "ORDER BY COUNT(u) DESC")
    List<Map<String, Object>> findProveedoresConUniformes();
}
