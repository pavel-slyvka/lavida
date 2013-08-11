package com.lavida.service.dao;

import com.lavida.service.entity.UserJdo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.persistence.TypedQuery;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 01.08.13
 * Time: 17:27
 * To change this template use File | Settings | File Templates.
 */
public class UserDao extends GenericDao<UserJdo> {

    public UserJdo getUserByLogin(String login) {
        TypedQuery<UserJdo> query = entityManager.createNamedQuery(UserJdo.FIND_BY_LOGIN, UserJdo.class)
                .setParameter("login", login);
        return query.getSingleResult();
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-security.xml");
        UserDao userDao = context.getBean(UserDao.class);
        System.out.println(userDao.getUserByLogin("seller"));
    }

}
