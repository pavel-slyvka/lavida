package com.lavida.service;

import com.lavida.service.dao.DiscountCardDao;
import com.lavida.service.entity.DiscountCardJdo;
import com.lavida.service.entity.SellerJdo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.List;

/**
 * The service layer for the {@link com.lavida.service.entity.DiscountCardJdo}.
 * Created: 8:55 06.09.13
 *
 * @author Ruslan
 */
@Service
public class DiscountCardService {

    @Resource
    private DiscountCardDao discountCardDao;

    @Transactional
    public void save (DiscountCardJdo discountCardJdo) {
        discountCardDao.put(discountCardJdo);
    }

    @Transactional
    public void update (DiscountCardJdo discountCardJdo) {
        discountCardDao.update(discountCardJdo);
    }

    @Transactional
    public void delete (int id) {
        discountCardDao.delete(DiscountCardJdo.class, id);
    }

    public List<DiscountCardJdo> getAll () {
        return discountCardDao.getAll(DiscountCardJdo.class);
    }

    public List<DiscountCardJdo> get (String query) {
        return discountCardDao.get(query);
    }

    public DiscountCardJdo getByNumber (int number) {
        return  discountCardDao.getByNumber(number);
    }

    public DiscountCardJdo getById (int id) {
        return discountCardDao.getById(DiscountCardJdo.class, id);
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-security.xml");
        DiscountCardService service = context.getBean(DiscountCardService.class);
        service.save(new DiscountCardJdo(1, "name1", "phone1", "address1", "eMail1", 0, 0, Calendar.getInstance(), null));
        service.save(new DiscountCardJdo(2, "name2", "phone2", "address2", "eMail2", 0, 0, Calendar.getInstance(), null));
        System.out.println(service.getAll());
    }

}
