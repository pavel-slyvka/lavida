package com.lavida.service.dao;

import com.lavida.service.entity.BrandJdo;
import com.lavida.service.entity.SizeJdo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

/**
 * The SizeDao
 * <p/>
 * Created: 13.10.13 18:36.
 *
 * @author Ruslan.
 */
@Repository
public class SizeDao extends AbstractDao<SizeJdo> {
    public SizeJdo getByName (String name) {
        TypedQuery<SizeJdo> query = entityManager.createNamedQuery(SizeJdo.FIND_BY_NAME, SizeJdo.class)
                .setParameter("name", name);
        return query.getSingleResult();
    }

}
