package com.lavida.service;

import com.lavida.service.dao.GenericDao;
import com.lavida.service.entity.TagJdo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created: 18:59 19.08.13
 * The TagService is a service for the TagJdo.
 * @author Ruslan
 */
@Service
public class TagService {
    @Resource
    private GenericDao<TagJdo> tagDao;

    @Transactional
    public void save(TagJdo tagJdo) {
        tagDao.put(tagJdo);
    }

    @Transactional
    public void update(TagJdo tagJdo) {
        tagDao.update(tagJdo);
    }

    public TagJdo getById(int id) {
        return tagDao.getById(TagJdo.class, id);
    }

    public List<TagJdo> getAll () {
        return tagDao.getAll(TagJdo.class);
    }

    @Transactional
    public void delete (int id) {
        tagDao.delete(TagJdo.class, id);
    }
}
