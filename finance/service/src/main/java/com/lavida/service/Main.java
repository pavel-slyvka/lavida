package com.lavida.service;

import com.lavida.service.entity.ArticleJdo;
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
        ArticleService articleService = context.getBean(ArticleService.class);
        ArticleJdo articleJdo = new ArticleJdo();
        articleJdo.setName("Мак");
        articleJdo.setCode("1234/46");
//        articleJdo.setId(1);
//        articleService.save(articleJdo);
//        articleService.update(articleJdo);
//        articleService.delete(1);
        System.out.println(articleService.getAll());
    }
}
