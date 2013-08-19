package com.lavida.service.dao;

import com.lavida.service.entity.TagJdo;
import com.lavida.service.entity.UserJdo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;

/**
 * Created: 21:13 19.08.13
 *  The Dao for the TagJdo.
 * @author Ruslan
 */
@Repository
public class TagDao extends AbstractDao<TagJdo> {

    public TagJdo getTagByName (String name) {
        TypedQuery<TagJdo> query = entityManager.createNamedQuery(TagJdo.FIND_BY_NAME, TagJdo.class)
                .setParameter("name", name);
        return query.getSingleResult();
    }

}
