package com.lavida.swing.service;

import com.lavida.service.ArticleChangedFieldService;
import com.lavida.service.entity.ArticleChangedFieldJdo;
import com.lavida.swing.event.ArticleChangedFieldEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * The ArticleChangedFieldServiceSwingWrapper
 * <p/>
 * Created: 01.10.13 21:23.
 *
 * @author Ruslan.
 */
@Service
public class ArticleChangedFieldServiceSwingWrapper implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Resource
    private ArticleChangedFieldService articleChangedFieldService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void update (ArticleChangedFieldJdo articleChangedFieldJdo) {
        articleChangedFieldService.update(articleChangedFieldJdo);
        applicationContext.publishEvent(new ArticleChangedFieldEvent(this));
    }

    public void update (List<ArticleChangedFieldJdo> articleChangedFieldJdoList) {
        for (ArticleChangedFieldJdo articleChangedFieldJdo : articleChangedFieldJdoList) {
            update(articleChangedFieldJdo);
        }
    }

    public List<ArticleChangedFieldJdo> get(String queryName) {
        return articleChangedFieldService.get(queryName);
    }

    public void delete (ArticleChangedFieldJdo articleChangedFieldJdo) {
        articleChangedFieldService.delete(articleChangedFieldJdo.getId());
        applicationContext.publishEvent(new ArticleChangedFieldEvent(this));
    }

    public List<ArticleChangedFieldJdo> clearAndReturnForCurrentDay () {
        List<ArticleChangedFieldJdo> oldArticleChangedFieldList = articleChangedFieldService.getAll();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDay = dateFormat.format(new Date());
        for (ArticleChangedFieldJdo articleChangedFieldJdo : oldArticleChangedFieldList) {
            if (!currentDay.equals(dateFormat.format(articleChangedFieldJdo.getOperationDate()))) {
                articleChangedFieldService.delete(articleChangedFieldJdo.getId());
            }
        }
        return articleChangedFieldService.getAll();
    }
}
