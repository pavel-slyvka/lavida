package com.lavida.service.dao;

import com.lavida.service.entity.DiscountCardJdo;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * The DAO layer for the {@link com.lavida.service.entity.DiscountCardJdo}.
 * Created: 8:49 06.09.13
 *
 * @author Ruslan
 */
@Repository
public class DiscountCardDao extends AbstractDao<DiscountCardJdo> {

    public DiscountCardJdo getByNumber(String number) {
        TypedQuery<DiscountCardJdo> query = entityManager.createNamedQuery(DiscountCardJdo.FIND_BY_NUMBER, DiscountCardJdo.class).
                setParameter("number", number);
        DiscountCardJdo discountCardJdo;
        try {
         discountCardJdo = query.getSingleResult();
        } catch (NoResultException e) {
          discountCardJdo = null;
        }
        return discountCardJdo;
    }

    public List<DiscountCardJdo> get (String query) {
        return entityManager.createNamedQuery(query, DiscountCardJdo.class).getResultList();
    }
}
