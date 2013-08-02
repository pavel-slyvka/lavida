package com.lavida.service.dao;

import com.lavida.service.entity.Authorities;
import com.lavida.service.entity.UserJdo;

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
public class AuthoritiesDao implements Dao<Authorities>{
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
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void update(Authorities authorities) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Authorities> getAll() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(int id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
