package com.boutique.uniformes.dao.interfaces;

import com.boutique.uniformes.domain.Empleado;
import java.util.List;
import java.util.Optional;

public interface EmpleadoDao {
    void guardar(Empleado empleado);
    Optional<Empleado> buscarPorId(Long id);
    List<Empleado> buscarTodos();
    List<Empleado> buscarTodos(int page, int size);
    void actualizar(Empleado empleado);
    void eliminar(Long id);
    Optional<Empleado> buscarPorDocumento(String documento);
    List<Empleado> buscarPorCargo(String cargo);
    List<Empleado> buscarActivos();
    List<Empleado> buscarPorNombre(String nombre);
    boolean existePorDocumento(String documento);
    long contarTodos();
    long contarActivos();
}
