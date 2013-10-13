package com.lavida.service;

import com.lavida.service.dao.ShopDao;
import com.lavida.service.entity.ShopJdo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * The ShopService
 * <p/>
 * Created: 13.10.13 19:40.
 *
 * @author Ruslan.
 */
@Service
public class ShopService {

    @Resource
    private ShopDao shopDao;

    @Transactional
    public void save(ShopJdo shopJdo) {
        shopDao.put(shopJdo);
    }

    @Transactional
    public void update(ShopJdo shopJdo) {
        shopDao.update(shopJdo);
    }

    @Transactional
    public void delete(int id){
        shopDao.delete(ShopJdo.class, id);
    }

    public ShopJdo getByName(String name) {
        return shopDao.getByName(name);
    }

    public List<ShopJdo> getAll(){
        return shopDao.getAll(ShopJdo.class);
    }

}
