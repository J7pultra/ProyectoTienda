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
    public void guardarProveedor(Proveedor proveedor) {
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
        TypedQuery<Proveedor> query = entityManager.createQuery("SELECT p FROM Proveedor p ORDER BY p.nombre", Proveedor.class);
        return query.getResultList();
    }

    @Override
    public void actualizarProveedor(Proveedor proveedor) {
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
    public boolean existePorNit(String nit) {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(p) FROM Proveedor p WHERE p.nit = :nit", Long.class);
        query.setParameter("nit", nit);
        return query.getSingleResult() > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Proveedor> buscarActivos() {
        TypedQuery<Proveedor> query = entityManager.createQuery("SELECT p FROM Proveedor p WHERE p.activo = true ORDER BY p.nombre", Proveedor.class);
        return query.getResultList();
    }
}

