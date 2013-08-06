package com.lavida.service;

import com.lavida.service.dao.ArticleDao;
import com.lavida.service.entity.ArticleJdo;

import java.util.List;

/**
 * The {@code ArticleService} is a service for ArticleJdo to work with database.
 * Created: 10:10 05.08.13
 *
 * @author Ruslan
 */
public class ArticleService {
    private ArticleDao articleDao;

    public void save (ArticleJdo articleJdo) {
        articleDao.put(articleJdo);
    }

    public void update (ArticleJdo articleJdo) {
        articleDao.update(articleJdo);
    }

    public ArticleJdo getById (int id) {
        return articleDao.getById(id);
    }

    public List<ArticleJdo> getAll () {
        return  articleDao.getAll();
    }

    public ArticleJdo getByCode (String code) {
        ArticleJdo neededArticle = null;
        List<ArticleJdo> articles = articleDao.getAll();
        for (ArticleJdo articleJdo : articles) {
            if (articleJdo.getCode().equals(code)) {
                neededArticle = articleJdo;
                return neededArticle;
            }
        }
        throw new RuntimeException("There is no article with code: " + code + "!"); //todo create databaseException
    }


    public ArticleDao getArticleDao() {
        return articleDao;
    }

    public void setArticleDao(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }
}
