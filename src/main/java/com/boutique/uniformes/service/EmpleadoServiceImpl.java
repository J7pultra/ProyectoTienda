package com.boutique.uniformes.service;

import com.boutique.uniformes.domain.Empleado;
import com.boutique.uniformes.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    @Override
    public Empleado guardarEmpleado(Empleado empleado) {
        if (empleado.getId() == null) {
            if (existeEmpleadoPorDocumento(empleado.getDocumento())) {
                throw new RuntimeException("Ya existe un empleado con ese documento");
            }
            empleado.setFechaRegistro(LocalDateTime.now());
            empleado.setActivo(true);
        }
        return empleadoRepository.save(empleado);
    }

    @Override
    @Transactional(readOnly = true)
    public Empleado obtenerEmpleadoPorId(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Empleado obtenerEmpleadoPorDocumento(String documento) {
        return empleadoRepository.findByDocumento(documento)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado con documento " + documento));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> obtenerEmpleadosActivos() {
        return empleadoRepository.findAll().stream().filter(Empleado::getActivo).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Empleado> obtenerEmpleadosPaginados(Pageable pageable) {
        return empleadoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Empleado> buscarEmpleados(String buscar, Pageable pageable) {
        // Puedes implementar búsqueda por nombre, apellido o documento si tienes método en repo
        return empleadoRepository.findAll(pageable); // Temporal, reemplazar si implementas búsqueda
    }

    @Override
    public void eliminarEmpleado(Long id) {
        Empleado empleado = obtenerEmpleadoPorId(id);
        empleado.setActivo(false);
        empleadoRepository.save(empleado);
    }

    @Override
    public void cambiarEstadoEmpleado(Long id, Boolean activo) {
        Empleado empleado = obtenerEmpleadoPorId(id);
        empleado.setActivo(activo);
        empleadoRepository.save(empleado);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarEmpleadosActivos() {
        return empleadoRepository.findAll().stream().filter(Empleado::getActivo).count();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeEmpleadoPorDocumento(String documento) {
        return empleadoRepository.existsByDocumento(documento);
    }

    @Override
    public java.util.List<Empleado> busquedaInteligente(String query) {
        return empleadoRepository.busquedaInteligente(query);
    }

    @Override
    public Empleado buscarPorCedulaParaAsistencia(String cedula) {
        return empleadoRepository.findByDocumentoAndActivoTrue(cedula)
                .orElseThrow(() -> new RuntimeException("Empleado no encontrado o inactivo"));
    }
}

