package com.boutique.uniformes.service;

import com.boutique.uniformes.domain.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProveedorService {
    Proveedor guardarProveedor(Proveedor proveedor);
    Proveedor obtenerProveedorPorId(Long id);
    Proveedor obtenerProveedorPorNit(String nit);
    List<Proveedor> obtenerProveedoresActivos();
    Page<Proveedor> obtenerProveedoresPaginados(Pageable pageable);
    Page<Proveedor> buscarProveedores(String buscar, Pageable pageable);
    void eliminarProveedor(Long id);
    void cambiarEstadoProveedor(Long id, Boolean activo);
    Long contarProveedoresActivos();
    boolean existeProveedorPorNit(String nit);
    java.util.List<Proveedor> busquedaInteligente(String query);
    java.util.List<Object> obtenerHistorialCompras(Long proveedorId);
}
