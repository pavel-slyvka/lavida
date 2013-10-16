package com.lavida.swing.service;

import com.google.gdata.util.ServiceException;
import com.lavida.service.DiscountCardService;
import com.lavida.service.DiscountCardsUpdateInfo;
import com.lavida.service.entity.ChangedFieldJdo;
import com.lavida.service.entity.DiscountCardJdo;
import com.lavida.service.remote.google.LavidaGoogleException;
import com.lavida.swing.event.DiscountCardUpdateEvent;
import com.lavida.swing.event.PostponedOperationEvent;
import com.lavida.swing.exception.LavidaSwingRuntimeException;
import com.lavida.swing.exception.RemoteUpdateException;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
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

//    @Resource
//    private DiscountCardsXmlService discountCardsXmlService;

    @Resource
    private ChangedFieldServiceSwingWrapper changedFieldServiceSwingWrapper;

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

    public List<DiscountCardJdo> loadDiscountCardsFromRemoteServer() throws IOException, ServiceException, LavidaGoogleException {
        return discountCardService.loadDiscountCardsFromRemoteServer();
    }

    /**
     * Updates discount cards from remote server to database according to equivalence by number.
     *
     * @param remoteDiscountCards the List of discount cards to be updated to database;
     * @return the discountCardsUpdateInfo object with the information about updating process.
     */
    public DiscountCardsUpdateInfo updateDatabaseFromRemote(List<DiscountCardJdo> remoteDiscountCards) {
        DiscountCardsUpdateInfo discountCardsUpdateInfo = discountCardService.updateDatabaseFromRemote(remoteDiscountCards);
        applicationContext.publishEvent(new DiscountCardUpdateEvent(this));
        return discountCardsUpdateInfo;
    }

    /**
     * Finds equivalent discount cards from the database to match loaded discount cards with postponed operations.
     * @param loadedDiscountCards the List < {@link com.lavida.service.entity.ArticleJdo} > with postponed operations.
     */
    public List<DiscountCardJdo> mergePostponedWithDatabase(List<DiscountCardJdo> loadedDiscountCards) {
        return discountCardService.mergePostponedWithDatabase(loadedDiscountCards);
    }

    public void updateToSpreadsheet(DiscountCardJdo oldDiscountCardJdo, DiscountCardJdo discountCardJdo) throws RemoteUpdateException, LavidaGoogleException {
        Date postponedDate = new Date();
        try {
            discountCardService.updateToSpreadsheet(discountCardJdo);
        } catch (IOException | ServiceException e) {
            List<ChangedFieldJdo> changedFieldJdoList = discountCardJdo.findUpdateChanges(oldDiscountCardJdo, null);
            for (ChangedFieldJdo changedFieldJdo: changedFieldJdoList) {
                changedFieldJdo.setPostponedDate(postponedDate);
            }
            changedFieldServiceSwingWrapper.update(changedFieldJdoList);
            discountCardJdo.setPostponedDate(postponedDate);
            update(discountCardJdo);
            applicationContext.publishEvent(new PostponedOperationEvent(this));
            throw new RemoteUpdateException(e);
        }
        update(discountCardJdo);
    }

/*
    public void saveToXml(List<DiscountCardJdo> discountCardJdoList, File file) throws JAXBException, IOException {
        String filePath = file.getAbsolutePath();
        discountCardsXmlService.marshal(discountCardJdoList, filePath);
    }

    public List<DiscountCardJdo> loadFromXml (File file) throws JAXBException, FileNotFoundException {
        String filePath = file.getAbsolutePath();
        return discountCardsXmlService.unmarshal(filePath);
    }
*/

}
