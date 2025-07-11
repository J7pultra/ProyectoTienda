package com.boutique.uniformes.controller;

import com.boutique.uniformes.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {
    private final UsuarioService usuarioService;
    private final ClienteService clienteService;
    private final EmpleadoService empleadoService;
    private final UniformeService uniformeService;
    private final VentaService ventaService;
    private final AsistenciaService asistenciaService;
    private final ProveedorService proveedorService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalUsuarios", usuarioService.contarUsuariosActivos());
        model.addAttribute("totalClientes", clienteService.obtenerClientesActivos().size());
        model.addAttribute("totalEmpleados", empleadoService.contarEmpleadosActivos());
        model.addAttribute("totalUniformes", uniformeService.contarUniformesActivos());
        model.addAttribute("totalVentas", ventaService.contarVentas());
        model.addAttribute("totalProveedores", proveedorService.contarProveedoresActivos());
        model.addAttribute("totalAsistencias", asistenciaService.contarAsistencias());
        model.addAttribute("uniformesBajoStock", uniformeService.contarUniformesBajoStock());
        model.addAttribute("productosMasVendidos", uniformeService.obtenerUniformesMasVendidos(5));
        // Agrega más estadísticas y reportes según necesidad
        return "dashboard/index";
    }
}
