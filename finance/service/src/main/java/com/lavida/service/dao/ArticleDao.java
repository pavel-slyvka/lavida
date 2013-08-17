package com.lavida.service.dao;

import com.lavida.service.entity.ArticleJdo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The {@code ArticleDao} is DAO for {@code ArticleJdo} .
 * Created: 9:59 05.08.13
 *
 * @author Ruslan
 */
@Repository
public class ArticleDao extends AbstractDao<ArticleJdo> {

    public List<ArticleJdo> get(String queryName) {
        return entityManager.createNamedQuery(queryName, ArticleJdo.class).getResultList();
    }
}
