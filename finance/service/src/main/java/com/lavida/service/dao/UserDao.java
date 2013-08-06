package com.lavida.service.dao;

import com.lavida.service.entity.UserJdo;

import javax.persistence.*;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 01.08.13
 * Time: 17:27
 * To change this template use File | Settings | File Templates.
 */
//@NamedQueries({
//        @NamedQuery(name = UserDao.FIND_ALL, query = "select u from UserJdo u"),
//        @NamedQuery(name = UserDao.FIND_BY_LOGIN, query = "select u from UserJdo u where u.login = :login")
//})
public class UserDao implements Dao<UserJdo> {
//    static final String FIND_ALL = "UserDao.findAll";
//    static final String FIND_BY_LOGIN = "UserDao.findByLogin";
    static final String FIND_BY_LOGIN = "select u from UserJdo u where u.login = :login";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserJdo getById(int id) {
        return entityManager.find(UserJdo.class, id);
    }

    public UserJdo getUserByLogin(String login) {
//        TypedQuery<UserJdo> query = entityManager.createNamedQuery(UserDao.FIND_BY_LOGIN, UserJdo.class)
        TypedQuery<UserJdo> query = entityManager.createQuery(UserDao.FIND_BY_LOGIN, UserJdo.class)
                .setParameter("login", login);
        return query.getSingleResult();
    }

    @Override
    public void put(UserJdo userJdo) {
        entityManager.persist(userJdo);
    }


    @Override
    public void update(UserJdo userJdo) {
        entityManager.merge(userJdo);
    }

    @Override
    public List<UserJdo> getAll() {
        return entityManager.createQuery("select u from UserJdo u", UserJdo.class).getResultList();
    }

    @Override
    public void delete(int id) {
        UserJdo deleteUser = entityManager.find(UserJdo.class, id);
        if (deleteUser != null) {
            entityManager.remove(deleteUser);
        } else {  //todo change exception
            throw new RuntimeException("Cannot delete user[" + id + "], because it doesn't exist.");

        }
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
