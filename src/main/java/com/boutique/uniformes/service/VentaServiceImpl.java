/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.service;


import com.boutique.uniformes.domain.Venta;
import com.boutique.uniformes.domain.DetalleVenta;
import com.boutique.uniformes.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final UniformeService uniformeService;

    @Override
    public Venta procesarVenta(Venta venta) {
        // Validar stock disponible
        for (DetalleVenta detalle : venta.getDetalles()) {
            if (detalle.getUniforme().getStockActual() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + detalle.getUniforme().getNombre());
            }
        }

        // Calcular totales
        BigDecimal subtotal = BigDecimal.ZERO;
        for (DetalleVenta detalle : venta.getDetalles()) {
            detalle.setPrecioUnitario(detalle.getUniforme().getPrecio());
            detalle.setSubtotal(detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad())));
            subtotal = subtotal.add(detalle.getSubtotal());
            detalle.setVenta(venta);
        }

        venta.setSubtotal(subtotal);
        venta.setImpuesto(subtotal.multiply(BigDecimal.valueOf(0.19))); // IVA 19%
        venta.setTotal(venta.getSubtotal().add(venta.getImpuesto()).subtract(venta.getDescuento()));
        venta.setFechaVenta(LocalDateTime.now());
        venta.setEstado(Venta.EstadoVenta.COMPLETADA);

        // Guardar venta
        Venta ventaGuardada = ventaRepository.save(venta);

        // Reducir stock
        for (DetalleVenta detalle : venta.getDetalles()) {
            uniformeService.reducirStock(detalle.getUniforme().getId(), detalle.getCantidad());
        }

        return ventaGuardada;
    }

    @Override
    @Transactional(readOnly = true)
    public Venta obtenerVentaPorId(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Venta obtenerVentaPorNumeroFactura(String numeroFactura) {
        return ventaRepository.findByNumeroFactura(numeroFactura)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada con número de factura: " + numeroFactura));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Venta> obtenerVentasPaginadas(Pageable pageable) {
        return ventaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Venta> buscarVentas(String buscar, Pageable pageable) {
        return ventaRepository.findByNumeroFacturaContainingIgnoreCaseOrClienteNombreContainingIgnoreCaseOrClienteApellidoContainingIgnoreCase(
                buscar, buscar, buscar, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Venta> obtenerVentasEntreFechas(LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);
        return ventaRepository.findByFechaVentaBetween(inicio, fin, pageable);
    }

    @Override
    public void cancelarVenta(Long id) {
        Venta venta = obtenerVentaPorId(id);
        if (venta.getEstado() == Venta.EstadoVenta.CANCELADA) {
            throw new RuntimeException("La venta ya está cancelada");
        }

        // Restaurar stock
        for (DetalleVenta detalle : venta.getDetalles()) {
            uniformeService.actualizarStock(
                detalle.getUniforme().getId(),
                detalle.getUniforme().getStockActual() + detalle.getCantidad()
            );
        }

        venta.setEstado(Venta.EstadoVenta.CANCELADA);
        ventaRepository.save(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularVentasEntreFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);
        BigDecimal total = ventaRepository.sumTotalByFechaVentaBetween(inicio, fin);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarVentasEntreFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.atTime(23, 59, 59);
        return ventaRepository.countByFechaVentaBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerVentasUltimos7Dias() {
        return ventaRepository.findVentasUltimos7Dias();
    }

    @Override
    public Long contarVentas() {
        return ventaRepository.count();
    }
}
