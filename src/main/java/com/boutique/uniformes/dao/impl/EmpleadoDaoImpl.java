package com.boutique.uniformes.dao.impl;
import com.boutique.uniformes.dao.interfaces.EmpleadoDao;
import com.boutique.uniformes.domain.Empleado;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate; // Cambiar a LocalDate si es necesario
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class EmpleadoDaoImpl implements EmpleadoDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void guardar(Empleado empleado) {
        // Usar LocalDate en lugar de LocalDateTime si tu entidad usa LocalDate
        if (empleado.getFechaIngreso() == null) {
            empleado.setFechaIngreso(LocalDate.now()); // o LocalDateTime.now() según tu entidad
        }
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
        TypedQuery<Empleado> query = entityManager.createQuery(
            "SELECT e FROM Empleado e ORDER BY e.nombre", Empleado.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> buscarTodos(int page, int size) {
        TypedQuery<Empleado> query = entityManager.createQuery(
            "SELECT e FROM Empleado e ORDER BY e.nombre", Empleado.class);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public void actualizar(Empleado empleado) {
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
    public Optional<Empleado> buscarPorDocumento(String documento) {
        TypedQuery<Empleado> query = entityManager.createQuery(
            "SELECT e FROM Empleado e WHERE e.documento = :documento", Empleado.class);
        query.setParameter("documento", documento);
        try {
            Empleado empleado = query.getSingleResult();
            return Optional.of(empleado);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> buscarPorCargo(String cargo) {
        TypedQuery<Empleado> query = entityManager.createQuery(
            "SELECT e FROM Empleado e WHERE e.cargo = :cargo ORDER BY e.nombre", Empleado.class);
        query.setParameter("cargo", cargo);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> buscarActivos() {
        TypedQuery<Empleado> query = entityManager.createQuery(
            "SELECT e FROM Empleado e WHERE e.activo = true ORDER BY e.nombre", Empleado.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Empleado> buscarPorNombre(String nombre) {
        TypedQuery<Empleado> query = entityManager.createQuery(
            "SELECT e FROM Empleado e WHERE LOWER(e.nombre) LIKE LOWER(:nombre) OR LOWER(e.apellido) LIKE LOWER(:nombre) ORDER BY e.nombre", 
            Empleado.class);
        query.setParameter("nombre", "%" + nombre + "%");
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorDocumento(String documento) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(e) FROM Empleado e WHERE e.documento = :documento", Long.class);
        query.setParameter("documento", documento);
        return query.getSingleResult() > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public long contarTodos() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(e) FROM Empleado e", Long.class);
        return query.getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarActivos() {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(e) FROM Empleado e WHERE e.activo = true", Long.class);
        return query.getSingleResult();
    }
}
