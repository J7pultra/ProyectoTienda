/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.boutique.uniformes.dao.impl;

import com.boutique.uniformes.dao.interfaces.ProveedorDao;
import com.boutique.uniformes.domain.Proveedor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ProveedorDaoImpl implements ProveedorDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void guardar(Proveedor proveedor) {
        entityManager.persist(proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Proveedor> buscarPorId(Long id) {
        Proveedor proveedor = entityManager.find(Proveedor.class, id);
        return Optional.ofNullable(proveedor);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> buscarTodos() {
        TypedQuery<Proveedor> query = entityManager.createQuery(
            "SELECT p FROM Proveedor p ORDER BY p.nombre", Proveedor.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> buscarTodos(int page, int size) {
        TypedQuery<Proveedor> query = entityManager.createQuery(
            "SELECT p FROM Proveedor p ORDER BY p.nombre", Proveedor.class);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public void actualizar(Proveedor proveedor) {
        entityManager.merge(proveedor);
    }

    @Override
    public void eliminar(Long id) {
        Proveedor proveedor = entityManager.find(Proveedor.class, id);
        if (proveedor != null) {
            entityManager.remove(proveedor);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> buscarPorNombre(String nombre) {
        TypedQuery<Proveedor> query = entityManager.createQuery(
            "SELECT p FROM Proveedor p WHERE LOWER(p.nombre) LIKE LOWER(:nombre)", Proveedor.class);
        query.setParameter("nombre", "%" + nombre + "%");
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Proveedor> buscarPorNit(String nit) {
        TypedQuery<Proveedor> query = entityManager.createQuery(
            "SELECT p FROM Proveedor p WHERE p.nit = :nit", Proveedor.class);
        query.setParameter("nit", nit);
        try {
            Proveedor proveedor = query.getSingleResult();
            return Optional.of(proveedor);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> buscarActivos() {
        TypedQuery<Proveedor> query = entityManager.createQuery(
            "SELECT p FROM Proveedor p WHERE p.activo = true ORDER BY p.nombre", Proveedor.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> buscarPorCiudad(String ciudad) {
        TypedQuery<Proveedor> query = entityManager.createQuery(
            "SELECT p FROM Proveedor p WHERE LOWER(p.ciudad) = LOWER(:ciudad)", Proveedor.class);
        query.setParameter("ciudad", ciudad);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNit(String nit) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(p) FROM Proveedor p WHERE p.nit = :nit", Long.class);
        query.setParameter("nit", nit);
        return query.getSingleResult() > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public long contarTodos() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(p) FROM Proveedor p", Long.class);
        return query.getSingleResult();
    }
}
