package com.lavida.swing;

import org.springframework.stereotype.Repository;

/**
 * Created: 10:21 14.08.13
 * For money exchanging.
 * @author Ruslan
 */
@Repository
public class ExchangerHolder {
    private double purchaseRateEUR = 10.73;
    private double sellRateEUR = 10.79;

    public double getPurchaseRateEUR() {
        return purchaseRateEUR;
    }

    public void setPurchaseRateEUR(double purchaseRateEUR) {
        this.purchaseRateEUR = purchaseRateEUR;
    }

    public double getSellRateEUR() {
        return sellRateEUR;
    }

    public void setSellRateEUR(double sellRateEUR) {
        this.sellRateEUR = sellRateEUR;
    }
}
