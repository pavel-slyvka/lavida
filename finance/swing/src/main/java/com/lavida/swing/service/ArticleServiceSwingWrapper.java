package com.lavida.swing.service;

import com.google.gdata.util.ServiceException;
import com.lavida.service.ArticleService;
import com.lavida.service.ArticleUpdateInfo;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.entity.ChangedFieldJdo;
import com.lavida.service.xml.ArticlesXmlService;
import com.lavida.swing.event.ArticleUpdateEvent;
import com.lavida.swing.event.PostponedOperationEvent;
import com.lavida.swing.exception.LavidaSwingRuntimeException;
import com.lavida.swing.exception.RemoteUpdateException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
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

    @Resource
    private ArticlesXmlService articlesXmlService;

    @Resource
    private ChangedFieldServiceSwingWrapper changedFieldServiceSwingWrapper;

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
    public ArticleUpdateInfo updateDatabaseFromRemote(List<ArticleJdo> remoteArticles) {
        ArticleUpdateInfo articleUpdateInfo = articleService.updateDatabaseFromRemote(remoteArticles);
        applicationContext.publishEvent(new ArticleUpdateEvent(this));
        return articleUpdateInfo;
    }

    /**
     * Finds equivalent articles from the database to match loaded articles with postponed operations.
     * @param loadedArticles the List < {@link com.lavida.service.entity.ArticleJdo} > with postponed operations.
     */
    public List<ArticleJdo> mergePostponedWithDatabase(List<ArticleJdo> loadedArticles) {
        return articleService.mergePostponedWithDatabase(loadedArticles);
    }

    public void updateToSpreadsheet(ArticleJdo oldArticle, ArticleJdo articleJdo, Boolean isSold) throws RemoteUpdateException {
        try {
            articleService.updateToSpreadsheet(articleJdo, isSold);
        } catch (IOException | ServiceException e) {
            Date postponedDate ;
            if (isSold == null) {
                postponedDate = new Date();
            } else if (isSold) {
                postponedDate = articleJdo.getSaleDate().getTime();
            } else {
                postponedDate = articleJdo.getRefundDate();
            }
            List<ChangedFieldJdo> changedFieldJdoList = articleJdo.findUpdateChanges(oldArticle, null);
            for (ChangedFieldJdo changedFieldJdo: changedFieldJdoList) {
                changedFieldJdo.setPostponedDate(postponedDate);
            }
            changedFieldServiceSwingWrapper.update(changedFieldJdoList);
            articleJdo.setPostponedOperationDate(postponedDate);
            update(articleJdo);
            applicationContext.publishEvent(new PostponedOperationEvent(this));
            throw new RemoteUpdateException(e);
        }
        update(articleJdo);

    }

    public void saveToXml(List<ArticleJdo> articleJdoList, File file) throws JAXBException, IOException {
        articlesXmlService.marshal(articleJdoList, file);
    }

    public List<ArticleJdo> loadFromXml (File file) throws JAXBException, FileNotFoundException {
        return articlesXmlService.unmarshal(file);
    }
}
