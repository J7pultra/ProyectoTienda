package com.boutique.uniformes.controller;

import com.boutique.uniformes.domain.Asistencia;
import com.boutique.uniformes.domain.Empleado;
import com.boutique.uniformes.service.AsistenciaService;
import com.boutique.uniformes.service.EmpleadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequestMapping("/asistencias")
@RequiredArgsConstructor
public class AsistenciaController {

    private final AsistenciaService asistenciaService;
    private final EmpleadoService empleadoService;

    @GetMapping
    public String listarAsistencias(
            Model model,
            @RequestParam(required = false) Long empleadoId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        if (fechaInicio == null) fechaInicio = LocalDate.now().minusDays(7);
        if (fechaFin == null) fechaFin = LocalDate.now();
        List<Asistencia> asistencias = asistenciaService.listarAsistencias(empleadoId, fechaInicio, fechaFin);
        model.addAttribute("asistencias", asistencias);
        model.addAttribute("empleados", empleadoService.obtenerEmpleadosActivos());
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("empleadoId", empleadoId);
        model.addAttribute("asistencia", new Asistencia()); // Para el modal
        return "asistencias/asistencias";
    }

    @GetMapping("/nuevo")
    public String nuevoFormulario(Model model) {
        model.addAttribute("asistencia", new Asistencia());
        model.addAttribute("empleados", empleadoService.obtenerEmpleadosActivos());
        List<Asistencia> asistencias = asistenciaService.listarAsistencias(null, LocalDate.now().minusDays(7), LocalDate.now());
        model.addAttribute("asistencias", asistencias);
        return "asistencias/asistencias";
    }

    @PostMapping("/guardar")
    public String guardarAsistencia(@ModelAttribute Asistencia asistencia,
                                   RedirectAttributes redirectAttributes,
                                   Model model) {
        try {
            asistenciaService.registrarEntrada(asistencia);
            redirectAttributes.addFlashAttribute("success", "Asistencia registrada correctamente");
            return "redirect:/asistencias";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("asistencia", asistencia);
            model.addAttribute("empleados", empleadoService.obtenerEmpleadosActivos());
            List<Asistencia> asistencias = asistenciaService.listarAsistencias(null, LocalDate.now().minusDays(7), LocalDate.now());
            model.addAttribute("asistencias", asistencias);
            return "asistencias/lista";
        }
    }

    @GetMapping("/editar/{id}")
    public String editarFormulario(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Asistencia asistencia = asistenciaService.obtenerPorId(id);
            model.addAttribute("asistencia", asistencia);
            model.addAttribute("empleados", empleadoService.obtenerEmpleadosActivos());
            List<Asistencia> asistencias = asistenciaService.listarAsistencias(null, LocalDate.now().minusDays(7), LocalDate.now());
            model.addAttribute("asistencias", asistencias);
            return "asistencias/asistencias";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/asistencias";
        }
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarAsistencia(@PathVariable Long id,
                                       @ModelAttribute Asistencia asistencia,
                                       RedirectAttributes redirectAttributes) {
        try {
            asistenciaService.actualizarAsistencia(id, asistencia);
            redirectAttributes.addFlashAttribute("success", "Asistencia actualizada correctamente");
            return "redirect:/asistencias";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/asistencias/editar/" + id;
        }
    }

    @PostMapping("/marcar-salida/{id}")
    public String marcarSalida(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            asistenciaService.marcarSalida(id, LocalTime.now());
            redirectAttributes.addFlashAttribute("success", "Salida marcada correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/asistencias";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarAsistencia(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            asistenciaService.eliminarAsistencia(id);
            redirectAttributes.addFlashAttribute("success", "Asistencia eliminada correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/asistencias";
    }

    @GetMapping("/buscar-empleado")
    @ResponseBody
    public Empleado buscarEmpleadoPorCedula(@RequestParam String cedula) {
        return empleadoService.buscarPorCedulaParaAsistencia(cedula);
    }

    @GetMapping("/reporte")
    public String reporteAsistencia(@RequestParam(required = false) Long empleadoId,
                                    @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate fechaInicio,
                                    @RequestParam(required = false) @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.time.LocalDate fechaFin,
                                    Model model) {
        if (fechaInicio == null) fechaInicio = java.time.LocalDate.now().minusDays(30);
        if (fechaFin == null) fechaFin = java.time.LocalDate.now();
        java.util.List<Asistencia> asistencias = asistenciaService.listarAsistencias(empleadoId, fechaInicio, fechaFin);
        model.addAttribute("asistencias", asistencias);
        model.addAttribute("empleados", empleadoService.obtenerEmpleadosActivos());
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("empleadoId", empleadoId);
        return "asistencias/reporte";
    }
}
