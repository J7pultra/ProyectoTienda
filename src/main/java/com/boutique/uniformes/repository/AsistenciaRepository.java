 package com.boutique.uniformes.repository;

import com.boutique.uniformes.domain.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    List<Asistencia> findByEmpleadoIdAndFechaBetween(Long empleadoId, LocalDate fechaInicio, LocalDate fechaFin);

    Optional<Asistencia> findByEmpleadoIdAndFecha(Long empleadoId, LocalDate fecha);

    List<Asistencia> findByFecha(LocalDate fecha);
}
