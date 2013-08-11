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

    public List<ArticleJdo> getNotSold() {
        return entityManager.createNamedQuery(ArticleJdo.FIND_NOT_SOLD, ArticleJdo.class).getResultList();
    }

    public List<ArticleJdo> getSold() {
        return entityManager.createNamedQuery(ArticleJdo.FIND_SOLD, ArticleJdo.class).getResultList();
    }
}
