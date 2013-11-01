package com.lavida.service;

import com.lavida.service.dao.UniversalProductDao;
import com.lavida.service.entity.UniversalProductJdo;
import com.lavida.service.entity.UserJdo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * UniversalProductService
 * Created: 28.10.13 12:53.
 *
 * @author Ruslan
 */
@Service
public class UniversalProductService {

    @Resource
    private UniversalProductDao universalProductDao;

    @Transactional
    public void save (UniversalProductJdo universalProductJdo) {
        universalProductDao.put(universalProductJdo);
    }

    @Transactional
    public void update (UniversalProductJdo universalProductJdo) {
        universalProductDao.update(universalProductJdo);
    }

    @Transactional
    public void delete (int id) {
        universalProductDao.delete(UniversalProductJdo.class, id);
    }

    public List<UniversalProductJdo> getAll() {
        return universalProductDao.getAll(UniversalProductJdo.class);
    }

    public UniversalProductJdo getById (int id) {
        return universalProductDao.getById(UniversalProductJdo.class, id);
    }

    public void update (List<UniversalProductJdo> universalProductJdoList) {
        for (UniversalProductJdo universalProductJdo : universalProductJdoList) {
            update(universalProductJdo);
        }
    }
}
