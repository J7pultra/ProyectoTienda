package com.boutique.uniformes.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoProveedorDTO {
    
    private Long id;
    
    @NotBlank(message = "La cédula del proveedor es obligatoria")
    private String cedulaProveedor;
    
    private String nombreProveedor;
    private String emailProveedor;
    private String telefonoProveedor;
    
    @NotBlank(message = "El número de pedido es obligatorio")
    private String numeroPedido;
    
    @NotNull(message = "La fecha del pedido es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaPedido;
    
    @NotNull(message = "La fecha de entrega es obligatoria")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaEntrega;
    
    @NotBlank(message = "La prioridad es obligatoria")
    private String prioridad; // baja, media, alta, urgente
    
    @NotEmpty(message = "Debe incluir al menos un item")
    private List<DetallePedidoDTO> items;
    
    private String condicionesPago;
    
    @DecimalMin(value = "0.0", message = "El subtotal debe ser mayor o igual a 0")
    private BigDecimal subtotal;
    
    @DecimalMin(value = "0.0", message = "El porcentaje de impuesto debe ser mayor o igual a 0")
    @DecimalMax(value = "100.0", message = "El porcentaje de impuesto debe ser menor o igual a 100")
    private BigDecimal impuestoPorcentaje = BigDecimal.ZERO;
    
    @DecimalMin(value = "0.0", message = "El monto del impuesto debe ser mayor o igual a 0")
    private BigDecimal impuestoMonto = BigDecimal.ZERO;
    
    @DecimalMin(value = "0.0", message = "El total debe ser mayor o igual a 0")
    private BigDecimal total;
    
    private String estado = "PENDIENTE"; // PENDIENTE, ENVIADO, COMPLETADO, CANCELADO
    
    private String usuarioCreador;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaCreacion;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaActualizacion;
}
