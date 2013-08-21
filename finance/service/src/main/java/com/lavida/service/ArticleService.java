package com.lavida.service;

import com.google.gdata.util.ServiceException;
import com.lavida.service.dao.ArticleDao;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.remote.RemoteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
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
    private RemoteService remoteService;

    @Transactional
    public void save(ArticleJdo articleJdo) {
        articleDao.put(articleJdo);
    }

    @Transactional
    public void update(ArticleJdo articleJdo) {
        articleDao.update(articleJdo);
    }

    @Transactional
    public ArticleUpdateInfo updateDatabase(List<ArticleJdo> remoteArticles) {
        List<ArticleJdo> dbOldArticles = getAll();

        List<ArticleJdo> articlesToUpdate = new ArrayList<ArticleJdo>();
        List<ArticleJdo> articlesToDelete = new ArrayList<ArticleJdo>();
        l:
        for (ArticleJdo dbOldArticle : dbOldArticles) {
            for (int i = 0; i<remoteArticles.size(); ++i) {
                ArticleJdo remoteArticle = remoteArticles.get(i);
                if (dbOldArticle.getSpreadsheetRow() == remoteArticle.getSpreadsheetRow()) {
                    remoteArticles.remove(i);
                    if (!dbOldArticle.equals(remoteArticle)) {
                        remoteArticle.setId(dbOldArticle.getId());
                        articlesToUpdate.add(remoteArticle);
                    }
                    continue l;
                }
                articlesToDelete.add(dbOldArticle);
            }
        }

        ArticleUpdateInfo articleUpdateInfo = new ArticleUpdateInfo();
        articleUpdateInfo.setAddedCount(remoteArticles.size());
        articleUpdateInfo.setUpdatedCount(articlesToUpdate.size());
        articleUpdateInfo.setDeletedCount(articlesToDelete.size());
        for (ArticleJdo articleJdo : articlesToDelete) {
            delete(articleJdo.getId());
        }
        for (ArticleJdo articleJdo : articlesToUpdate) {
            update(articleJdo);
        }
        for (ArticleJdo articleJdo : remoteArticles) {
            save(articleJdo);
        }
        return articleUpdateInfo;
    }

    @Transactional
    public void delete(int id) {
        articleDao.delete(ArticleJdo.class, id);
    }

    public ArticleJdo getById(int id) {
        return articleDao.getById(ArticleJdo.class, id);
    }

    public List<ArticleJdo> getAll() {
        return articleDao.getAll(ArticleJdo.class);
    }

    public ArticleJdo getByCode(String code) {
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

    public void updateToSpreadsheet(ArticleJdo articleJdo) throws IOException, ServiceException {
        remoteService.updateArticle(articleJdo);
    }

    public void setArticleDao(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    public void setRemoteService(RemoteService remoteService) {
        this.remoteService = remoteService;
    }
}
