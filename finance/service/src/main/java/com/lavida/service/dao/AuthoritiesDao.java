package com.lavida.service.dao;

import com.lavida.service.entity.Authorities;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 02.08.13
 * Time: 9:20
 * To change this template use File | Settings | File Templates.
 */
public class AuthoritiesDao implements Dao<Authorities> {
    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Authorities getById(int id) {
        return entityManager.find(Authorities.class, id);
    }

    @Override
    public void put(Authorities authorities) {
        entityManager.persist(authorities);
    }

    @Override
    public void update(Authorities authorities) {
        entityManager.merge(authorities);
    }

    @Override
    public List<Authorities> getAll() {
        return entityManager.createQuery("select a from Authorities a", Authorities.class).getResultList();
    }

    @Override
    public void delete(int id) {
        Authorities deleteAuthority = entityManager.find(Authorities.class, id);
        if (deleteAuthority != null) {
            entityManager.remove(deleteAuthority);
        } else {  //todo change exception
            throw new RuntimeException("Cannot delete authority[" + id + "], because it doesn't exist.");
        }
    }
}
