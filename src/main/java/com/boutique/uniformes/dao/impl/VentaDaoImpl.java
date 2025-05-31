/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.dao.impl;

import com.boutique.uniformes.dao.interfaces.VentaDao;
import com.boutique.uniformes.domain.Venta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class VentaDaoImpl implements VentaDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void guardar(Venta venta) {
        if (venta.getFechaVenta() == null) {
            venta.setFechaVenta(LocalDateTime.now());
        }
        if (venta.getNumeroFactura() == null) {
            venta.setNumeroFactura(generarNumeroFactura());
        }
        entityManager.persist(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> buscarPorId(Long id) {
        Venta venta = entityManager.find(Venta.class, id);
        return Optional.ofNullable(venta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> buscarTodos() {
        TypedQuery<Venta> query = entityManager.createQuery(
            "SELECT v FROM Venta v ORDER BY v.fechaVenta DESC", Venta.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> buscarTodos(int page, int size) {
        TypedQuery<Venta> query = entityManager.createQuery(
            "SELECT v FROM Venta v ORDER BY v.fechaVenta DESC", Venta.class);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public void actualizar(Venta venta) {
        entityManager.merge(venta);
    }

    @Override
    public void eliminar(Long id) {
        Venta venta = entityManager.find(Venta.class, id);
        if (venta != null) {
            entityManager.remove(venta);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> buscarPorNumeroFactura(String numeroFactura) {
        TypedQuery<Venta> query = entityManager.createQuery(
            "SELECT v FROM Venta v WHERE v.numeroFactura = :numeroFactura", Venta.class);
        query.setParameter("numeroFactura", numeroFactura);
        try {
            Venta venta = query.getSingleResult();
            return Optional.of(venta);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> buscarPorCliente(Long clienteId) {
        TypedQuery<Venta> query = entityManager.createQuery(
            "SELECT v FROM Venta v WHERE v.cliente.id = :clienteId ORDER BY v.fechaVenta DESC", Venta.class);
        query.setParameter("clienteId", clienteId);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> buscarPorVendedor(Long vendedorId) {
        TypedQuery<Venta> query = entityManager.createQuery(
            "SELECT v FROM Venta v WHERE v.vendedor.id = :vendedorId ORDER BY v.fechaVenta DESC", Venta.class);
        query.setParameter("vendedorId", vendedorId);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> buscarPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        TypedQuery<Venta> query = entityManager.createQuery(
            "SELECT v FROM Venta v WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin ORDER BY v.fechaVenta DESC", 
            Venta.class);
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> buscarPorEstado(String estado) {
        TypedQuery<Venta> query = entityManager.createQuery(
            "SELECT v FROM Venta v WHERE v.estado = :estado ORDER BY v.fechaVenta DESC", Venta.class);
        query.setParameter("estado", estado);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalVentasPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        TypedQuery<BigDecimal> query = entityManager.createQuery(
            "SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin AND v.estado = 'COMPLETADA'", 
            BigDecimal.class);
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);
        BigDecimal resultado = query.getSingleResult();
        return resultado != null ? resultado : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public long contarVentasHoy() {
        LocalDateTime inicioHoy = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finHoy = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(v) FROM Venta v WHERE v.fechaVenta BETWEEN :inicio AND :fin", Long.class);
        query.setParameter("inicio", inicioHoy);
        query.setParameter("fin", finHoy);
        return query.getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularVentasHoy() {
        LocalDateTime inicioHoy = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finHoy = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        
        TypedQuery<BigDecimal> query = entityManager.createQuery(
            "SELECT COALESCE(SUM(v.total), 0) FROM Venta v WHERE v.fechaVenta BETWEEN :inicio AND :fin AND v.estado = 'COMPLETADA'", 
            BigDecimal.class);
        query.setParameter("inicio", inicioHoy);
        query.setParameter("fin", finHoy);
        BigDecimal resultado = query.getSingleResult();
        return resultado != null ? resultado : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> buscarVentasRecientes(int limite) {
        TypedQuery<Venta> query = entityManager.createQuery(
            "SELECT v FROM Venta v ORDER BY v.fechaVenta DESC", Venta.class);
        query.setMaxResults(limite);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public String generarNumeroFactura() {
        // Obtener el último número de factura del día
        LocalDateTime inicioHoy = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime finHoy = LocalDateTime.now().withHour(23).withMinute(59).withSecond(59);
        
        TypedQuery<String> query = entityManager.createQuery(
            "SELECT v.numeroFactura FROM Venta v WHERE v.fechaVenta BETWEEN :inicio AND :fin ORDER BY v.numeroFactura DESC", 
            String.class);
        query.setParameter("inicio", inicioHoy);
        query.setParameter("fin", finHoy);
        query.setMaxResults(1);
        
        List<String> resultados = query.getResultList();
        
        String fechaHoy = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int numeroSecuencial = 1;
        
        if (!resultados.isEmpty()) {
            String ultimaFactura = resultados.get(0);
            // Formato: FAC-20240101-001
            String[] partes = ultimaFactura.split("-");
            if (partes.length == 3 && partes[1].equals(fechaHoy)) {
                numeroSecuencial = Integer.parseInt(partes[2]) + 1;
            }
        }
        
        return String.format("FAC-%s-%03d", fechaHoy, numeroSecuencial);
    }
}
