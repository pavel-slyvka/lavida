package com.lavida.swing.service;

import com.lavida.service.DiscountCardService;
import com.lavida.service.entity.DiscountCardJdo;
import com.lavida.swing.event.DiscountCardUpdateEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created: 12:09 08.09.13
 *
 * @author Ruslan
 */
@Service
public class DiscountCardServiceSwingWrapper  implements ApplicationContextAware{

    private ApplicationContext applicationContext;

    @Resource
    private DiscountCardService discountCardService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void update(DiscountCardJdo discountCardJdo) {
        discountCardService.update(discountCardJdo);
        applicationContext.publishEvent(new DiscountCardUpdateEvent(this));
    }

    public void save (DiscountCardJdo discountCardJdo) {
        discountCardService.save(discountCardJdo);
        applicationContext.publishEvent(new DiscountCardUpdateEvent(this));
    }

    public void delete (int id) {
        discountCardService.delete(id);
        applicationContext.publishEvent(new DiscountCardUpdateEvent(this));
    }

    public DiscountCardJdo getByNumber (String number) {
        return discountCardService.getByNumber(number);
    }

    public List<DiscountCardJdo> getAll () {
        return discountCardService.getAll();
    }

    public List<DiscountCardJdo> get (String query) {
        return discountCardService.get(query);
    }
}
