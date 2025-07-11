package com.boutique.uniformes.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorResponseDTO {
    
    private Long id;
    private String cedula;
    private String nombre;
    private String email;
    private String telefono;
    private String direccion;
    private String ciudad;
    private String contacto;
    private Boolean activo;
    
    // Constructor básico para búsqueda por cédula
    public ProveedorResponseDTO(String cedula, String nombre, String email, String telefono) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.activo = true;
    }
}
