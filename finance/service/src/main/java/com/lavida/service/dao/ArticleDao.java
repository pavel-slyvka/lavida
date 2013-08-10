package com.lavida.service.dao;

import com.lavida.service.entity.ArticleJdo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/** The {@code ArticleDao} is DAO for {@code ArticleJdo} .
 * Created: 9:59 05.08.13
 *
 * @author Ruslan
 */
public class ArticleDao implements Dao<ArticleJdo> {
    public static final String SELECT_ALL_QUERY = "select a from ArticleJdo a";
    public static final String SELECT_NOT_SOLD_QUERY = "select a from ArticleJdo a where a.sold IS NULL";
    public static final String SELECT_SOLD_QUERY = "select a from ArticleJdo a where a.sold IS NOT NULL";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ArticleJdo getById(int id) {
        return entityManager.find(ArticleJdo.class, id);
    }

    @Override
    public void put(ArticleJdo articleJdo) {
        entityManager.persist(articleJdo);
    }

    @Override
    public void update(ArticleJdo articleJdo) {
        entityManager.merge(articleJdo);
    }

    @Override
    public List<ArticleJdo> getAll() {
        return entityManager.createQuery(SELECT_ALL_QUERY, ArticleJdo.class).getResultList();
    }

    public List<ArticleJdo> getNotSold() {
        return entityManager.createQuery(SELECT_NOT_SOLD_QUERY, ArticleJdo.class).getResultList();
    }

    public List<ArticleJdo> getSold() {
        return entityManager.createQuery(SELECT_SOLD_QUERY, ArticleJdo.class).getResultList();
    }

    @Override
    public void delete(int id) {
        ArticleJdo deleteArticle = entityManager.find(ArticleJdo.class, id);
        if (deleteArticle != null) {
            entityManager.remove(deleteArticle);
        } else { //todo change exception
            throw new RuntimeException("Cannot delete article[" + id + "], because it doesn't exist.");
        }
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
