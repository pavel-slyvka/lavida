package com.lavida.service;

import com.lavida.service.dao.UserDao;
import com.lavida.service.entity.UserJdo;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 01.08.13
 * Time: 18:11
 * To change this template use File | Settings | File Templates.
 */
public class UserService {
    private UserDao userDao;
    @Transactional
    public void save (UserJdo userJdo) {
        userDao.put(userJdo);
    }

    @Transactional
    public void update (UserJdo userJdo) {
        userDao.update(userJdo);
    }

    public UserJdo getByLogin(String login) {
       return userDao.getUserByLogin(login);
    }

    public void getById (int id) {
        userDao.getById(id);
    }

    @Transactional
    public void delete (int id) {
        userDao.delete(id);
    }

    public List<UserJdo> getAll () {
        return userDao.getAll();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
