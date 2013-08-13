package com.lavida.service;

import com.lavida.service.dao.SoldArticleDao;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.entity.SoldArticleJdo;
import com.lavida.service.settings.SettingsHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created: 13:15 13.08.13
 * The SoldArticleService is the service for operations with SoldArticleJdo.
 * @author Ruslan
 */
@Service
public class SoldArticleService {
    @Resource
    private SoldArticleDao soldArticleDao;
    @Resource
    private SettingsHolder settingsHolder;

    @Transactional
    public void save (SoldArticleJdo soldArticleJdo) {
        soldArticleDao.put(soldArticleJdo);
    }

    @Transactional
    public void update (SoldArticleJdo soldArticleJdo) {
        soldArticleDao.update(soldArticleJdo);
    }

    @Transactional
    public void update(List<SoldArticleJdo> soldArticles) {
        for (SoldArticleJdo soldArticleJdo : soldArticles) {
            update(soldArticleJdo);
//            articleDao.update(articleJdo);
        }
    }

    @Transactional
    public void delete (int id) {
        soldArticleDao.delete(SoldArticleJdo.class, id);
    }

    public SoldArticleJdo getById (int id) {
        return soldArticleDao.getById(SoldArticleJdo.class, id);
    }

    public List<SoldArticleJdo> getAll () {
        return  soldArticleDao.getAll(SoldArticleJdo.class);
    }

    public SoldArticleJdo getByCode (String code) {
        SoldArticleJdo neededArticle = null;
        List<SoldArticleJdo> soldArticles = soldArticleDao.getAll(SoldArticleJdo.class);
        for (SoldArticleJdo soldArticleJdo : soldArticles) {
            if (soldArticleJdo.getCode().equals(code)) {
                neededArticle = soldArticleJdo;
                return neededArticle;
            }
        }
        throw new RuntimeException("There is no article with code: " + code + "!"); //todo create databaseException
    }



    public void setSoldArticleDao(SoldArticleDao soldArticleDao) {
        this.soldArticleDao = soldArticleDao;
    }

    public void setSettingsHolder(SettingsHolder settingsHolder) {
        this.settingsHolder = settingsHolder;
    }
}
