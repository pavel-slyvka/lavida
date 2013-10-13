package com.lavida.service;

import com.lavida.service.dao.SizeDao;
import com.lavida.service.entity.SizeJdo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * The SizeService
 * <p/>
 * Created: 13.10.13 18:38.
 *
 * @author Ruslan.
 */
@Service
public class SizeService {

    @Resource
    private SizeDao sizeDao;

    @Transactional
    public void save(SizeJdo sizeJdo) {
        sizeDao.put(sizeJdo);
    }

    @Transactional
    public void update(SizeJdo sizeJdo) {
        sizeDao.update(sizeJdo);
    }

    @Transactional
    public void delete(int id) {
        sizeDao.delete(SizeJdo.class, id);
    }

    public SizeJdo getByName(String name) {
        return sizeDao.getByName(name);
    }

    public List<SizeJdo> getAll(){
        return sizeDao.getAll(SizeJdo.class);
    }
}
