package com.boutique.uniformes.dao.interfaces;

import com.boutique.uniformes.domain.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoDao {
    void guardarEmpleado(Empleado empleado);
    Optional<Empleado> buscarPorId(Long id);
    List<Empleado> buscarTodos();
    void actualizarEmpleado(Empleado empleado);
    void eliminar(Long id);
    boolean existePorDocumento(String documento);
    List<Empleado> buscarActivos();
}

