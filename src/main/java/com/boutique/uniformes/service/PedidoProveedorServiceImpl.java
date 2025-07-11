package com.boutique.uniformes.service;

import com.boutique.uniformes.domain.PedidoProveedor;
import com.boutique.uniformes.domain.DetallePedidoProveedor;
import com.boutique.uniformes.domain.Uniforme;
import com.boutique.uniformes.domain.Proveedor;
import com.boutique.uniformes.dto.PedidoProveedorDTO;
import com.boutique.uniformes.dto.DetallePedidoDTO;
import com.boutique.uniformes.repository.PedidoProveedorRepository;
import com.boutique.uniformes.repository.DetallePedidoProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoProveedorServiceImpl implements PedidoProveedorService {

    @Autowired
    private PedidoProveedorRepository pedidoProveedorRepository;

    @Autowired
    private DetallePedidoProveedorRepository detallePedidoProveedorRepository;

    @Autowired
    private UniformeService uniformeService;
    
    @Autowired
    private ProveedorService proveedorService;

    @Override
    @Transactional
    public PedidoProveedor guardarPedido(PedidoProveedor pedido, List<DetallePedidoProveedor> detalles) {
        // Validar que el precio de compra sea menor al precio de venta
        for (DetallePedidoProveedor detalle : detalles) {
            Uniforme uniforme = uniformeService.obtenerUniformePorId(detalle.getUniforme().getId());
            if (uniforme == null) {
                throw new IllegalArgumentException("Uniforme no encontrado");
            }
            if (detalle.getPrecioUnitario() == null || uniforme.getPrecio() == null) {
                throw new IllegalArgumentException("Precio de compra o venta no válido");
            }
            if (detalle.getPrecioUnitario().compareTo(uniforme.getPrecio()) >= 0) {
                throw new IllegalArgumentException("El precio de compra debe ser menor al precio de venta del producto: " + uniforme.getNombre());
            }
        }
        // No necesitamos setFecha aquí ya que usamos fechaPedido y fechaEntrega por separado
        pedido.setEstado("PENDIENTE");
        PedidoProveedor savedPedido = pedidoProveedorRepository.save(pedido);
        for (DetallePedidoProveedor detalle : detalles) {
            detalle.setPedidoProveedor(savedPedido);
            detallePedidoProveedorRepository.save(detalle);
        }
        savedPedido.setDetalles(detalles);
        return savedPedido;
    }

    @Override
    public List<PedidoProveedor> listarPedidos() {
        return pedidoProveedorRepository.findAll();
    }

    @Override
    public PedidoProveedor obtenerPedidoPorId(Long id) {
        return pedidoProveedorRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void marcarComoCompletado(Long id) {
        PedidoProveedor pedido = pedidoProveedorRepository.findById(id).orElse(null);
        if (pedido != null && "PENDIENTE".equals(pedido.getEstado())) {
            pedido.setEstado("COMPLETADO");
            // Sumar al inventario cada uniforme del pedido
            if (pedido.getDetalles() != null) {
                pedido.getDetalles().forEach(detalle -> {
                    if (detalle.getUniforme() != null && detalle.getCantidad() != null) {
                        Uniforme uniforme = uniformeService.obtenerUniformePorId(detalle.getUniforme().getId());
                        if (uniforme != null) {
                            int nuevoStock = (uniforme.getStockActual() != null ? uniforme.getStockActual() : 0) + detalle.getCantidad();
                            uniformeService.actualizarStock(uniforme.getId(), nuevoStock);
                        }
                    }
                });
            }
            pedidoProveedorRepository.save(pedido);
        }
    }

    @Override
    public List<PedidoProveedor> buscarPorNitProveedor(String nit) {
        return pedidoProveedorRepository.findByProveedor_Nit(nit);
    }

    @Override
    public List<PedidoProveedor> buscarPorNombreProveedor(String nombre) {
        return pedidoProveedorRepository.findByProveedor_NombreContainingIgnoreCase(nombre);
    }

    @Override
    public String generarNumeroPedido() {
        LocalDateTime now = LocalDateTime.now();
        String fecha = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        
        // Contar pedidos del día para generar secuencial
        long pedidosDelDia = pedidoProveedorRepository.countByNumeroPedidoStartingWith("PED-" + fecha);
        String secuencial = String.format("%03d", pedidosDelDia + 1);
        
        return "PED-" + fecha + "-" + secuencial;
    }

    @Override
    @Transactional
    public PedidoProveedorDTO guardarPedidoDesdeDTO(PedidoProveedorDTO pedidoDTO) {
        // Validar proveedor
        Proveedor proveedor = proveedorService.obtenerProveedorPorNit(pedidoDTO.getCedulaProveedor());
        if (!proveedor.getActivo()) {
            throw new IllegalArgumentException("El proveedor está inactivo");
        }

        // Crear entidad PedidoProveedor
        PedidoProveedor pedido = new PedidoProveedor();
        pedido.setNumeroPedido(pedidoDTO.getNumeroPedido());
        pedido.setProveedor(proveedor);
        pedido.setFechaPedido(pedidoDTO.getFechaPedido());
        pedido.setFechaEntrega(pedidoDTO.getFechaEntrega());
        pedido.setPrioridad(pedidoDTO.getPrioridad());
        pedido.setCondicionesPago(pedidoDTO.getCondicionesPago());
        pedido.setSubtotal(pedidoDTO.getSubtotal());
        pedido.setImpuestoPorcentaje(pedidoDTO.getImpuestoPorcentaje());
        pedido.setImpuestoMonto(pedidoDTO.getImpuestoMonto());
        pedido.setTotal(pedidoDTO.getTotal());
        pedido.setEstado(pedidoDTO.getEstado());
        
        // Obtener usuario actual
        String usuarioActual = obtenerUsuarioActual();
        pedido.setUsuarioCreador(usuarioActual);

        // Guardar pedido
        PedidoProveedor savedPedido = pedidoProveedorRepository.save(pedido);

        // Crear y guardar detalles
        List<DetallePedidoProveedor> detalles = pedidoDTO.getItems().stream()
            .map(detalleDTO -> {
                DetallePedidoProveedor detalle = new DetallePedidoProveedor();
                detalle.setPedidoProveedor(savedPedido);
                detalle.setDescripcion(detalleDTO.getDescripcion());
                detalle.setCantidad(detalleDTO.getCantidad());
                detalle.setPrecioUnitario(detalleDTO.getPrecioUnitario());
                detalle.setTotal(detalleDTO.getTotal());
                
                // Si hay referencia a uniforme existente
                if (detalleDTO.getUniformeId() != null) {
                    try {
                        Uniforme uniforme = uniformeService.obtenerUniformePorId(detalleDTO.getUniformeId());
                        detalle.setUniforme(uniforme);
                        detalle.setCodigoUniforme(uniforme.getCodigo());
                    } catch (Exception e) {
                        // Si no se encuentra el uniforme, continuar sin referencia
                    }
                }
                
                return detallePedidoProveedorRepository.save(detalle);
            })
            .collect(Collectors.toList());

        savedPedido.setDetalles(detalles);

        // Convertir a DTO para respuesta
        return convertirADTO(savedPedido);
    }

    @Override
    @Transactional(readOnly = true)
    public PedidoProveedorDTO obtenerPedidoDTOPorId(Long id) {
        PedidoProveedor pedido = pedidoProveedorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID " + id));
        
        return convertirADTO(pedido);
    }

    @Override
    @Transactional
    public void enviarPedido(Long id) {
        PedidoProveedor pedido = pedidoProveedorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID " + id));
        
        if (!"PENDIENTE".equals(pedido.getEstado())) {
            throw new IllegalStateException("Solo se pueden enviar pedidos en estado PENDIENTE");
        }
        
        pedido.setEstado("ENVIADO");
        pedidoProveedorRepository.save(pedido);
    }

    @Override
    @Transactional
    public void cancelarPedido(Long id) {
        PedidoProveedor pedido = pedidoProveedorRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID " + id));
        
        if ("COMPLETADO".equals(pedido.getEstado())) {
            throw new IllegalStateException("No se puede cancelar un pedido completado");
        }
        
        pedido.setEstado("CANCELADO");
        pedidoProveedorRepository.save(pedido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoProveedorDTO> listarPedidosDTO() {
        return pedidoProveedorRepository.findAll().stream()
            .map(this::convertirADTO)
            .collect(Collectors.toList());
    }

    // Métodos auxiliares
    private PedidoProveedorDTO convertirADTO(PedidoProveedor pedido) {
        PedidoProveedorDTO dto = new PedidoProveedorDTO();
        dto.setId(pedido.getId());
        dto.setNumeroPedido(pedido.getNumeroPedido());
        dto.setCedulaProveedor(pedido.getProveedor().getNit());
        dto.setNombreProveedor(pedido.getProveedor().getNombre());
        dto.setEmailProveedor(pedido.getProveedor().getEmail());
        dto.setTelefonoProveedor(pedido.getProveedor().getTelefono());
        dto.setFechaPedido(pedido.getFechaPedido());
        dto.setFechaEntrega(pedido.getFechaEntrega());
        dto.setPrioridad(pedido.getPrioridad());
        dto.setCondicionesPago(pedido.getCondicionesPago());
        dto.setSubtotal(pedido.getSubtotal());
        dto.setImpuestoPorcentaje(pedido.getImpuestoPorcentaje());
        dto.setImpuestoMonto(pedido.getImpuestoMonto());
        dto.setTotal(pedido.getTotal());
        dto.setEstado(pedido.getEstado());
        dto.setUsuarioCreador(pedido.getUsuarioCreador());
        dto.setFechaCreacion(pedido.getFechaCreacion());
        dto.setFechaActualizacion(pedido.getFechaActualizacion());

        // Convertir detalles
        if (pedido.getDetalles() != null) {
            List<DetallePedidoDTO> detallesDTO = pedido.getDetalles().stream()
                .map(detalle -> {
                    DetallePedidoDTO detalleDTO = new DetallePedidoDTO();
                    detalleDTO.setId(detalle.getId());
                    detalleDTO.setDescripcion(detalle.getDescripcion());
                    detalleDTO.setCantidad(detalle.getCantidad());
                    detalleDTO.setPrecioUnitario(detalle.getPrecioUnitario());
                    detalleDTO.setTotal(detalle.getTotal());
                    
                    if (detalle.getUniforme() != null) {
                        detalleDTO.setUniformeId(detalle.getUniforme().getId());
                        detalleDTO.setCodigoUniforme(detalle.getUniforme().getCodigo());
                    }
                    
                    return detalleDTO;
                })
                .collect(Collectors.toList());
            
            dto.setItems(detallesDTO);
        }

        return dto;
    }

    private String obtenerUsuarioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "sistema";
    }
}
