package com.lavida.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: Admin
 * Date: 01.08.13
 * Time: 19:29
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-security.xml");
        UserService service = context.getBean(UserService.class);
//        service.save("login2", "pass2", true, Arrays.asList( "ROLE_ADMIN"));
//        service.delete(2);
//        System.out.println(service.getAll());
        System.out.println(service.getByLogin("login2"));
    }
}
