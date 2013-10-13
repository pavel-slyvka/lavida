package com.lavida.service;

import com.lavida.service.dao.BrandDao;
import com.lavida.service.entity.BrandJdo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * The BrandService
 * <p/>
 * Created: 12.10.13 10:04.
 *
 * @author Ruslan.
 */
@Service
public class BrandService {

    @Resource
    private BrandDao brandDao;

    @Transactional
    public void save (BrandJdo brandJdo){
        brandDao.put(brandJdo);
    }

    @Transactional
    public void update (BrandJdo brandJdo) {
        brandDao.update(brandJdo);
    }

    @Transactional
    public void delete (int id) {
        brandDao.delete(BrandJdo.class, id);
    }

    public BrandJdo getById (int id) {
        return brandDao.getById(BrandJdo.class, id);
    }

    public List<BrandJdo> getAll () {
        return brandDao.getAll(BrandJdo.class);
    }

    public BrandJdo getByName (String name) {
        return brandDao.getByName(name);
    }

    public void update (List<BrandJdo> brandJdoList) {
        for (BrandJdo brandJdo : brandJdoList) {
            update(brandJdo);
        }
    }

}
