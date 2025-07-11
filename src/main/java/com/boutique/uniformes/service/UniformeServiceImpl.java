/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.service;

import com.boutique.uniformes.domain.Uniforme;
import com.boutique.uniformes.repository.UniformeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class UniformeServiceImpl implements UniformeService {

    private final UniformeRepository uniformeRepository;

    @Override
    public Uniforme guardarUniforme(Uniforme uniforme) {
        if (uniforme.getId() == null) {
            if (existeUniformePorCodigo(uniforme.getCodigo())) {
                throw new RuntimeException("Ya existe un uniforme con ese código");
            }
            uniforme.setFechaRegistro(LocalDateTime.now());
            uniforme.setActivo(true);
        }
        return uniformeRepository.save(uniforme);
    }

    @Override
    @Transactional(readOnly = true)
    public Uniforme obtenerUniformePorId(Long id) {
        return uniformeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Uniforme no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Uniforme obtenerUniformePorCodigo(String codigo) {
        return uniformeRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Uniforme no encontrado con código: " + codigo));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Uniforme> obtenerUniformesActivos() {
        return uniformeRepository.findByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Uniforme> obtenerUniformesDisponibles() {
        return uniformeRepository.findByActivoTrueAndStockActualGreaterThan(0);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Uniforme> obtenerUniformesPaginados(Pageable pageable) {
        return uniformeRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Uniforme> buscarUniformes(String buscar, Pageable pageable) {
        return uniformeRepository.findByCodigoContainingIgnoreCaseOrNombreContainingIgnoreCaseOrCategoriaContainingIgnoreCase(
                buscar, buscar, buscar, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Uniforme> obtenerUniformesPorCategoria(String categoria, Pageable pageable) {
        return uniformeRepository.findByCategoriaContainingIgnoreCase(categoria, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Uniforme> obtenerUniformesBajoStock(Pageable pageable) {
        return uniformeRepository.findByStockActualLessThanEqualStockMinimo(pageable);
    }

    @Override
    public void eliminarUniforme(Long id) {
        Uniforme uniforme = obtenerUniformePorId(id);
        uniforme.setActivo(false);
        uniformeRepository.save(uniforme);
    }

    @Override
    public void actualizarStock(Long id, Integer nuevoStock) {
        Uniforme uniforme = obtenerUniformePorId(id);
        uniforme.setStockActual(nuevoStock);
        uniformeRepository.save(uniforme);
    }

    @Override
    public void reducirStock(Long id, Integer cantidad) {
        Uniforme uniforme = obtenerUniformePorId(id);
        if (uniforme.getStockActual() < cantidad) {
            throw new RuntimeException("Stock insuficiente para el uniforme: " + uniforme.getNombre());
        }
        uniforme.setStockActual(uniforme.getStockActual() - cantidad);
        uniformeRepository.save(uniforme);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarUniformesActivos() {
        return uniformeRepository.countByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarUniformesBajoStock() {
        return uniformeRepository.countByStockActualLessThanEqualStockMinimo();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> obtenerCategorias() {
        return uniformeRepository.findDistinctCategorias();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerUniformesMasVendidos(int limite) {
        Pageable pageable = PageRequest.of(0, limite);
        return uniformeRepository.findUniformesMasVendidos(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeUniformePorCodigo(String codigo) {
        return uniformeRepository.existsByCodigo(codigo);
    }

    @Override
    public List<Uniforme> busquedaInteligente(String query) {
        return uniformeRepository.busquedaInteligente(query);
    }

    @Override
    public List<Uniforme> obtenerUniformesBajoStock() {
        return uniformeRepository.findByStockActualLessThanEqualStockMinimo(org.springframework.data.domain.PageRequest.of(0, 100)).getContent();
    }
}
