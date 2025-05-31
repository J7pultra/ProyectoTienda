package com.boutique.uniformes.dao.interfaces;

import com.boutique.uniformes.domain.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteDao {
    void guardar(Cliente cliente);
    Optional<Cliente> buscarPorId(Long id);
    List<Cliente> buscarTodos();
    List<Cliente> buscarTodos(int page, int size);
    void actualizar(Cliente cliente);
    void eliminar(Long id);
    List<Cliente> buscarPorNombre(String nombre);
    Optional<Cliente> buscarPorDocumento(String documento);
    List<Cliente> buscarPorEstado(boolean activo);
    List<Cliente> buscarPorCiudad(String ciudad);
    long contarTodos();
    boolean existePorDocumento(String documento);
}
