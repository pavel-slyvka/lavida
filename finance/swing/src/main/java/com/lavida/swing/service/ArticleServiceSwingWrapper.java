package com.lavida.swing.service;

import com.google.gdata.util.ServiceException;
import com.lavida.service.ArticleService;
import com.lavida.service.ArticleUpdateInfo;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.event.ArticleUpdateEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * ArticleServiceSwingWrapper
 * Created: 22:09 16.08.13
 *
 * @author Pavel
 */
@Service
public class ArticleServiceSwingWrapper implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Resource
    private ArticleService articleService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public List<ArticleJdo> getAll() {
        return articleService.getAll();
    }

    public List<ArticleJdo> loadArticlesFromRemoteServer() throws IOException, ServiceException {
        return articleService.loadArticlesFromRemoteServer();
    }

    public void update(List<ArticleJdo> articles) {
        articleService.updateDatabase(articles);
        applicationContext.publishEvent(new ArticleUpdateEvent(this));
    }

    public void update(ArticleJdo articleJdo) {
        articleService.update(articleJdo);
        applicationContext.publishEvent(new ArticleUpdateEvent(this));
    }

    /**
     * Updates Articles from remote server to database according to equivalence by code, name, brand, size and
     * deliveryDate
     *
     * @param remoteArticles the List of articles to be updated to database;
     * @return the ArticleUpdateInfo object with the information about updating process.
     */
    public ArticleUpdateInfo updateToDatabase(List<ArticleJdo> remoteArticles) {
        ArticleUpdateInfo articleUpdateInfo = articleService.updateDatabase(remoteArticles);
        applicationContext.publishEvent(new ArticleUpdateEvent(this));
        return articleUpdateInfo;
    }

    public void updateToSpreadsheet(ArticleJdo articleJdo) throws IOException, ServiceException {
        articleService.updateToSpreadsheet(articleJdo);
    }
}
