package com.lavida.service;

import com.lavida.service.dao.SellerDao;
import com.lavida.service.entity.SellerJdo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * The SellerService is a service layer for the {@link com.lavida.service.entity.SellerJdo}.
 * Created: 13:08 02.09.13
 *
 * @author Ruslan
 */
@Service
public class SellerService {

    @Resource
    private SellerDao sellerDao;

    @Transactional
    public void save(SellerJdo sellerJdo) {
        sellerDao.put(sellerJdo);
    }

    @Transactional
    public void update(SellerJdo sellerJdo) {
        sellerDao.update(sellerJdo);
    }

    public void delete(int id) {
        sellerDao.delete(SellerJdo.class, id);
    }

    public SellerJdo getById(int id) {
        return sellerDao.getById(SellerJdo.class, id);
    }

    public List<SellerJdo> getAll() {
        return sellerDao.getAll(SellerJdo.class);
    }

    public SellerJdo getByName (String name) {
        return sellerDao.getSellerByName(name);
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-security.xml");
        SellerService service = context.getBean(SellerService.class);
        service.save(new SellerJdo("Seller 1"));
        service.save(new SellerJdo("Seller 2"));

        System.out.println(service.getAll());


    }
}
