package com.lavida.service.dao;

import com.lavida.service.entity.DiscountCardJdo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

/**
 * The DAO layer for the {@link com.lavida.service.entity.DiscountCardJdo}.
 * Created: 8:49 06.09.13
 *
 * @author Ruslan
 */
@Repository
public class DiscountCardDao extends AbstractDao<DiscountCardJdo> {

    public DiscountCardJdo getByNumber(int number) {
        TypedQuery<DiscountCardJdo> query = entityManager.createNamedQuery(DiscountCardJdo.FIND_BY_NUMBER, DiscountCardJdo.class).
                setParameter("number", number);
        return query.getSingleResult();
    }
}
