package com.boutique.uniformes.repository;

import com.boutique.uniformes.domain.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(Rol.NombreRol nombre);
}
