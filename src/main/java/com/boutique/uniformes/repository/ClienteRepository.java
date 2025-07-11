package com.boutique.uniformes.repository;

import com.boutique.uniformes.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    boolean existsByDocumento(String documento);
    Cliente findByDocumento(String documento);
    Cliente findByDocumentoAndActivoTrue(String documento);
    Cliente findByEmailAndActivoTrue(String email);
    boolean existsByEmail(String email);
    List<Cliente> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);
    @Query("SELECT c FROM Cliente c WHERE (c.documento = :query OR LOWER(c.nombre) LIKE LOWER(CONCAT('%',:query,'%')) OR LOWER(c.email) LIKE LOWER(CONCAT('%',:query,'%'))) AND c.activo = true")
    List<Cliente> busquedaInteligente(@Param("query") String query);
}
