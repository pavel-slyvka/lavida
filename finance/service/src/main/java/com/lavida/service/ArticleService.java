package com.lavida.service;

import com.google.gdata.util.ServiceException;
import com.lavida.service.dao.ArticleDao;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.google.RemoteSpreadsheetsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * The {@code ArticleService} is a service for ArticleJdo to work with database.
 * Created: 10:10 05.08.13
 *
 * @author Ruslan
 */
@Service
public class ArticleService {

    @Resource
    private ArticleDao articleDao;

    @Resource
    private RemoteSpreadsheetsService remoteService;

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

    @Transactional    // todo may be make transactional
    public void update(List<ArticleJdo> articles) {
        for (ArticleJdo articleJdo : articles) {
            update(articleJdo);
//            articleDao.update(articleJdo);
        }
    }

    @Transactional
    public void delete (int id) {
        articleDao.delete(ArticleJdo.class, id);
    }

    public ArticleJdo getById (int id) {
        return articleDao.getById(ArticleJdo.class, id);
    }

    public List<ArticleJdo> getAll () {
        return  articleDao.getAll(ArticleJdo.class);
    }

    public ArticleJdo getByCode (String code) {
        ArticleJdo neededArticle = null;
        List<ArticleJdo> articles = articleDao.getAll(ArticleJdo.class);
        for (ArticleJdo articleJdo : articles) {
            if (articleJdo.getCode().equals(code)) {
                neededArticle = articleJdo;
                return neededArticle;
            }
        }
        throw new RuntimeException("There is no article with code: " + code + "!"); //todo create databaseException
    }

    public List<ArticleJdo> loadArticlesFromRemoteServer() throws IOException, ServiceException {
        return remoteService.loadArticles();
    }

    public List<String> loadTableHeadersFromRemoteServer() throws IOException, ServiceException {
        return remoteService.readTableHeader();
    }

    public List<ArticleJdo> getNotSoldArticles() {
        return articleDao.getNotSold();
    }

    public List<ArticleJdo> getSoldArticles() {
        return articleDao.getSold();
    }
    @Transactional
    public void updateToSpreadsheet(ArticleJdo articleJdo) throws IOException, ServiceException {
        remoteService.updateArticle(articleJdo);
    }


    public void setArticleDao(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    public void setRemoteService(RemoteSpreadsheetsService remoteService) {
        this.remoteService = remoteService;
    }
}
