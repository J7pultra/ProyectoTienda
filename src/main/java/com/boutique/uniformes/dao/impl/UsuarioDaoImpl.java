package com.boutique.uniformes.dao.impl;


import com.boutique.uniformes.dao.interfaces.UsuarioDao;
import com.boutique.uniformes.domain.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UsuarioDaoImpl implements UsuarioDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Usuario usuario) {
        if (usuario.getFechaCreacion() == null) {
            usuario.setFechaCreacion(LocalDateTime.now());
        }
        entityManager.persist(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        Usuario usuario = entityManager.find(Usuario.class, id);
        return Optional.ofNullable(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        TypedQuery<Usuario> query = entityManager.createQuery(
            "SELECT u FROM Usuario u ORDER BY u.nombre", Usuario.class);
        return query.getResultList();
    }

    @Override
    public void update(Usuario usuario) {
        entityManager.merge(usuario);
    }

    @Override
    public void delete(Long id) {
        Usuario usuario = entityManager.find(Usuario.class, id);
        if (usuario != null) {
            entityManager.remove(usuario);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByUsername(String username) {
        TypedQuery<Usuario> query = entityManager.createQuery(
            "SELECT u FROM Usuario u WHERE u.username = :username", Usuario.class);
        query.setParameter("username", username);
        try {
            Usuario usuario = query.getSingleResult();
            return Optional.of(usuario);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findByEmail(String email) {
        TypedQuery<Usuario> query = entityManager.createQuery(
            "SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class);
        query.setParameter("email", email);
        try {
            Usuario usuario = query.getSingleResult();
            return Optional.of(usuario);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findByRol(String rol) {
        TypedQuery<Usuario> query = entityManager.createQuery(
            "SELECT u FROM Usuario u WHERE u.rol = :rol ORDER BY u.nombre", Usuario.class);
        query.setParameter("rol", rol);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(u) FROM Usuario u WHERE u.username = :username", Long.class);
        query.setParameter("username", username);
        return query.getSingleResult() > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(u) FROM Usuario u WHERE u.email = :email", Long.class);
        query.setParameter("email", email);
        return query.getSingleResult() > 0;
    }

    @Override
    public void updateLastAccess(Long id) {
        entityManager.createQuery(
            "UPDATE Usuario u SET u.ultimoAcceso = :fecha WHERE u.id = :id")
            .setParameter("fecha", LocalDateTime.now())
            .setParameter("id", id)
            .executeUpdate();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findByNombreCompletoContaining(String nombre) {
        TypedQuery<Usuario> query = entityManager.createQuery(
            "SELECT u FROM Usuario u WHERE LOWER(CONCAT(u.nombre, ' ', u.apellido)) LIKE LOWER(:nombre) ORDER BY u.nombre", 
            Usuario.class);
        query.setParameter("nombre", "%" + nombre + "%");
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findEmpleados() {
        TypedQuery<Usuario> query = entityManager.createQuery(
            "SELECT u FROM Usuario u WHERE u.rol IN ('EMPLEADO', 'VENDEDOR', 'ADMIN') ORDER BY u.nombre", 
            Usuario.class);
        return query.getResultList();
    }

    // MÉTODO FALTANTE - Implementar findAdministradores()
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAdministradores() {
        TypedQuery<Usuario> query = entityManager.createQuery(
            "SELECT u FROM Usuario u WHERE u.rol = 'ADMIN' ORDER BY u.nombre", 
            Usuario.class);
        return query.getResultList();
    }
}
