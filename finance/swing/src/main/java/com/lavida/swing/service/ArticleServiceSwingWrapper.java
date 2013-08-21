package com.lavida.swing.service;

import com.google.gdata.util.ServiceException;
import com.lavida.service.ArticleService;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.swing.event.ArticleUpdateEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
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
        articleService.update(articles);
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
     * @return the ArticleUpdateInformer object with the information about updating process.
     */
    public ArticleUpdateInformer updateToDatabase(List<ArticleJdo> remoteArticles) {
        ArticleUpdateInformer informer = new ArticleUpdateInformer();
        List<ArticleJdo> oldArticles = getAll();
        List<ArticleJdo> articlesToAdd = new ArrayList<ArticleJdo>();
        List<ArticleJdo> articlesToUpdate = new ArrayList<ArticleJdo>();
        List<ArticleJdo> articlesToDelete = new ArrayList<ArticleJdo>();
        List<ArticleJdo> articlesNotChanged = new ArrayList<ArticleJdo>();

        for (ArticleJdo remoteArticle : remoteArticles) {
            for (ArticleJdo oldArticle : oldArticles) {
                if (remoteArticle.equals(oldArticle)) {
                    articlesNotChanged.add(oldArticle);
                    remoteArticles.remove(remoteArticle);
                    oldArticles.remove(oldArticle);
                } else if (isEquivalent(remoteArticle, oldArticle)) {
                    if (oldArticle.getPostponedOperationDate() != null) {
                        if (!remoteArticle.getSold().equals(oldArticle.getSold()) ||       // sold in application
                                remoteArticle.getRefundDate().equals(oldArticle.getRefundDate())) {
                            articlesNotChanged.add(oldArticle);
                            remoteArticles.remove(remoteArticle);
                            oldArticles.remove(oldArticle);
                        }else if (!remoteArticle.getSold().equals(oldArticle.getSold()) ||       // refund in application
                                !remoteArticle.getRefundDate().equals(oldArticle.getRefundDate())) {
                            articlesNotChanged.add(oldArticle);
                            remoteArticles.remove(remoteArticle);
                            oldArticles.remove(oldArticle);
                        }
                    } else if (oldArticle.getPostponedOperationDate() == null) {   // sold and refund in spreadsheet
                        remoteArticle.setId(oldArticle.getId());
                        articlesToUpdate.add(remoteArticle);
                        remoteArticles.remove(remoteArticle);
                        oldArticles.remove(oldArticle);
                    }
                }
            }
        }

        articleService.update(articlesToUpdate);
        articlesToAdd.addAll(remoteArticles); //rest remote not removed
        articleService.update(articlesToAdd);
        articlesToDelete.addAll(oldArticles); // rest old not removed
        for ( ArticleJdo articleJdo : articlesToDelete) {
            articleService.delete(articleJdo.getId());
        }

        informer.setAddedCount(articlesToAdd.size());
        informer.setUpdatedCount(articlesToUpdate.size());
        informer.setDeletedCount(articlesToDelete.size());
        applicationContext.publishEvent(new ArticleUpdateEvent(this));
        return informer;
    }

    /**
     * Defines whether two articles are equivalent by quantity, brand, code, name, size and delivery date.
     * @param articleJdoThis
     * @param articleJdoThat
     * @return
     */
    private boolean isEquivalent(ArticleJdo articleJdoThis, ArticleJdo articleJdoThat) {
        if (articleJdoThis == articleJdoThat) return true;

        if (articleJdoThis.getQuantity() != articleJdoThat.getQuantity()) return false;
        if (articleJdoThis.getBrand() != null ? !articleJdoThis.getBrand().equals(articleJdoThat.getBrand()) :
                articleJdoThat.getBrand() != null) return false;
        if (articleJdoThis.getCode() != null ? !articleJdoThis.getCode().equals(articleJdoThat.getCode()) :
                articleJdoThat.getCode() != null) return false;
        if (!articleJdoThis.getDeliveryDate().equals(articleJdoThat.getDeliveryDate())) return false;
        if (!articleJdoThis.getName().equals(articleJdoThat.getName())) return false;
        if (articleJdoThis.getSize() != null ? !articleJdoThis.getSize().equals(articleJdoThat.getSize()) :
                articleJdoThat.getSize() != null) return false;
        return true;

    }

    public void updateToSpreadsheet(ArticleJdo articleJdo) throws IOException, ServiceException {
        articleService.updateToSpreadsheet(articleJdo);
    }
}
