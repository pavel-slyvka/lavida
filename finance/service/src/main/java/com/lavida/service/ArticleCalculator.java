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

    public double fixIfNeedAndParseDouble(String doubleString) {  // todo make reusable with GoogleCellsTransformer
        if (doubleString == null || doubleString.trim().isEmpty()) {
            return 0;
        }
            doubleString = doubleString.replace(" ", "");
        doubleString = doubleString.replaceAll("[^0-9]", ".");
        return Double.parseDouble(doubleString);
    }

    public void calculateMultiplier(ArticleJdo articleJdo) {
        if (articleJdo.getTotalCostUAH() == 0) return;
        double multiplier = articleJdo.getSalePrice() / articleJdo.getTotalCostUAH();
        multiplier = BigDecimal.valueOf(multiplier).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
        articleJdo.setMultiplier(multiplier);
        calculateCalculatedSalePrice(articleJdo);
    }
}