package com.boutique.uniformes.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedidoDTO {
    
    private Long id;
    
    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
    
    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio unitario debe ser mayor a 0")
    private BigDecimal precioUnitario;
    
    private BigDecimal total;
    
    // Campos opcionales para referencia a producto/uniforme existente
    private Long uniformeId;
    private String codigoUniforme;
    
    // Constructor para calcular total automáticamente
    public DetallePedidoDTO(String descripcion, Integer cantidad, BigDecimal precioUnitario) {
        this.descripcion = descripcion;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        calculateTotal();
    }
    
    public void calculateTotal() {
        if (cantidad != null && precioUnitario != null) {
            this.total = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
        }
    }
    
    // Setters que recalculan el total automáticamente
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
        calculateTotal();
    }
    
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
        calculateTotal();
    }
}
