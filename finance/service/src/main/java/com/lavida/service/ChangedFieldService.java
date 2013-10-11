package com.lavida.service;

import com.lavida.service.dao.ChangedFieldDao;
import com.lavida.service.entity.ChangedFieldJdo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * The ArticleChangedFieldService
 * <p/>
 * Created: 01.10.13 8:40.
 *
 * @author Ruslan.
 */
@Service
public class ChangedFieldService {
    @Resource
    private ChangedFieldDao changedFieldDao;

    @Transactional
    public void save (ChangedFieldJdo changedFieldJdo) {
        changedFieldDao.put(changedFieldJdo);
    }

    @Transactional
    public void update (ChangedFieldJdo changedFieldJdo) {
        changedFieldDao.update(changedFieldJdo);
    }

    @Transactional
    public void delete (int id) {
        changedFieldDao.delete(ChangedFieldJdo.class, id);
    }

    public List<ChangedFieldJdo> getAll () {
        return changedFieldDao.getAll(ChangedFieldJdo.class);
    }

    public ChangedFieldJdo getById (int id) {
        return changedFieldDao.getById(ChangedFieldJdo.class, id);
    }

/*
    public ChangedFieldJdo getByCode (String code) {
        return articleChangedFieldDao.findByCode(code);
    }

    public ChangedFieldJdo getByOperationDate (Date date) {
        return articleChangedFieldDao.findByOperationDate(date);
    }
*/

    public List<ChangedFieldJdo> get(String queryName) {
        return changedFieldDao.get(queryName);
    }
}
