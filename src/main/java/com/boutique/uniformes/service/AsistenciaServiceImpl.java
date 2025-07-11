package com.boutique.uniformes.service;

import com.boutique.uniformes.domain.Asistencia;
import com.boutique.uniformes.repository.AsistenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AsistenciaServiceImpl implements AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Override
    @Transactional
    public Asistencia registrarEntrada(Asistencia asistencia) {
        boolean existe = asistenciaRepository.findByEmpleadoIdAndFecha(asistencia.getEmpleado().getId(), asistencia.getFecha()).isPresent();
        if (existe) {
            throw new RuntimeException("Ya existe una asistencia registrada para este empleado en la fecha indicada");
        }
        asistencia.setHoraEntrada(LocalTime.now());
        asistencia.setEstado("PRESENTE");
        return asistenciaRepository.save(asistencia);
    }

    @Override
    @Transactional
    public Asistencia marcarSalida(Long asistenciaId, LocalTime horaSalida) {
        Asistencia asistencia = asistenciaRepository.findById(asistenciaId)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada"));
        if (asistencia.getHoraSalida() != null) {
            throw new RuntimeException("La salida ya fue marcada");
        }
        asistencia.setHoraSalida(horaSalida);
        return asistenciaRepository.save(asistencia);
    }

    @Override
    @Transactional
    public Asistencia actualizarAsistencia(Long id, Asistencia asistencia) {
        Asistencia existente = asistenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada"));
        existente.setEstado(asistencia.getEstado());
        existente.setObservaciones(asistencia.getObservaciones());
        return asistenciaRepository.save(existente);
    }

    @Override
    @Transactional
    public void eliminarAsistencia(Long id) {
        asistenciaRepository.deleteById(id);
    }

    @Override
    public List<Asistencia> listarAsistencias(Long empleadoId, LocalDate fechaInicio, LocalDate fechaFin) {
        if (empleadoId == null) {
            return asistenciaRepository.findByFecha(fechaInicio); // O ajustar según filtro
        }
        return asistenciaRepository.findByEmpleadoIdAndFechaBetween(empleadoId, fechaInicio, fechaFin);
    }

    @Override
    public boolean existeAsistencia(Long empleadoId, LocalDate fecha) {
        return asistenciaRepository.findByEmpleadoIdAndFecha(empleadoId, fecha).isPresent();
    }

    @Override
    public Asistencia obtenerPorId(Long id) {
        return asistenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada"));
    }

    @Override
    public Long contarAsistencias() {
        return asistenciaRepository.count();
    }
}
