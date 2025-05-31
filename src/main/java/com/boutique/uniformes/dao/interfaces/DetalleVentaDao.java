package com.boutique.uniformes.dao.interfaces;

import com.boutique.uniformes.domain.DetalleVenta;
import java.util.List;
import java.util.Optional;

public interface DetalleVentaDao {
    void guardar(DetalleVenta detalleVenta);
    Optional<DetalleVenta> buscarPorId(Long id);
    List<DetalleVenta> buscarTodos();
    void actualizar(DetalleVenta detalleVenta);
    void eliminar(Long id);
    List<DetalleVenta> buscarPorVenta(Long ventaId);
    List<DetalleVenta> buscarPorUniforme(Long uniformeId);
    List<Object[]> buscarProductosMasVendidos(int limite);
}
