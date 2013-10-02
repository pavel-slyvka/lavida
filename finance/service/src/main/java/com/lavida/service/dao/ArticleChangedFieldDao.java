package com.lavida.service.dao;

import com.lavida.service.entity.ArticleChangedFieldJdo;
import com.lavida.service.entity.ArticleJdo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * The ArticleChangedFieldDao
 * <p/>
 * Created: 27.09.13 11:29.
 *
 * @author Ruslan.
 */
@Repository
public class ArticleChangedFieldDao extends AbstractDao<ArticleChangedFieldJdo> {

/*
    public ArticleChangedFieldJdo findByCode(String code) {
        TypedQuery<ArticleChangedFieldJdo> query = entityManager.createNamedQuery(ArticleChangedFieldJdo.FIND_BY_CODE,
                ArticleChangedFieldJdo.class).setParameter("code", code);
        return query.getSingleResult();
    }

    public ArticleChangedFieldJdo findByOperationDate(Date date) {
        TypedQuery<ArticleChangedFieldJdo> query = entityManager.createNamedQuery(ArticleChangedFieldJdo.FIND_BY_OPERATION_DATE,
                ArticleChangedFieldJdo.class).setParameter("date", date);
        return query.getSingleResult();
    }
*/

    public List<ArticleChangedFieldJdo> get(String queryName) {
        return entityManager.createNamedQuery(queryName, ArticleChangedFieldJdo.class).getResultList();
    }

}
