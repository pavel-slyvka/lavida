package com.lavida.service;

import com.lavida.service.dao.UserDao;
import com.lavida.service.entity.UserJdo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 01.08.13
 * Time: 18:11
 * To change this template use File | Settings | File Templates.
 */
@Service
public class UserService {

    @Resource
    private DaoUserDetailsManagerImpl userDetailsManager;

    @Resource
    private UserDao userDao;

    public void login(String username, String rowPassword) {
        userDetailsManager.login(username, rowPassword);
    }

    public void logout() {
        userDetailsManager.logout();
    }

    public List<String> getCurrentUserRoles() {
        return userDetailsManager.getRoles();
    }

    public List<UserJdo> getAll() {
        return userDao.getAll(UserJdo.class);
    }

    public void getById(int id) {
        userDao.getById(UserJdo.class, id);
    }

    public UserJdo getByLogin(String login) {
        return userDao.getUserByLogin(login);
    }

    @Transactional
    public void save(String username, String rawPassword, boolean enabled, List<String> authorities, String email) {
        UserJdo userJdo = new UserJdo(username, "", true, email);
        userJdo.setPassword(userDetailsManager.encodePassword(username, rawPassword));
        userJdo.setAuthorities(new HashSet<String>(authorities));
        userDao.put(userJdo);
    }

    @Transactional
    public void updatePassword(UserJdo userJdo, String newRawPassword) {
        userJdo.setPassword(userDetailsManager.encodePassword(userJdo.getLogin(), newRawPassword));
        userDao.update(userJdo);
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
        userDao.delete(UserJdo.class, id);
    }

    public UserDao getUserDao() {
        return userDao;
    }


    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setUserDetailsManager(DaoUserDetailsManagerImpl userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-security.xml");
        UserService service = context.getBean(UserService.class);
        service.save("seller", "seller", true, Arrays.asList("ROLE_SELLER"), "fra.prsnl@i.ua");
//        service.save("manager", "manager", true, Arrays.asList("ROLE_MANAGER"));
//        service.save("owner", "owner", true, Arrays.asList("ROLE_OWNER"));
//        service.save("admin", "admin", true, Arrays.asList("ROLE_ADMIN"));
        System.out.println(service.getAll());
//        System.out.println(service.getByLogin("login2"));

    }
}
