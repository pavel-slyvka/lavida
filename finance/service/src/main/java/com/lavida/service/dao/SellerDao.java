package com.lavida.service.dao;

import com.lavida.service.entity.SellerJdo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

/**
 * The SellerDao is a DAO for the {@link com.lavida.service.entity.SellerJdo}.
 * Created: 12:59 02.09.13
 *
 * @author Ruslan
 */
@Repository
public class SellerDao extends AbstractDao<SellerJdo> {

    public SellerJdo getSellerByName(String name) {
        TypedQuery<SellerJdo> query = entityManager.createNamedQuery(SellerJdo.FIND_BY_NAME, SellerJdo.class)
                .setParameter("name", name);
        return query.getSingleResult();
    }

}
