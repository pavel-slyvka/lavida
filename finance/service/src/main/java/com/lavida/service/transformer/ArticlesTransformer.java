package com.lavida.service.transformer;

import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.entity.SoldArticleJdo;
import org.springframework.stereotype.Component;

/**
 * Created: 14:33 12.08.13
 * The ArticlesTransformer transforms ArticleJdo and SoldArticleJdo to each other.
 * @author Ruslan
 */


@Component
@Deprecated
public class ArticlesTransformer {
    /**
     * transforms ArticleJdo to SoldArticleJdo.
     * @param articleJdo to be transformed;
     * @return result SoldArticleJdo.
     */
    public SoldArticleJdo toSoldArticleJdo (ArticleJdo articleJdo) {
        SoldArticleJdo soldArticleJdo = new SoldArticleJdo();
        soldArticleJdo.setId(articleJdo.getId());
        soldArticleJdo.setCode((articleJdo.getCode() == null)? null : articleJdo.getCode() );
        soldArticleJdo.setName((articleJdo.getName() == null) ? null : articleJdo.getName() );
        soldArticleJdo.setBrand((articleJdo.getBrand() == null)? null : articleJdo.getBrand());
        soldArticleJdo.setQuantity(articleJdo.getQuantity());
        soldArticleJdo.setSize((articleJdo.getSize() == null)? null : articleJdo.getSize());
        soldArticleJdo.setPurchasingPriceEUR(articleJdo.getPurchasingPriceEUR());
        soldArticleJdo.setTransportCostEUR(articleJdo.getTransportCostEUR());
        soldArticleJdo.setDeliveryDate((articleJdo.getDeliveryDate() == null)? null : articleJdo.getDeliveryDate());
        soldArticleJdo.setPriceUAH(articleJdo.getPriceUAH());
        soldArticleJdo.setRaisedPriceUAH(articleJdo.getRaisedPriceUAH());
        soldArticleJdo.setActionPriceUAH(articleJdo.getActionPriceUAH());
        soldArticleJdo.setSold((articleJdo.getSold() == null)? null : articleJdo.getSold());
        soldArticleJdo.setOurs((articleJdo.getOurs() == null)? null : articleJdo.getOurs());
        soldArticleJdo.setSaleDate((articleJdo.getSaleDate() == null)? null : articleJdo.getSaleDate());
        soldArticleJdo.setComment((articleJdo.getComment() == null)? null : articleJdo.getComment());
        return soldArticleJdo;
    }

    /**
     * transforms SoldArticleJdo to ArticleJdo
     * @param soldArticleJdo to be transformed;
     * @return result ArticleJdo.
     */
    public ArticleJdo toArticleJdo (SoldArticleJdo soldArticleJdo) {
        ArticleJdo articleJdo = new ArticleJdo();
        articleJdo.setId(soldArticleJdo.getId());
        articleJdo.setCode((soldArticleJdo.getCode() == null)? null : soldArticleJdo.getCode() );
        articleJdo.setName((soldArticleJdo.getName() == null) ? null : soldArticleJdo.getName() );
        articleJdo.setBrand((soldArticleJdo.getBrand() == null)? null : soldArticleJdo.getBrand());
        articleJdo.setQuantity(soldArticleJdo.getQuantity());
        articleJdo.setSize((soldArticleJdo.getSize() == null)? null : soldArticleJdo.getSize());
        articleJdo.setPurchasingPriceEUR(soldArticleJdo.getPurchasingPriceEUR());
        articleJdo.setTransportCostEUR(soldArticleJdo.getTransportCostEUR());
        articleJdo.setDeliveryDate((soldArticleJdo.getDeliveryDate() == null)? null : soldArticleJdo.getDeliveryDate());
        articleJdo.setPriceUAH(soldArticleJdo.getPriceUAH());
        articleJdo.setRaisedPriceUAH(soldArticleJdo.getRaisedPriceUAH());
        articleJdo.setActionPriceUAH(soldArticleJdo.getActionPriceUAH());
        articleJdo.setSold((soldArticleJdo.getSold() == null)? null : soldArticleJdo.getSold());
        articleJdo.setOurs((soldArticleJdo.getOurs() == null)? null : soldArticleJdo.getOurs());
        articleJdo.setSaleDate((soldArticleJdo.getSaleDate() == null)? null : soldArticleJdo.getSaleDate());
        articleJdo.setComment((soldArticleJdo.getComment() == null)? null : soldArticleJdo.getComment());
        return articleJdo;
    }
}
