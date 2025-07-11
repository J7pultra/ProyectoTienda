package com.boutique.uniformes.service;

import com.boutique.uniformes.domain.Asistencia;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AsistenciaService {

    Asistencia registrarEntrada(Asistencia asistencia);

    Asistencia marcarSalida(Long asistenciaId, LocalTime horaSalida);

    Asistencia actualizarAsistencia(Long id, Asistencia asistencia);

    void eliminarAsistencia(Long id);

    List<Asistencia> listarAsistencias(Long empleadoId, LocalDate fechaInicio, LocalDate fechaFin);

    boolean existeAsistencia(Long empleadoId, LocalDate fecha);

    Asistencia obtenerPorId(Long id);

    Long contarAsistencias();
}

