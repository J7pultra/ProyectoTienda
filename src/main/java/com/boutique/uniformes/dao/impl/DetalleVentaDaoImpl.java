/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.dao.impl;

import com.boutique.uniformes.dao.interfaces.DetalleVentaDao;
import com.boutique.uniformes.domain.DetalleVenta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class DetalleVentaDaoImpl implements DetalleVentaDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void guardar(DetalleVenta detalleVenta) {
        entityManager.persist(detalleVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetalleVenta> buscarPorId(Long id) {
        DetalleVenta detalleVenta = entityManager.find(DetalleVenta.class, id);
        return Optional.ofNullable(detalleVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> buscarTodos() {
        TypedQuery<DetalleVenta> query = entityManager.createQuery(
            "SELECT dv FROM DetalleVenta dv ORDER BY dv.venta.fechaVenta DESC", DetalleVenta.class);
        return query.getResultList();
    }

    @Override
    public void actualizar(DetalleVenta detalleVenta) {
        entityManager.merge(detalleVenta);
    }

    @Override
    public void eliminar(Long id) {
        DetalleVenta detalleVenta = entityManager.find(DetalleVenta.class, id);
        if (detalleVenta != null) {
            entityManager.remove(detalleVenta);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> buscarPorVenta(Long ventaId) {
        TypedQuery<DetalleVenta> query = entityManager.createQuery(
            "SELECT dv FROM DetalleVenta dv WHERE dv.venta.id = :ventaId ORDER BY dv.id", DetalleVenta.class);
        query.setParameter("ventaId", ventaId);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> buscarPorUniforme(Long uniformeId) {
        TypedQuery<DetalleVenta> query = entityManager.createQuery(
            "SELECT dv FROM DetalleVenta dv WHERE dv.uniforme.id = :uniformeId ORDER BY dv.venta.fechaVenta DESC", 
            DetalleVenta.class);
        query.setParameter("uniformeId", uniformeId);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> buscarProductosMasVendidos(int limite) {
        TypedQuery<Object[]> query = entityManager.createQuery(
            "SELECT dv.uniforme.nombre, dv.uniforme.codigo, SUM(dv.cantidad) as totalVendido, COUNT(dv.venta.id) as numeroVentas " +
            "FROM DetalleVenta dv " +
            "WHERE dv.venta.estado = 'COMPLETADA' " +
            "GROUP BY dv.uniforme.id, dv.uniforme.nombre, dv.uniforme.codigo " +
            "ORDER BY totalVendido DESC", Object[].class);
        query.setMaxResults(limite);
        return query.getResultList();
    }
}
