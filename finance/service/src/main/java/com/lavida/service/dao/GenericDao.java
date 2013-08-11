package com.lavida.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * GenericDao
 * <p/>
 * Created: 13:46 11.08.13
 *
 * @author Pavel
 */
public class GenericDao<T> {

    @PersistenceContext
    protected EntityManager entityManager;

    public T getById(Class<T> clazz, int id) {
        return entityManager.find(clazz, id);
    }

    public List<T> getAll(Class<T> clazz) {
        return entityManager.createQuery("select o from " + clazz.getSimpleName() + " o", clazz).getResultList();
    }

    public void put(T jdo) {
        entityManager.persist(jdo);
    }

    public void update(T jdo) {
        entityManager.merge(jdo);
    }

    public void delete(Class<T> clazz, int id) {
        T deletingJdo = entityManager.find(clazz, id);
        entityManager.remove(deletingJdo);
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
