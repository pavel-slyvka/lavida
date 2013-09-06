package com.lavida.service.dao;

import com.lavida.service.entity.DiscountCardJdo;
import org.springframework.stereotype.Repository;

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

    public DiscountCardJdo getByNumber(int number) {
        TypedQuery<DiscountCardJdo> query = entityManager.createNamedQuery(DiscountCardJdo.FIND_BY_NUMBER, DiscountCardJdo.class).
                setParameter("number", number);
        return query.getSingleResult();
    }

    public List<DiscountCardJdo> get (String query) {
        return entityManager.createNamedQuery(query, DiscountCardJdo.class).getResultList();
    }
}
