/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.boutique.uniformes.dao.impl;

import com.boutique.uniformes.dao.interfaces.AsistenciaDao;
import com.boutique.uniformes.domain.Asistencia;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class AsistenciaDaoImpl implements AsistenciaDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void guardar(Asistencia asistencia) {
        if (asistencia.getFecha() == null) {
            asistencia.setFecha(LocalDate.now());
        }
        entityManager.persist(asistencia);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Asistencia> buscarPorId(Long id) {
        Asistencia asistencia = entityManager.find(Asistencia.class, id);
        return Optional.ofNullable(asistencia);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asistencia> buscarTodos() {
        TypedQuery<Asistencia> query = entityManager.createQuery(
            "SELECT a FROM Asistencia a ORDER BY a.fecha DESC, a.empleado.nombre", Asistencia.class);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asistencia> buscarTodos(int page, int size) {
        TypedQuery<Asistencia> query = entityManager.createQuery(
            "SELECT a FROM Asistencia a ORDER BY a.fecha DESC, a.empleado.nombre", Asistencia.class);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    @Override
    public void actualizar(Asistencia asistencia) {
        entityManager.merge(asistencia);
    }

    @Override
    public void eliminar(Long id) {
        Asistencia asistencia = entityManager.find(Asistencia.class, id);
        if (asistencia != null) {
            entityManager.remove(asistencia);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asistencia> buscarPorEmpleado(Long empleadoId) {
        TypedQuery<Asistencia> query = entityManager.createQuery(
            "SELECT a FROM Asistencia a WHERE a.empleado.id = :empleadoId ORDER BY a.fecha DESC", Asistencia.class);
        query.setParameter("empleadoId", empleadoId);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asistencia> buscarPorFecha(LocalDate fecha) {
        TypedQuery<Asistencia> query = entityManager.createQuery(
            "SELECT a FROM Asistencia a WHERE a.fecha = :fecha ORDER BY a.empleado.nombre", Asistencia.class);
        query.setParameter("fecha", fecha);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asistencia> buscarPorEmpleadoYFecha(Long empleadoId, LocalDate fechaInicio, LocalDate fechaFin) {
        TypedQuery<Asistencia> query = entityManager.createQuery(
            "SELECT a FROM Asistencia a WHERE a.empleado.id = :empleadoId AND a.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY a.fecha DESC", 
            Asistencia.class);
        query.setParameter("empleadoId", empleadoId);
        query.setParameter("fechaInicio", fechaInicio);
        query.setParameter("fechaFin", fechaFin);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Asistencia> buscarPorEmpleadoYFechaExacta(Long empleadoId, LocalDate fecha) {
        TypedQuery<Asistencia> query = entityManager.createQuery(
            "SELECT a FROM Asistencia a WHERE a.empleado.id = :empleadoId AND a.fecha = :fecha", Asistencia.class);
        query.setParameter("empleadoId", empleadoId);
        query.setParameter("fecha", fecha);
        try {
            Asistencia asistencia = query.getSingleResult();
            return Optional.of(asistencia);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Asistencia> buscarPorEstado(String estado) {
        TypedQuery<Asistencia> query = entityManager.createQuery(
            "SELECT a FROM Asistencia a WHERE a.estado = :estado ORDER BY a.fecha DESC", Asistencia.class);
        query.setParameter("estado", estado);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarPresentesHoy() {
        LocalDate hoy = LocalDate.now();
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(a) FROM Asistencia a WHERE a.fecha = :hoy AND a.estado = 'PRESENTE'", Long.class);
        query.setParameter("hoy", hoy);
        return query.getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarAusentesHoy() {
        LocalDate hoy = LocalDate.now();
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(a) FROM Asistencia a WHERE a.fecha = :hoy AND a.estado = 'AUSENTE'", Long.class);
        query.setParameter("hoy", hoy);
        return query.getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarTardanzasHoy() {
        LocalDate hoy = LocalDate.now();
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(a) FROM Asistencia a WHERE a.fecha = :hoy AND a.estado = 'TARDANZA'", Long.class);
        query.setParameter("hoy", hoy);
        return query.getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeAsistenciaHoy(Long empleadoId) {
        LocalDate hoy = LocalDate.now();
        TypedQuery<Long> query = entityManager.createQuery(
            "SELECT COUNT(a) FROM Asistencia a WHERE a.empleado.id = :empleadoId AND a.fecha = :hoy", Long.class);
        query.setParameter("empleadoId", empleadoId);
        query.setParameter("hoy", hoy);
        return query.getSingleResult() > 0;
    }
}
