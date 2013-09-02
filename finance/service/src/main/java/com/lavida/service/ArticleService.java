package com.lavida.service;

import com.google.gdata.util.ServiceException;
import com.lavida.service.dao.ArticleDao;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.remote.RemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * The {@code ArticleService} is a service for ArticleJdo to work with database.
 * Created: 10:10 05.08.13
 *
 * @author Ruslan
 */
@Service
public class ArticleService {
    private final static Logger logger = LoggerFactory.getLogger(ArticleService.class);

    @Resource
    private ArticleDao articleDao;

    @Resource
    private RemoteService remoteService;

    @Transactional
    public void save(ArticleJdo articleJdo) {
        articleDao.put(articleJdo);
    }

    @Transactional
    public void update(ArticleJdo articleJdo) {
        articleDao.update(articleJdo);
    }

    public List<ArticleJdo> fixArticles(List<ArticleJdo> articles) {
        final String ERROR = "ERROR";
        List<ArticleJdo> articlesForUpdate = new ArrayList<ArticleJdo>();
        for (int i = 0; i < articles.size(); ++i) {
            ArticleJdo articleJdo = articles.get(i);
            boolean updated = false;
            if (!Integer.toString(i + 1).equals(articleJdo.getSpreadsheetNum())) {
                articleJdo.setSpreadsheetNum(Integer.toString(i + 1));
                updated = true;
            }
            if (articleJdo.getCode() == null || articleJdo.getCode().trim().isEmpty()) {
                articleJdo.setCode(ERROR);
                updated = true;
            }
            if (articleJdo.getBrand() == null || articleJdo.getBrand().trim().isEmpty()) {
                articleJdo.setBrand("Unknown");
                updated = true;
            }
            if (articleJdo.getDeliveryDate() == null) {
                logger.warn("Article deliveryDate == null: " + articleJdo);
                // todo
            }
            if (articleJdo.getPurchasePriceEUR() == 0 && articleJdo.getTotalCostUAH() == 0) {
                logger.warn("Article purchasePriceEUR == null: " + articleJdo);
                //todo
            }
            if (articleJdo.getTransportCostEUR() == 0 && articleJdo.getTotalCostUAH() == 0) {
                logger.warn("Article transportCostEUR == null: " + articleJdo);
                //todo
            }
            double cost = articleJdo.getPurchasePriceEUR() + articleJdo.getTransportCostEUR() + articleJdo.getPurchasePriceEUR() / 100 * 6.5;
            cost = BigDecimal.valueOf(cost).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            if (articleJdo.getTotalCostEUR() != cost) {
                articleJdo.setTotalCostEUR(cost);
                updated = true;
            }
            double costUAH = articleJdo.getTotalCostEUR() * 11; //todo
            costUAH = BigDecimal.valueOf(costUAH).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
            if (articleJdo.getTotalCostEUR() != 0 && articleJdo.getTotalCostUAH() != costUAH) {
                articleJdo.setTotalCostUAH(costUAH);
                updated = true;
            }
            if (articleJdo.getSalePrice() == 0 && (articleJdo.getSellType() == null || !"В подарок".equals(articleJdo.getSellType().trim()))) {
                logger.warn("Article salePrice == null: " + articleJdo);
                //todo
            }
            if (articleJdo.getMultiplier() == 0) {
                if (articleJdo.getTotalCostUAH() == 0) {
                    logger.warn("Article totalCostUAH == 0: " + articleJdo);
                } else {
                    double multi = BigDecimal.valueOf(articleJdo.getSalePrice() / articleJdo.getTotalCostUAH()).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                    articleJdo.setMultiplier(multi);
                    updated = true;
                }
            }
            if (articleJdo.getCalculatedSalePrice() == 0) {
                articleJdo.setCalculatedSalePrice(articleJdo.getTotalCostUAH() * articleJdo.getMultiplier());
                updated = true;
            }
            if (articleJdo.getRaisedSalePrice() != 0 && articleJdo.getSalePrice() > articleJdo.getOldSalePrice()) {
                double buf = articleJdo.getSalePrice();
                articleJdo.setSalePrice(articleJdo.getOldSalePrice());
                articleJdo.setOldSalePrice(buf);
                updated = true;
            }
            if (articleJdo.getSellType() != null && !articleJdo.getSellType().trim().isEmpty()
                    && (articleJdo.getSold() == null || articleJdo.getSold().trim().isEmpty())) {
                articleJdo.setSold("Продано");
                updated = true;
            }
            if (articleJdo.getSold() != null && !articleJdo.getSold().trim().isEmpty()
                    && articleJdo.getSaleDate() == null) {
                logger.warn("Article sale date == null: " + articleJdo);
                // todo
            }
            if (articleJdo.getShop() == null || articleJdo.getShop().trim().isEmpty()) {
                articleJdo.setShop("LA VIDA");
                updated = true;
            }
            if (articleJdo.getQuantity() > 1) {
                try {
                    for (int j = 0; j < articleJdo.getQuantity() - 1; ++j) {
                        ArticleJdo newArticleJdo = (ArticleJdo) articleJdo.clone();
                        newArticleJdo.setQuantity(1);
                        articles.add(i + 1, newArticleJdo);
//                        articlesForUpdate.add(newArticleJdo);
//                        i++;
                    }
                    articleJdo.setQuantity(1);
                    updated = true;
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            if (updated) {
                articlesForUpdate.add(articleJdo);
            }
        }
        return articlesForUpdate;
    }

    @Transactional
    public ArticleUpdateInfo updateDatabaseFromRemote(List<ArticleJdo> remoteArticles) {
        List<ArticleJdo> dbOldArticles = getAll();

        List<ArticleJdo> articlesToUpdate = new ArrayList<ArticleJdo>();
        List<ArticleJdo> articlesToDelete = new ArrayList<ArticleJdo>();
        l:
        for (ArticleJdo dbOldArticle : dbOldArticles) {
            if (dbOldArticle.getPostponedOperationDate() != null) {
                throw new RuntimeException("Postponed operations must be operated firstly!");
            }
            for (int i = 0; i < remoteArticles.size(); ++i) {
                ArticleJdo remoteArticle = remoteArticles.get(i);
                if (dbOldArticle.getSpreadsheetRow() == remoteArticle.getSpreadsheetRow()) {
                    remoteArticles.remove(i);
                    if (!dbOldArticle.equals(remoteArticle)) {
                        remoteArticle.setId(dbOldArticle.getId());
                        articlesToUpdate.add(remoteArticle);
                    }
                    continue l;
                }
            }
            articlesToDelete.add(dbOldArticle);
        }

        ArticleUpdateInfo articleUpdateInfo = new ArticleUpdateInfo();
        articleUpdateInfo.setAddedCount(remoteArticles.size());
        articleUpdateInfo.setUpdatedCount(articlesToUpdate.size());
        articleUpdateInfo.setDeletedCount(articlesToDelete.size());
        remove(articlesToDelete);
        update(articlesToUpdate);
        save(remoteArticles);
        return articleUpdateInfo;
    }

    void save(List<ArticleJdo> articleToSave) {
        for (ArticleJdo articleJdo : articleToSave) {
            save(articleJdo);
        }
    }

    void update(List<ArticleJdo> articleToSave) {
        for (ArticleJdo articleJdo : articleToSave) {
            update(articleJdo);
        }
    }

    void remove(List<ArticleJdo> articleToSave) {
        for (ArticleJdo articleJdo : articleToSave) {
            delete(articleJdo.getId());
        }
    }

    @Transactional
    public void delete(int id) {
        articleDao.delete(ArticleJdo.class, id);
    }

    public ArticleJdo getById(int id) {
        return articleDao.getById(ArticleJdo.class, id);
    }

    public List<ArticleJdo> getAll() {
        return articleDao.getAll(ArticleJdo.class);
    }

    public ArticleJdo getByCode(String code) {
        ArticleJdo neededArticle = null;
        List<ArticleJdo> articles = articleDao.getAll(ArticleJdo.class);
        for (ArticleJdo articleJdo : articles) {
            if (articleJdo.getCode().equals(code)) {
                neededArticle = articleJdo;
                return neededArticle;
            }
        }
        throw new RuntimeException("There is no article with code: " + code + "!"); //todo create databaseException
    }

    public List<ArticleJdo> loadArticlesFromRemoteServer() throws IOException, ServiceException {
        return remoteService.loadArticles();
    }

    public void updateToSpreadsheet(ArticleJdo articleJdo, Boolean isSold) throws IOException, ServiceException {
        remoteService.updateArticleToRemote(articleJdo, isSold);
    }

    /**
     * Finds equivalent articles from the database to match loaded articles with postponed operations.
     *
     * @param loadedArticles the List < {@link com.lavida.service.entity.ArticleJdo} > with postponed operations.
     */
    public List<ArticleJdo> mergePostponedWithDatabase(List<ArticleJdo> loadedArticles) {
        List<ArticleJdo> fromDatabaseArticles = getAll();
        List<ArticleJdo> forUpdateArticles = new ArrayList<ArticleJdo>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        label:
        for (ArticleJdo loadedArticle : loadedArticles) {
            if (dateFormat.format(loadedArticle.getPostponedOperationDate()).equals(dateFormat.format(loadedArticle.getSaleDate().getTime()))
                    && !loadedArticle.getSold().isEmpty()) {   // selling
                for (ArticleJdo fromDatabaseArticle : fromDatabaseArticles) {
                    if ((loadedArticle.getCode() != null) ? loadedArticle.getCode().equals(fromDatabaseArticle.getCode()) :
                            fromDatabaseArticle.getCode() == null &&
                                    (loadedArticle.getSize() != null) ? loadedArticle.getSize().equals(fromDatabaseArticle.getSize()) :
                                    fromDatabaseArticle.getSize() == null &&
                                            fromDatabaseArticle.getSold() == null) {

                        fromDatabaseArticle.setPostponedOperationDate(loadedArticle.getPostponedOperationDate());
                        fromDatabaseArticle.setSaleDate(loadedArticle.getSaleDate());
                        fromDatabaseArticle.setSold(loadedArticle.getSold());
                        fromDatabaseArticle.setComment(loadedArticle.getComment());
                        fromDatabaseArticle.setSellType(loadedArticle.getSellType());
                        fromDatabaseArticle.setShop(loadedArticle.getShop());
                        fromDatabaseArticle.setSalePrice(loadedArticle.getSalePrice());
                        fromDatabaseArticle.setTags(loadedArticle.getTags());

                        forUpdateArticles.add(fromDatabaseArticle);
                        continue label;
                    }
                }
            }
            if (loadedArticle.getPostponedOperationDate().equals(loadedArticle.getRefundDate()) &&
                    loadedArticle.getSold().isEmpty()) {                 // refunding
                for (ArticleJdo fromDatabaseArticle : fromDatabaseArticles) {
                    if ((loadedArticle.getCode() != null) ? loadedArticle.getCode().equals(fromDatabaseArticle.getCode()) :
                            fromDatabaseArticle.getCode() == null &&
                                    (loadedArticle.getSize() != null) ? loadedArticle.getSize().equals(fromDatabaseArticle.getSize()) :
                                    fromDatabaseArticle.getSize() == null &&
                                            fromDatabaseArticle.getSold() != null) {
                        fromDatabaseArticle.setPostponedOperationDate(loadedArticle.getPostponedOperationDate());
                        fromDatabaseArticle.setRefundDate(loadedArticle.getRefundDate());
                        fromDatabaseArticle.setSaleDate(loadedArticle.getSaleDate());
                        fromDatabaseArticle.setSold(loadedArticle.getSold());
                        fromDatabaseArticle.setComment(loadedArticle.getComment());
                        fromDatabaseArticle.setSellType(loadedArticle.getSellType());
                        fromDatabaseArticle.setShop(loadedArticle.getShop());
                        fromDatabaseArticle.setSalePrice(loadedArticle.getSalePrice());
                        fromDatabaseArticle.setTags(loadedArticle.getTags());

                        forUpdateArticles.add(fromDatabaseArticle);
                        continue label;
                    }
                }
            }
            if (!loadedArticle.getPostponedOperationDate().equals(loadedArticle.getRefundDate()) &&
                    !dateFormat.format(loadedArticle.getPostponedOperationDate()).equals(dateFormat.format(loadedArticle.getSaleDate().getTime()))) {
                for (ArticleJdo fromDatabaseArticle : fromDatabaseArticles) {  // editing
                    if ((loadedArticle.getCode() != null) ? loadedArticle.getCode().equals(fromDatabaseArticle.getCode()) :
                            fromDatabaseArticle.getCode() == null &&
                                    (loadedArticle.getSize() != null) ? loadedArticle.getSize().equals(fromDatabaseArticle.getSize()) :
                                    fromDatabaseArticle.getSize() == null &&
                                            (loadedArticle.getSold() != null) ? loadedArticle.getSold().equals(fromDatabaseArticle.getSold()) :
                                            fromDatabaseArticle.getSold() == null &&
                                                    (loadedArticle.getSaleDate() != null) ? loadedArticle.getSaleDate().equals(fromDatabaseArticle.getSaleDate()) :
                                                    fromDatabaseArticle.getSaleDate() == null &&
                                                            (loadedArticle.getRefundDate() != null) ? loadedArticle.getRefundDate().equals(fromDatabaseArticle.getRefundDate()) :
                                                            fromDatabaseArticle.getRefundDate() == null) {
                        fromDatabaseArticle.setPostponedOperationDate(loadedArticle.getPostponedOperationDate());
                        fromDatabaseArticle.setComment(loadedArticle.getComment());
                        fromDatabaseArticle.setSellType(loadedArticle.getSellType());
                        fromDatabaseArticle.setShop(loadedArticle.getShop());
                        fromDatabaseArticle.setSalePrice(loadedArticle.getSalePrice());
                        fromDatabaseArticle.setTags(loadedArticle.getTags());
                        fromDatabaseArticle.setBrand(loadedArticle.getBrand());
                        fromDatabaseArticle.setName(loadedArticle.getName());
                        fromDatabaseArticle.setDeliveryDate(loadedArticle.getDeliveryDate());
                        fromDatabaseArticle.setPurchasePriceEUR(loadedArticle.getPurchasePriceEUR());
                        fromDatabaseArticle.setTransportCostEUR(loadedArticle.getTransportCostEUR());
                        fromDatabaseArticle.setTotalCostEUR(loadedArticle.getTotalCostEUR());
                        fromDatabaseArticle.setTotalCostUAH(loadedArticle.getTotalCostUAH());
                        fromDatabaseArticle.setMultiplier(loadedArticle.getMultiplier());
                        fromDatabaseArticle.setCalculatedSalePrice(loadedArticle.getCalculatedSalePrice());
                        fromDatabaseArticle.setOldSalePrice(loadedArticle.getOldSalePrice());
                        fromDatabaseArticle.setRaisedSalePrice(loadedArticle.getRaisedSalePrice());

                        forUpdateArticles.add(fromDatabaseArticle);
                        continue label;
                    }
                }
            }
        }
//        update(forUpdateArticles);
        return forUpdateArticles;
    }

    public void setArticleDao(ArticleDao articleDao) {
        this.articleDao = articleDao;
    }

    public void setRemoteService(RemoteService remoteService) {
        this.remoteService = remoteService;
    }
}
