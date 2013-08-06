package com.lavida.service;

import com.lavida.service.dao.AuthoritiesDao;
import com.lavida.service.dao.UserDao;
import com.lavida.service.entity.AuthorityJdo;
import com.lavida.service.entity.UserJdo;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 01.08.13
 * Time: 18:11
 * To change this template use File | Settings | File Templates.
 */
public class UserService {
    private DaoUserDetailsManagerImpl userDetailsManager;
    private UserDao userDao;
    private AuthoritiesDao authoritiesDao;

    public void login(String username, String rowPassword) {
        userDetailsManager.login(username, rowPassword);
    }

    public void logout() {
        userDetailsManager.logout();
    }

    public List<UserJdo> getAll() {
        return userDao.getAll();
    }

    public void getById(int id) {
        userDao.getById(id);
    }

    public UserJdo getByLogin(String login) {
        return userDao.getUserByLogin(login);
    }

    @Transactional
    public void save(String username, String rawPassword, boolean enabled, List<String> authorities) {
        UserJdo userJdo = new UserJdo(username, "", true);
        userJdo.setPassword(userDetailsManager.encodePassword(username, rawPassword));
        userJdo.setAuthorities(new HashSet<String>(authorities));
        userDao.put(userJdo);
    }

    @Transactional
    @Deprecated // never used
    public void assignAuthorityUpdate(UserJdo userJdo, AuthorityJdo authorities) {
        userDao.update(userJdo);
        authoritiesDao.put(authorities);
    }

    @Transactional
    @Deprecated // never used
    public void assignAuthoritySave(UserJdo userJdo, AuthorityJdo authorities) {
        userDao.put(userJdo);
        authoritiesDao.put(authorities);
    }

    @Transactional
    @Deprecated // must not work with UserJdo
    public void save(UserJdo userJdo) {
        userDao.put(userJdo);
    }

    @Transactional
    @Deprecated // must not work with UserJdo
    public void update(UserJdo userJdo) {
        userDao.update(userJdo);
    }

    @Transactional
    public void delete(int id) {
        userDao.delete(id);
    }

    public UserDao getUserDao() {
        return userDao;
    }


    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public AuthoritiesDao getAuthoritiesDao() {
        return authoritiesDao;
    }

    public void setAuthoritiesDao(AuthoritiesDao authoritiesDao) {
        this.authoritiesDao = authoritiesDao;
    }

    public void setUserDetailsManager(DaoUserDetailsManagerImpl userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }
}
