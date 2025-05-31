package com.boutique.uniformes.dao.interfaces;

import com.boutique.uniformes.domain.Uniforme;
import java.util.List;
import java.util.Optional;

public interface UniformeDao {
    void guardar(Uniforme uniforme);
    Optional<Uniforme> buscarPorId(Long id);
    List<Uniforme> buscarTodos();
    List<Uniforme> buscarTodos(int page, int size);
    void actualizar(Uniforme uniforme);
    void eliminar(Long id);
    Optional<Uniforme> buscarPorCodigo(String codigo);
    List<Uniforme> buscarPorNombre(String nombre);
    List<Uniforme> buscarPorCategoria(String categoria);
    List<Uniforme> buscarPorTalla(String talla);
    List<Uniforme> buscarConStockBajo(int stockMinimo);
    List<Uniforme> buscarDisponibles();
    void actualizarStock(Long id, int nuevoStock);
    long contarTodos();
    long contarConStockBajo(int stockMinimo);
}

