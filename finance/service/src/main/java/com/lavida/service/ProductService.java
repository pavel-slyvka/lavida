package com.lavida.service;

import com.lavida.service.dao.ProductDao;
import com.lavida.service.entity.ProductJdo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * The ProductService
 * <p/>
 * Created: 24.10.13 11:26.
 *
 * @author Ruslan.
 */
@Service
public class ProductService {

    @Resource
    private ProductDao productDao;

    @Transactional
    public void save (ProductJdo productJdo) {
        productDao.put(productJdo);
    }

    @Transactional
    public void update (ProductJdo productJdo) {
        productDao.update(productJdo);
    }

    @Transactional
    public void delete (int id) {
        productDao.delete(ProductJdo.class, id);
    }

    public List<ProductJdo> getAll() {
        return productDao.getAll(ProductJdo.class);
    }

    public ProductJdo getById (int id) {
        return productDao.getById(ProductJdo.class, id);
    }

}
