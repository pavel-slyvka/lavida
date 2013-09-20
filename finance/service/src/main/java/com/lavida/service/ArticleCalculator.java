package com.lavida.service;

import com.lavida.service.entity.ArticleJdo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * ArticleCalculator
 * <p/>
 * Created: 14:50 20.09.13
 *
 * @author Pavel
 */
@Service
public class ArticleCalculator {

    public void calculateTotalCostEUR(ArticleJdo articleJdo) {
        double totalCostEUR = articleJdo.getPurchasePriceEUR() + articleJdo.getTransportCostEUR()
                + 0.065 * (articleJdo.getPurchasePriceEUR() + articleJdo.getTransportCostEUR());
        totalCostEUR = BigDecimal.valueOf(totalCostEUR).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        articleJdo.setTotalCostEUR(totalCostEUR);
    }

    public void calculateTotalCostUAH(ArticleJdo articleJdo) {
        double totalCostUAH = articleJdo.getTotalCostEUR() * 11.0;
        totalCostUAH = BigDecimal.valueOf(totalCostUAH).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        articleJdo.setTotalCostUAH(totalCostUAH);
    }

    public void calculateCalculatedSalePrice(ArticleJdo articleJdo) {
        double calculatedSalePrice = articleJdo.getTotalCostUAH() * articleJdo.getMultiplier() * 1.1;
        calculatedSalePrice = BigDecimal.valueOf(calculatedSalePrice).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        articleJdo.setCalculatedSalePrice(calculatedSalePrice);
    }

    public void calculateTotalCostsAndCalculatedSalePrice(ArticleJdo articleJdo) {
        calculateTotalCostEUR(articleJdo);
        calculateTotalCostUAH(articleJdo);
        calculateCalculatedSalePrice(articleJdo);
    }
}