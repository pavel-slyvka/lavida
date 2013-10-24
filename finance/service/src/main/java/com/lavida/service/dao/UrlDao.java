package com.lavida.service.dao;

import com.lavida.service.entity.Url;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

/**
 * The UrlDao
 * <p/>
 * Created: 24.10.13 11:25.
 *
 * @author Ruslan.
 */
@Repository
public class UrlDao extends AbstractDao<Url> {

    public Url findByUrlString (String urlString) {
        TypedQuery<Url> query = entityManager.createNamedQuery(Url.FIND_BY_URL, Url.class).setParameter("urlString", urlString);
        Url url;
        try{
            url = query.getSingleResult();
        }catch(NoResultException e) {
            url = null;
        }

        return url;
    }
}
