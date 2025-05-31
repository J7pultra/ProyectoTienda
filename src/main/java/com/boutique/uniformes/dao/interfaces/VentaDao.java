package com.boutique.uniformes.dao.interfaces;

import com.boutique.uniformes.domain.Venta;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

public interface VentaDao {
    void guardar(Venta venta);
    Optional<Venta> buscarPorId(Long id);
    List<Venta> buscarTodos();
    List<Venta> buscarTodos(int page, int size);
    void actualizar(Venta venta);
    void eliminar(Long id);
    Optional<Venta> buscarPorNumeroFactura(String numeroFactura);
    List<Venta> buscarPorCliente(Long clienteId);
    List<Venta> buscarPorVendedor(Long vendedorId);
    List<Venta> buscarPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<Venta> buscarPorEstado(String estado);
    BigDecimal calcularTotalVentasPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    long contarVentasHoy();
    BigDecimal calcularVentasHoy();
    List<Venta> buscarVentasRecientes(int limite);
    String generarNumeroFactura();
}
