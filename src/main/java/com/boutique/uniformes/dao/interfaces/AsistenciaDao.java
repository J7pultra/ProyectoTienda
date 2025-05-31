package com.boutique.uniformes.dao.interfaces;

import com.boutique.uniformes.domain.Asistencia;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AsistenciaDao {
    void guardar(Asistencia asistencia);
    Optional<Asistencia> buscarPorId(Long id);
    List<Asistencia> buscarTodos();
    List<Asistencia> buscarTodos(int page, int size);
    void actualizar(Asistencia asistencia);
    void eliminar(Long id);
    List<Asistencia> buscarPorEmpleado(Long empleadoId);
    List<Asistencia> buscarPorFecha(LocalDate fecha);
    List<Asistencia> buscarPorEmpleadoYFecha(Long empleadoId, LocalDate fechaInicio, LocalDate fechaFin);
    Optional<Asistencia> buscarPorEmpleadoYFechaExacta(Long empleadoId, LocalDate fecha);
    List<Asistencia> buscarPorEstado(String estado);
    long contarPresentesHoy();
    long contarAusentesHoy();
    long contarTardanzasHoy();
    boolean existeAsistenciaHoy(Long empleadoId);
}
