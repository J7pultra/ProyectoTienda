package com.boutique.uniformes.repository;

import com.boutique.uniformes.domain.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByDocumento(String documento);
    boolean existsByDocumento(String documento);
    Optional<Empleado> findByDocumentoAndActivoTrue(String documento);
    Optional<Empleado> findByEmailAndActivoTrue(String email);
    boolean existsByEmail(String email);
    @Query("SELECT e FROM Empleado e WHERE (e.documento = :query OR LOWER(e.nombre) LIKE LOWER(CONCAT('%',:query,'%')) OR LOWER(e.email) LIKE LOWER(CONCAT('%',:query,'%'))) AND e.activo = true")
    java.util.List<Empleado> busquedaInteligente(@org.springframework.data.repository.query.Param("query") String query);
}

