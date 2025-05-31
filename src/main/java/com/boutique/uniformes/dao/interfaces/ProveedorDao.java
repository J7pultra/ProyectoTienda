package com.boutique.uniformes.dao.interfaces;

import com.boutique.uniformes.domain.Proveedor;
import java.util.List;
import java.util.Optional;

public interface ProveedorDao {
    void guardar(Proveedor proveedor);
    Optional<Proveedor> buscarPorId(Long id);
    List<Proveedor> buscarTodos();
    List<Proveedor> buscarTodos(int page, int size);
    void actualizar(Proveedor proveedor);
    void eliminar(Long id);
    List<Proveedor> buscarPorNombre(String nombre);
    Optional<Proveedor> buscarPorNit(String nit);
    List<Proveedor> buscarActivos();
    List<Proveedor> buscarPorCiudad(String ciudad);
    boolean existePorNit(String nit);
    long contarTodos();
}
