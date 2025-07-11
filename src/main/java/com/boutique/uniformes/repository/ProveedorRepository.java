package com.boutique.uniformes.repository;

import com.boutique.uniformes.domain.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    // Métodos usados en tu ProveedorServiceImpl y controlador
    boolean existsByNit(String nit);
    List<Proveedor> findByActivoTrue();
    Optional<Proveedor> findByNit(String nit); // Usado en obtenerProveedorPorNit
    Page<Proveedor> findByNombreContainingIgnoreCaseOrNitContainingIgnoreCaseOrContactoContainingIgnoreCase(String nombre, String nit, String contacto, Pageable pageable);
    Long countByActivoTrue();

    @Query("SELECT p FROM Proveedor p WHERE (p.nit = :query OR LOWER(p.nombre) LIKE LOWER(CONCAT('%',:query,'%')) OR LOWER(p.contacto) LIKE LOWER(CONCAT('%',:query,'%'))) AND p.activo = true")
    List<Proveedor> busquedaInteligente(@org.springframework.data.repository.query.Param("query") String query);
}
