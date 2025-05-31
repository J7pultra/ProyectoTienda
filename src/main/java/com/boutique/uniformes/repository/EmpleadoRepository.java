/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.repository;


import com.boutique.uniformes.domain.Empleado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    
    Optional<Empleado> findByDocumento(String documento);
    
    List<Empleado> findByActivoTrue();
    
    Page<Empleado> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCaseOrDocumentoContainingIgnoreCaseOrCargoContainingIgnoreCase(
            String nombre, String apellido, String documento, String cargo, Pageable pageable);
    
    Long countByActivoTrue();
    
    boolean existsByDocumento(String documento);
    
    List<Empleado> findByCargo(String cargo);
    
    @Query("SELECT e FROM Empleado e WHERE e.salario BETWEEN :salarioMin AND :salarioMax AND e.activo = true")
    List<Empleado> findBySalarioBetween(@Param("salarioMin") BigDecimal salarioMin, 
                                       @Param("salarioMax") BigDecimal salarioMax);
    
    @Query("SELECT e FROM Empleado e WHERE e.fechaIngreso BETWEEN :fechaInicio AND :fechaFin")
    List<Empleado> findByFechaIngresoBetween(@Param("fechaInicio") LocalDate fechaInicio, 
                                           @Param("fechaFin") LocalDate fechaFin);
    
    @Query("SELECT e FROM Empleado e JOIN e.ventas v GROUP BY e ORDER BY COUNT(v) DESC")
    List<Empleado> findEmpleadosConMasVentas(Pageable pageable);
}
