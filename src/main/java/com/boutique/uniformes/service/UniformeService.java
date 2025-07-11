/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.service;


import com.boutique.uniformes.domain.Uniforme;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface UniformeService {
    Uniforme guardarUniforme(Uniforme uniforme);
    Uniforme obtenerUniformePorId(Long id);
    Uniforme obtenerUniformePorCodigo(String codigo);
    List<Uniforme> obtenerUniformesActivos();
    List<Uniforme> obtenerUniformesDisponibles();
    Page<Uniforme> obtenerUniformesPaginados(Pageable pageable);
    Page<Uniforme> buscarUniformes(String buscar, Pageable pageable);
    Page<Uniforme> obtenerUniformesPorCategoria(String categoria, Pageable pageable);
    Page<Uniforme> obtenerUniformesBajoStock(Pageable pageable);
    void eliminarUniforme(Long id);
    void actualizarStock(Long id, Integer nuevoStock);
    void reducirStock(Long id, Integer cantidad);
    Long contarUniformesActivos();
    Long contarUniformesBajoStock();
    List<String> obtenerCategorias();
    List<Map<String, Object>> obtenerUniformesMasVendidos(int limite);
    boolean existeUniformePorCodigo(String codigo);
    List<Uniforme> busquedaInteligente(String query);
    List<Uniforme> obtenerUniformesBajoStock();
}
