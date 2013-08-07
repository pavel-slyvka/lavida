package com.lavida.service;

import com.google.gdata.util.ServiceException;
import com.lavida.service.dao.ArticleDao;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.google.ArticlesFromGoogleDocUnmarshaller;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * The {@code ArticleService} is a service for ArticleJdo to work with database.
 * Created: 10:10 05.08.13
 *
 * @author Ruslan
 */
public class ArticleService {
    private ArticleDao articleDao;
    private ArticlesFromGoogleDocUnmarshaller articlesUnmarshaller;

    public ArticleService() {
    }
    @Transactional
    public void save (ArticleJdo articleJdo) {
        articleDao.put(articleJdo);
    }

    @Transactional
    public void update (ArticleJdo articleJdo) {
        articleDao.update(articleJdo);
    }

    @Transactional
    public void delete (int id) {
        articleDao.delete(id);
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

    public List<ArticleJdo> loadFromGoogle (String userNameGmail, String passwordGmail) throws IOException, ServiceException {
        return  articlesUnmarshaller.unmarshal(userNameGmail, passwordGmail);
    }

    public List<String> loadTableHeader (String userNameGmail, String passwordGmail) throws IOException, ServiceException {
        return articlesUnmarshaller.readTableHeader(userNameGmail, passwordGmail);
    }

    public ArticleDao getArticleDao() {
        return articleDao;
    }

    public void setArticleDao(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    public void setArticlesUnmarshaller(ArticlesFromGoogleDocUnmarshaller articlesUnmarshaller) {
        this.articlesUnmarshaller = articlesUnmarshaller;
    }
}
