/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.repository;


import com.boutique.uniformes.domain.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
 
    Optional<Cliente> findByDocumento(String documento);
    
    List<Cliente> findByActivoTrue();
    
    Page<Cliente> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrDocumentoContainingIgnoreCase(
            String nombre, String apellido, String documento, Pageable pageable);
    
    Long countByActivoTrue();
    
    boolean existsByDocumento(String documento);
    
    @Query("SELECT c FROM Cliente c WHERE c.activo = true ORDER BY c.fechaRegistro DESC")
    List<Cliente> findClientesRecientes(Pageable pageable);
    
    @Query("SELECT c FROM Cliente c WHERE c.fechaRegistro BETWEEN :fechaInicio AND :fechaFin")
    List<Cliente> findByFechaRegistroBetween(@Param("fechaInicio") LocalDateTime fechaInicio, 
                                           @Param("fechaFin") LocalDateTime fechaFin);
    
    @Query("SELECT c FROM Cliente c JOIN c.ventas v GROUP BY c ORDER BY COUNT(v) DESC")
    List<Cliente> findClientesConMasCompras(Pageable pageable);
    
 


}
