package com.boutique.uniformes.controller;

import com.boutique.uniformes.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class ReportesController {
    private final VentaService ventaService;
    private final UniformeService uniformeService;
    private final EmpleadoService empleadoService;
    private final AsistenciaService asistenciaService;

    @GetMapping("/reportes/ventas")
    public String reporteVentas(@RequestParam(required = false) LocalDate fechaInicio,
                                @RequestParam(required = false) LocalDate fechaFin,
                                Model model) {
        if (fechaInicio == null) fechaInicio = LocalDate.now().minusMonths(1);
        if (fechaFin == null) fechaFin = LocalDate.now();
        var ventas = ventaService.obtenerVentasEntreFechas(fechaInicio, fechaFin, PageRequest.of(0, 100));
        model.addAttribute("ventas", ventas.getContent());
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        return "reportes/ventas";
    }

    @GetMapping("/reportes/productos-mas-vendidos")
    public String productosMasVendidos(Model model) {
        model.addAttribute("productos", uniformeService.obtenerUniformesMasVendidos(10));
        return "reportes/productos-mas-vendidos";
    }

    @GetMapping("/reportes/inventario")
    public String reporteInventario(Model model) {
        model.addAttribute("uniformes", uniformeService.obtenerUniformesActivos());
        return "reportes/inventario";
    }

    @GetMapping("/reportes/asistencia")
    public String reporteAsistencia(@RequestParam(required = false) Long empleadoId,
                                    @RequestParam(required = false) LocalDate fechaInicio,
                                    @RequestParam(required = false) LocalDate fechaFin,
                                    Model model) {
        if (fechaInicio == null) fechaInicio = LocalDate.now().minusMonths(1);
        if (fechaFin == null) fechaFin = LocalDate.now();
        var asistencias = asistenciaService.listarAsistencias(empleadoId, fechaInicio, fechaFin);
        model.addAttribute("asistencias", asistencias);
        model.addAttribute("empleados", empleadoService.obtenerEmpleadosActivos());
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("empleadoId", empleadoId);
        return "reportes/asistencia";
    }
}
