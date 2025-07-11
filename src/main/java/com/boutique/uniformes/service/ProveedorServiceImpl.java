package com.boutique.uniformes.service;

import com.boutique.uniformes.domain.Proveedor;
import com.boutique.uniformes.repository.ProveedorRepository;
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
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    @Override
    public Proveedor guardarProveedor(Proveedor proveedor) {
        if (proveedor.getId() == null) {
            if (existeProveedorPorNit(proveedor.getNit())) {
                throw new RuntimeException("Ya existe un proveedor con ese NIT/RUC");
            }
            proveedor.setFechaRegistro(LocalDateTime.now());
            proveedor.setActivo(true);
        }
        return proveedorRepository.save(proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public Proveedor obtenerProveedorPorId(Long id) {
        return proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Proveedor obtenerProveedorPorNit(String nit) {
        return proveedorRepository.findByNit(nit)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado con NIT/RUC " + nit));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> obtenerProveedoresActivos() {
        return proveedorRepository.findByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Proveedor> obtenerProveedoresPaginados(Pageable pageable) {
        return proveedorRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Proveedor> buscarProveedores(String buscar, Pageable pageable) {
        return proveedorRepository.findByNombreContainingIgnoreCaseOrNitContainingIgnoreCaseOrContactoContainingIgnoreCase(buscar, buscar, buscar, pageable);
    }

    @Override
    public void eliminarProveedor(Long id) {
        Proveedor proveedor = obtenerProveedorPorId(id);
        proveedor.setActivo(false);
        proveedorRepository.save(proveedor);
    }

    @Override
    public void cambiarEstadoProveedor(Long id, Boolean activo) {
        Proveedor proveedor = obtenerProveedorPorId(id);
        proveedor.setActivo(activo);
        proveedorRepository.save(proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarProveedoresActivos() {
        return proveedorRepository.countByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeProveedorPorNit(String nit) {
        return proveedorRepository.existsByNit(nit);
    }

    @Override
    public java.util.List<Proveedor> busquedaInteligente(String query) {
        return proveedorRepository.busquedaInteligente(query);
    }

    @Override
    public java.util.List<Object> obtenerHistorialCompras(Long proveedorId) {
        // Implementar consulta real de historial de compras por proveedor
        // Ejemplo: return compraRepository.findByProveedorId(proveedorId);
        return java.util.List.of(); // Placeholder
    }
}
