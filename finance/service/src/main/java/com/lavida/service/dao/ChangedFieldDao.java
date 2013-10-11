package com.lavida.service.dao;

import com.lavida.service.entity.ChangedFieldJdo;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The ArticleChangedFieldDao
 * <p/>
 * Created: 27.09.13 11:29.
 *
 * @author Ruslan.
 */
@Repository
public class ChangedFieldDao extends AbstractDao<ChangedFieldJdo> {

    public List<ChangedFieldJdo> get(String queryName) {
        return entityManager.createNamedQuery(queryName, ChangedFieldJdo.class).getResultList();
    }

}
