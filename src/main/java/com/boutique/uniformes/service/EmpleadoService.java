package com.boutique.uniformes.service;

import com.boutique.uniformes.domain.Empleado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EmpleadoService {
    Empleado guardarEmpleado(Empleado empleado);
    Empleado obtenerEmpleadoPorId(Long id);
    Empleado obtenerEmpleadoPorDocumento(String documento);
    List<Empleado> obtenerEmpleadosActivos();
    Page<Empleado> obtenerEmpleadosPaginados(Pageable pageable);
    Page<Empleado> buscarEmpleados(String buscar, Pageable pageable);
    void eliminarEmpleado(Long id);
    void cambiarEstadoEmpleado(Long id, Boolean activo);
    Long contarEmpleadosActivos();
    boolean existeEmpleadoPorDocumento(String documento);
    java.util.List<Empleado> busquedaInteligente(String query);
    Empleado buscarPorCedulaParaAsistencia(String cedula);
}
