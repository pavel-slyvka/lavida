package com.lavida.swing.service;

import com.lavida.service.ChangedFieldService;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.entity.ChangedFieldJdo;
import com.lavida.service.entity.DiscountCardJdo;
import com.lavida.swing.event.ChangedFieldEvent;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The ArticleChangedFieldServiceSwingWrapper
 * <p/>
 * Created: 01.10.13 21:23.
 *
 * @author Ruslan.
 */
@Service
public class ChangedFieldServiceSwingWrapper implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Resource
    private ChangedFieldService changedFieldService;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void update (ChangedFieldJdo changedFieldJdo) {
        changedFieldService.update(changedFieldJdo);
        applicationContext.publishEvent(new ChangedFieldEvent(this));
    }

    public void update (List<ChangedFieldJdo> changedFieldJdoList) {
        for (ChangedFieldJdo changedFieldJdo : changedFieldJdoList) {
            update(changedFieldJdo);
        }
    }

    public List<ChangedFieldJdo> get(String queryName) {
        return changedFieldService.get(queryName);
    }

    public List<ChangedFieldJdo> getAll(){
        return changedFieldService.getAll();
    }

    public void delete (ChangedFieldJdo changedFieldJdo) {
        changedFieldService.delete(changedFieldJdo.getId());
        applicationContext.publishEvent(new ChangedFieldEvent(this));
    }

    public void delete (Object object) {
        ChangedFieldJdo.ObjectType objectType = null;
        int objectId = 0;
        if (object instanceof ArticleJdo) {
            objectType = ChangedFieldJdo.ObjectType.ARTICLE;
            objectId = ((ArticleJdo) object).getId();
        }else if (object instanceof DiscountCardJdo) {
            objectType = ChangedFieldJdo.ObjectType.DISCOUNT_CARD;
            objectId = ((DiscountCardJdo)object).getId();
        } else {
            throw new RuntimeException("Unsupported object type for: " + object);
        }
        List<ChangedFieldJdo> changedFieldJdoListToDelete = new ArrayList<>();
        for (ChangedFieldJdo changedFieldJdo : getAll()) {
            if (objectType.equals(changedFieldJdo.getObjectType()) && objectId == changedFieldJdo.getObjectId()) {
                changedFieldJdoListToDelete.add(changedFieldJdo);
            }
        }
        for (ChangedFieldJdo changedFieldJdo : changedFieldJdoListToDelete) {
            delete(changedFieldJdo);
        }
    }
    /**
     * Clears the database table of articleChangedField when table model initializes.
     * @return the list of changedFieldJdo without refreshed items that have operationDate less then current Date.
     */
    public List<ChangedFieldJdo> clearAndReturnForCurrentDay () {
        List<ChangedFieldJdo> oldArticleChangedFieldList = changedFieldService.getAll();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        String currentDay = dateFormat.format(new Date());
        for (ChangedFieldJdo changedFieldJdo : oldArticleChangedFieldList) {
            if (!currentDay.equals(dateFormat.format(changedFieldJdo.getOperationDate())) &&
                    changedFieldJdo.getPostponedDate() == null) {
                changedFieldService.delete(changedFieldJdo.getId());
            }
        }
        return changedFieldService.getAll();
    }
}
