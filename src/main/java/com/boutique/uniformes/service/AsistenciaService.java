/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.boutique.uniformes.service;

import com.boutique.uniformes.domain.Asistencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface AsistenciaService {
    Asistencia registrarAsistencia(Asistencia asistencia);
    Asistencia marcarEntrada(Long empleadoId);
    Asistencia marcarSalida(Long empleadoId);
    Asistencia obtenerAsistenciaPorId(Long id);
    Page<Asistencia> obtenerAsistenciasPaginadas(Pageable pageable);
    Page<Asistencia> obtenerAsistenciasPorEmpleado(Long empleadoId, Pageable pageable);
    Page<Asistencia> obtenerAsistenciasEntreFechas(LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable);
    Page<Asistencia> obtenerAsistenciasPorEmpleadoYFechas(Long empleadoId, LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable);
    boolean existeAsistenciaHoy(Long empleadoId);
}
