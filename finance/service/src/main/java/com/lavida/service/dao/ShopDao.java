package com.lavida.service.dao;

import com.lavida.service.entity.ShopJdo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

/**
 * The ShopDao
 * <p/>
 * Created: 13.10.13 19:36.
 *
 * @author Ruslan.
 */
@Repository
public class ShopDao extends AbstractDao<ShopJdo> {

    public ShopJdo getByName(String name) {
        TypedQuery<ShopJdo> query = entityManager.createNamedQuery(ShopJdo.FIND_BY_NAME, ShopJdo.class).
                setParameter("name", name);
        return query.getSingleResult();
    }
}
