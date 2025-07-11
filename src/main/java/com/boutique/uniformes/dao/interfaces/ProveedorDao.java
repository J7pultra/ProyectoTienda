package com.boutique.uniformes.dao.interfaces;

import com.boutique.uniformes.domain.Proveedor;

import java.util.List;
import java.util.Optional;

public interface ProveedorDao {
    void guardarProveedor(Proveedor proveedor);
    Optional<Proveedor> buscarPorId(Long id);
    List<Proveedor> buscarTodos();
    void actualizarProveedor(Proveedor proveedor);
    void eliminar(Long id);
    boolean existePorNit(String nit);
    List<Proveedor> buscarActivos();
}
