package com.lavida.service.dao;

import com.lavida.service.entity.AuthorityJdo;

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
@Deprecated // don't need because AuthoritiesJdo also out of sense.
public class AuthoritiesDao implements Dao<AuthorityJdo> {
    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public AuthorityJdo getById(int id) {
        return entityManager.find(AuthorityJdo.class, id);
    }

    @Override
    public void put(AuthorityJdo authorities) {
        entityManager.persist(authorities);
    }

    @Override
    public void update(AuthorityJdo authorities) {
        entityManager.merge(authorities);
    }

    @Override
    public List<AuthorityJdo> getAll() {
        return entityManager.createQuery("select a from AuthorityJdo a", AuthorityJdo.class).getResultList();
    }

    @Override
    public void delete(int id) {
        AuthorityJdo deleteAuthority = entityManager.find(AuthorityJdo.class, id);
        if (deleteAuthority != null) {
            entityManager.remove(deleteAuthority);
        } else {  //todo change exception
            throw new RuntimeException("Cannot delete authority[" + id + "], because it doesn't exist.");
        }
    }
}
