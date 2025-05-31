/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.repository;

import com.boutique.uniformes.domain.Asistencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
    
    Page<Asistencia> findByEmpleadoId(Long empleadoId, Pageable pageable);
    
    Page<Asistencia> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable);
    
    Page<Asistencia> findByEmpleadoIdAndFechaBetween(Long empleadoId, LocalDate fechaInicio, 
                                                    LocalDate fechaFin, Pageable pageable);
    
    Optional<Asistencia> findByEmpleadoIdAndFecha(Long empleadoId, LocalDate fecha);
    
    boolean existsByEmpleadoIdAndFecha(Long empleadoId, LocalDate fecha);
    
    List<Asistencia> findByFecha(LocalDate fecha);
    
    List<Asistencia> findByEstado(Asistencia.EstadoAsistencia estado);
    
    @Query("SELECT a FROM Asistencia a WHERE a.empleado.id = :empleadoId AND a.fecha = :fecha")
    Optional<Asistencia> findAsistenciaDelDia(@Param("empleadoId") Long empleadoId, @Param("fecha") LocalDate fecha);
    
    @Query("SELECT a.empleado.id as empleadoId, a.empleado.nombre as empleadoNombre, " +
           "a.empleado.apellido as empleadoApellido, COUNT(a) as diasTrabajados " +
           "FROM Asistencia a " +
           "WHERE a.fecha BETWEEN :fechaInicio AND :fechaFin AND a.estado = 'PRESENTE' " +
           "GROUP BY a.empleado " +
           "ORDER BY COUNT(a) DESC")
    List<Map<String, Object>> findAsistenciaPorEmpleado(@Param("fechaInicio") LocalDate fechaInicio, 
                                                       @Param("fechaFin") LocalDate fechaFin);
    
    @Query("SELECT a.estado as estado, COUNT(a) as total " +
           "FROM Asistencia a " +
           "WHERE a.fecha BETWEEN :fechaInicio AND :fechaFin " +
           "GROUP BY a.estado")
    List<Map<String, Object>> findEstadisticasAsistencia(@Param("fechaInicio") LocalDate fechaInicio, 
                                                        @Param("fechaFin") LocalDate fechaFin);
    
    @Query("SELECT COUNT(a) FROM Asistencia a WHERE a.empleado.id = :empleadoId AND a.fecha BETWEEN :fechaInicio AND :fechaFin AND a.estado = 'PRESENTE'")
    Long countDiasTrabajados(@Param("empleadoId") Long empleadoId, 
                           @Param("fechaInicio") LocalDate fechaInicio, 
                           @Param("fechaFin") LocalDate fechaFin);
    
    @Query("SELECT COUNT(a) FROM Asistencia a WHERE a.empleado.id = :empleadoId AND a.fecha BETWEEN :fechaInicio AND :fechaFin AND a.estado = 'AUSENTE'")
    Long countDiasAusente(@Param("empleadoId") Long empleadoId, 
                        @Param("fechaInicio") LocalDate fechaInicio, 
                        @Param("fechaFin") LocalDate fechaFin);
}
