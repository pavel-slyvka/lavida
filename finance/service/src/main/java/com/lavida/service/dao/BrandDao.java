package com.lavida.service.dao;

import com.lavida.service.entity.BrandJdo;
import com.lavida.service.entity.TagJdo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

/**
 * The BrandDao
 * <p/>
 * Created: 12.10.13 9:59.
 *
 * @author Ruslan.
 */
@Repository
public class BrandDao extends AbstractDao<BrandJdo> {

    public BrandJdo getByName (String name) {
        TypedQuery<BrandJdo> query = entityManager.createNamedQuery(BrandJdo.FIND_BY_NAME, BrandJdo.class)
                .setParameter("name", name);
        return query.getSingleResult();
    }

}
