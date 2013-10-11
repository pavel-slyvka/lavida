package com.lavida.service;

import com.google.gdata.util.ServiceException;
import com.lavida.service.dao.DiscountCardDao;
import com.lavida.service.entity.ChangedFieldJdo;
import com.lavida.service.entity.DiscountCardJdo;
import com.lavida.service.remote.RemoteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

    @Resource
    private RemoteService remoteService;


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

    @Transactional
    public DiscountCardsUpdateInfo updateDatabaseFromRemote(List<DiscountCardJdo> remoteDiscountCards) {
        List<DiscountCardJdo> dbOldDiscountCards = getAll();
        List<DiscountCardJdo> discountCardsToUpdate = new ArrayList<DiscountCardJdo>();
        List<DiscountCardJdo> discountCardsToDelete = new ArrayList<DiscountCardJdo>();

        List<ChangedFieldJdo> changedFieldJdoList = new ArrayList<>();
        Date operationDate = new Date();
        l:
        for (DiscountCardJdo dbOldDiscountCard : dbOldDiscountCards) {
            if (dbOldDiscountCard.getPostponedDate() != null) {
                throw new RuntimeException("Postponed operations must be operated firstly!");
            }
            for (int i = 0; i < remoteDiscountCards.size(); ++i) {
                DiscountCardJdo remoteCard = remoteDiscountCards.get(i);
                if (dbOldDiscountCard.getSpreadsheetRow() == remoteCard.getSpreadsheetRow()) {
                    remoteDiscountCards.remove(i);
                    if (!dbOldDiscountCard.equals(remoteCard)) {
                        remoteCard.setId(dbOldDiscountCard.getId());
                        discountCardsToUpdate.add(remoteCard);
                        changedFieldJdoList.addAll(remoteCard.findUpdateChanges(dbOldDiscountCard,
                                ChangedFieldJdo.RefreshOperationType.UPDATED));
                    }
                    continue l;
                }
            }
            discountCardsToDelete.add(dbOldDiscountCard);
            changedFieldJdoList.add(new ChangedFieldJdo(operationDate, ChangedFieldJdo.ObjectType.DISCOUNT_CARD, dbOldDiscountCard.getId(),
                    dbOldDiscountCard.getNumber(), null, null, null, null, ChangedFieldJdo.RefreshOperationType.DELETED, null));
        }

        for (DiscountCardJdo discountCardJdo : remoteDiscountCards) {
            changedFieldJdoList.add(new ChangedFieldJdo(operationDate, ChangedFieldJdo.ObjectType.DISCOUNT_CARD, discountCardJdo.getId(),
                    discountCardJdo.getNumber(), null, null, null, null, ChangedFieldJdo.RefreshOperationType.SAVED, null));

        }

        DiscountCardsUpdateInfo discountCardsUpdateInfo = new DiscountCardsUpdateInfo();
        discountCardsUpdateInfo.setAddedCount(remoteDiscountCards.size());
        discountCardsUpdateInfo.setUpdatedCount(discountCardsToUpdate.size());
        discountCardsUpdateInfo.setDeletedCount(discountCardsToDelete.size());
        discountCardsUpdateInfo.setChangedFieldJdoList(changedFieldJdoList);
        remove(discountCardsToDelete);
        update(discountCardsToUpdate);
        save(remoteDiscountCards);
        return discountCardsUpdateInfo;
    }

    void save(List<DiscountCardJdo> discountCardsToSave) {
        for (DiscountCardJdo discountCardJdo : discountCardsToSave) {
            save(discountCardJdo);
        }
    }

    void update(List<DiscountCardJdo> discountCardsToUpdate) {
        for (DiscountCardJdo discountCardJdo : discountCardsToUpdate) {
            update(discountCardJdo);
        }
    }

    void remove(List<DiscountCardJdo> discountCardsToRemove) {
        for (DiscountCardJdo discountCardJdo : discountCardsToRemove) {
            delete(discountCardJdo.getId());
        }
    }


    public List<DiscountCardJdo> getAll () {
        return discountCardDao.getAll(DiscountCardJdo.class);
    }

    public List<DiscountCardJdo> get (String query) {
        return discountCardDao.get(query);
    }

    public DiscountCardJdo getByNumber (String number) {
        return  discountCardDao.getByNumber(number);
    }

    public DiscountCardJdo getById (int id) {
        return discountCardDao.getById(DiscountCardJdo.class, id);
    }

 /*   public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-security.xml");
        DiscountCardService service = context.getBean(DiscountCardService.class);
        service.save(new DiscountCardJdo("1", "name1", "phone1", "address1", "eMail1", 0, 0, 0, Calendar.getInstance(), null, null));
        service.save(new DiscountCardJdo("2", "name2", "phone2", "address2", "eMail2", 0, 0, 0, Calendar.getInstance(), null, null));
        System.out.println(service.getAll());
    }
*/

    public List<DiscountCardJdo> loadDiscountCardsFromRemoteServer() throws IOException, ServiceException {
        return remoteService.loadDiscountCards();
    }

    public void updateToSpreadsheet(DiscountCardJdo discountCardJdo) throws IOException, ServiceException {
        remoteService.updateDiscountCardToRemote(discountCardJdo);
    }

    /**
     * Finds equivalent discount cards from the database to match loaded discount cards with postponed operations.
     *
     * @param loadedDiscountCards the List < {@link com.lavida.service.entity.DiscountCardJdo} > with postponed operations.
     */
    public List<DiscountCardJdo> mergePostponedWithDatabase(List<DiscountCardJdo> loadedDiscountCards) {
        List<DiscountCardJdo> fromDatabaseDiscountCards = getAll();
        List<DiscountCardJdo> forUpdateDiscountCards = new ArrayList<>();
        label:
        for (DiscountCardJdo loadedDiscountCard : loadedDiscountCards) {
            if (loadedDiscountCard.getPostponedDate() != null) {
                for (DiscountCardJdo fromDatabaseDiscountCard : fromDatabaseDiscountCards) {
                    if ((loadedDiscountCard.getNumber() != null) ? loadedDiscountCard.getNumber().equals(
                            fromDatabaseDiscountCard.getNumber()) : fromDatabaseDiscountCard.getNumber() == null) {
                        fromDatabaseDiscountCard.setPostponedDate(loadedDiscountCard.getPostponedDate());
                        fromDatabaseDiscountCard.setActivationDate(loadedDiscountCard.getActivationDate());
                        fromDatabaseDiscountCard.setSumTotalUAH(loadedDiscountCard.getSumTotalUAH());
                        fromDatabaseDiscountCard.setBonusUAH(loadedDiscountCard.getBonusUAH());
                        fromDatabaseDiscountCard.setDiscountRate(loadedDiscountCard.getDiscountRate());
                        fromDatabaseDiscountCard.setName(loadedDiscountCard.getName());
                        fromDatabaseDiscountCard.setPhone(loadedDiscountCard.getPhone());
                        fromDatabaseDiscountCard.setAddress(loadedDiscountCard.getAddress());
                        fromDatabaseDiscountCard.seteMail(loadedDiscountCard.geteMail());

                        forUpdateDiscountCards.add(fromDatabaseDiscountCard);
                        continue label;
                    }
                }
            }
        }
        return forUpdateDiscountCards;
    }

}
