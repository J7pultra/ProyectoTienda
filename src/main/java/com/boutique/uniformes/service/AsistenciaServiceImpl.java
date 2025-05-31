/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.service;

import com.boutique.uniformes.domain.Asistencia;
import com.boutique.uniformes.domain.Empleado;
import com.boutique.uniformes.repository.AsistenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AsistenciaServiceImpl implements AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final EmpleadoService empleadoService;

    @Override
    public Asistencia registrarAsistencia(Asistencia asistencia) {
        if (existeAsistenciaHoy(asistencia.getEmpleado().getId())) {
            throw new RuntimeException("Ya existe un registro de asistencia para este empleado hoy");
        }
        return asistenciaRepository.save(asistencia);
    }

    @Override
    public Asistencia marcarEntrada(Long empleadoId) {
        if (existeAsistenciaHoy(empleadoId)) {
            throw new RuntimeException("Ya se marcó la entrada para este empleado hoy");
        }

        Empleado empleado = empleadoService.obtenerEmpleadoPorId(empleadoId);
        Asistencia asistencia = new Asistencia();
        asistencia.setEmpleado(empleado);
        asistencia.setFecha(LocalDate.now());
        asistencia.setHoraEntrada(LocalTime.now());
        asistencia.setEstado(Asistencia.EstadoAsistencia.PRESENTE);

        return asistenciaRepository.save(asistencia);
    }

    @Override
    public Asistencia marcarSalida(Long empleadoId) {
        Asistencia asistencia = asistenciaRepository.findByEmpleadoIdAndFecha(empleadoId, LocalDate.now())
                .orElseThrow(() -> new RuntimeException("No se encontró registro de entrada para hoy"));

        if (asistencia.getHoraSalida() != null) {
            throw new RuntimeException("Ya se marcó la salida para este empleado hoy");
        }

        asistencia.setHoraSalida(LocalTime.now());
        return asistenciaRepository.save(asistencia);
    }

    @Override
    @Transactional(readOnly = true)
    public Asistencia obtenerAsistenciaPorId(Long id) {
        return asistenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Asistencia> obtenerAsistenciasPaginadas(Pageable pageable) {
        return asistenciaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Asistencia> obtenerAsistenciasPorEmpleado(Long empleadoId, Pageable pageable) {
        return asistenciaRepository.findByEmpleadoId(empleadoId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Asistencia> obtenerAsistenciasEntreFechas(LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable) {
        return asistenciaRepository.findByFechaBetween(fechaInicio, fechaFin, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Asistencia> obtenerAsistenciasPorEmpleadoYFechas(Long empleadoId, LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable) {
        return asistenciaRepository.findByEmpleadoIdAndFechaBetween(empleadoId, fechaInicio, fechaFin, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeAsistenciaHoy(Long empleadoId) {
        return asistenciaRepository.existsByEmpleadoIdAndFecha(empleadoId, LocalDate.now());
    }
}
