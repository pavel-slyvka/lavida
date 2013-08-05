package com.lavida.service;

import com.lavida.service.dao.ArticleDao;
import com.lavida.service.entity.ArticleJdo;

/**
 * Created: 10:10 05.08.13
 *
 * @author Ruslan
 */
public class ArticleService {
    private ArticleDao articleDao;

    public ArticleJdo getById (int id) {
        return articleDao.getById(id);
    }



    public ArticleDao getArticleDao() {
        return articleDao;
    }

    public void setArticleDao(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }
}
