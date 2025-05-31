/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.service;


import com.boutique.uniformes.domain.Venta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface VentaService {
    Venta procesarVenta(Venta venta);
    Venta obtenerVentaPorId(Long id);
    Venta obtenerVentaPorNumeroFactura(String numeroFactura);
    Page<Venta> obtenerVentasPaginadas(Pageable pageable);
    Page<Venta> buscarVentas(String buscar, Pageable pageable);
    Page<Venta> obtenerVentasEntreFechas(LocalDate fechaInicio, LocalDate fechaFin, Pageable pageable);
    void cancelarVenta(Long id);
    BigDecimal calcularVentasEntreFechas(LocalDate fechaInicio, LocalDate fechaFin);
    Long contarVentasEntreFechas(LocalDate fechaInicio, LocalDate fechaFin);
    List<Map<String, Object>> obtenerVentasUltimos7Dias();
}
