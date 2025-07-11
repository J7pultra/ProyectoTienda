package com.boutique.uniformes.dao.impl;

import com.boutique.uniformes.dao.interfaces.EmpleadoDao;
import com.boutique.uniformes.domain.Empleado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class EmpleadoDaoImpl implements EmpleadoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void guardarEmpleado(Empleado empleado) {
        entityManager.persist(empleado);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Empleado> buscarPorId(Long id) {
        Empleado empleado = entityManager.find(Empleado.class, id);
        return Optional.ofNullable(empleado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> buscarTodos() {
        TypedQuery<Empleado> query = entityManager.createQuery("SELECT e FROM Empleado e ORDER BY e.nombreCompleto", Empleado.class);
        return query.getResultList();
    }

    @Override
    public void actualizarEmpleado(Empleado empleado) {
        entityManager.merge(empleado);
    }

    @Override
    public void eliminar(Long id) {
        Empleado empleado = entityManager.find(Empleado.class, id);
        if (empleado != null) {
            entityManager.remove(empleado);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorDocumento(String documento) {
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(e) FROM Empleado e WHERE e.documento = :documento", Long.class);
        query.setParameter("documento", documento);
        return query.getSingleResult() > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> buscarActivos() {
        TypedQuery<Empleado> query = entityManager.createQuery("SELECT e FROM Empleado e WHERE e.activo = true ORDER BY e.nombreCompleto", Empleado.class);
        return query.getResultList();
    }
}
