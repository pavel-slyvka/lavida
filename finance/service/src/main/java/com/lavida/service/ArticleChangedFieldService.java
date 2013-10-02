package com.lavida.service;

import com.lavida.service.dao.ArticleChangedFieldDao;
import com.lavida.service.entity.ArticleChangedFieldJdo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * The ArticleChangedFieldService
 * <p/>
 * Created: 01.10.13 8:40.
 *
 * @author Ruslan.
 */
@Service
public class ArticleChangedFieldService {
    @Resource
    private ArticleChangedFieldDao articleChangedFieldDao;

    @Transactional
    public void save (ArticleChangedFieldJdo articleChangedFieldJdo) {
        articleChangedFieldDao.put(articleChangedFieldJdo);
    }

    @Transactional
    public void update (ArticleChangedFieldJdo articleChangedFieldJdo) {
        articleChangedFieldDao.update(articleChangedFieldJdo);
    }

    @Transactional
    public void delete (int id) {
        articleChangedFieldDao.delete(ArticleChangedFieldJdo.class, id);
    }

    public List<ArticleChangedFieldJdo> getAll () {
        return articleChangedFieldDao.getAll(ArticleChangedFieldJdo.class);
    }

    public ArticleChangedFieldJdo getById (int id) {
        return articleChangedFieldDao.getById(ArticleChangedFieldJdo.class, id);
    }

/*
    public ArticleChangedFieldJdo getByCode (String code) {
        return articleChangedFieldDao.findByCode(code);
    }

    public ArticleChangedFieldJdo getByOperationDate (Date date) {
        return articleChangedFieldDao.findByOperationDate(date);
    }
*/

    public List<ArticleChangedFieldJdo> get(String queryName) {
        return articleChangedFieldDao.get(queryName);
    }
}
