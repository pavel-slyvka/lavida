package com.lavida.service.entity;


import com.lavida.service.google.SpreadsheetColumn;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Created: 8:15 05.08.13
 * The {@code ArticleJdo} is entity for article of goods related to database.
 *
 * @author Ruslan
 */
@Entity
@Table(name = "goods")
@NamedQueries({
        @NamedQuery(name = ArticleJdo.FIND_NOT_SOLD, query = "select a from ArticleJdo a where a.sold IS NULL"),
        @NamedQuery(name = ArticleJdo.FIND_SOLD, query = "select a from ArticleJdo a where a.sold IS NOT NULL")
})
public class ArticleJdo {
    public static final String FIND_NOT_SOLD = "ArticleJdo.findSold";
    public static final String FIND_SOLD = "ArticleJdo.findNotSold";

    @Id
    private int id;

    @SpreadsheetColumn(sheetColumn = "code", titleKey = "mainForm.table.articles.column.code.title")
    private String code;

    @SpreadsheetColumn(sheetColumn = "name", titleKey = "mainForm.table.articles.column.name.title")
    private String name;

    @SpreadsheetColumn(sheetColumn = "brand", titleKey = "mainForm.table.articles.column.brand.title")
    private String brand;

    private int quantity;

    @SpreadsheetColumn(sheetColumn = "size", titleKey = "mainForm.table.articles.column.size.title")
    private String size;

    @SpreadsheetColumn(sheetColumn = "purchasePriceEur", titleKey = "mainForm.table.articles.column.purchase.price.eur.title", forbiddenRoles = "ROLE_SELLER")
    private double purchasingPriceEUR;

    private double transportCostEUR;// , forbiddenRoles = "ROLE_SELLER"

    @SpreadsheetColumn(sheetColumn = "purchaseDate", titleKey = "mainForm.table.articles.column.purchase.date.title")
    @Temporal(TemporalType.DATE)
    private Calendar deliveryDate;

    @SpreadsheetColumn(sheetColumn = "sellPrice", titleKey = "mainForm.table.articles.column.sell.price.uah.title")
    private double priceUAH;

    private double raisedPriceUAH;

    private double actionPriceUAH;

    @SpreadsheetColumn(sheetColumn = "sold", show = false)
    private String sold;

    @SpreadsheetColumn(sheetColumn = "sellMarker", titleKey = "mainForm.table.articles.column.sell.marker.title")
    private String ours;

    @SpreadsheetColumn(sheetColumn = "sellDate", titleKey = "mainForm.table.articles.column.sell.date.title")
    @Temporal(TemporalType.DATE)
    private Calendar saleDate;

    @SpreadsheetColumn(sheetColumn = "comment", titleKey = "mainForm.table.articles.column.comment.title")
    private String comment;

    public ArticleJdo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public double getPurchasingPriceEUR() {
        return purchasingPriceEUR;
    }

    public void setPurchasingPriceEUR(double purchasingPriceEUR) {
        this.purchasingPriceEUR = purchasingPriceEUR;
    }

    public double getTransportCostEUR() {
        return transportCostEUR;
    }

    public void setTransportCostEUR(double transportCostEUR) {
        this.transportCostEUR = transportCostEUR;
    }

    public Calendar getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Calendar deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public double getPriceUAH() {
        return priceUAH;
    }

    public void setPriceUAH(double priceUAH) {
        this.priceUAH = priceUAH;
    }

    public double getRaisedPriceUAH() {
        return raisedPriceUAH;
    }

    public void setRaisedPriceUAH(double raisedPriceUAH) {
        this.raisedPriceUAH = raisedPriceUAH;
    }

    public double getActionPriceUAH() {
        return actionPriceUAH;
    }

    public void setActionPriceUAH(double actionPriceUAH) {
        this.actionPriceUAH = actionPriceUAH;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getOurs() {
        return ours;
    }

    public void setOurs(String ours) {
        this.ours = ours;
    }

    public Calendar getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Calendar saleDate) {
        this.saleDate = saleDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArticleJdo that = (ArticleJdo) o;

        if (Double.compare(that.actionPriceUAH, actionPriceUAH) != 0) return false;
        if (id != that.id) return false;
        if (Double.compare(that.priceUAH, priceUAH) != 0) return false;
        if (Double.compare(that.purchasingPriceEUR, purchasingPriceEUR) != 0) return false;
        if (quantity != that.quantity) return false;
        if (Double.compare(that.raisedPriceUAH, raisedPriceUAH) != 0) return false;
        if (Double.compare(that.transportCostEUR, transportCostEUR) != 0) return false;
        if (brand != null ? !brand.equals(that.brand) : that.brand != null) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (deliveryDate != null ? !deliveryDate.equals(that.deliveryDate) : that.deliveryDate != null) return false;
        if (!name.equals(that.name)) return false;
        if (ours != null ? !ours.equals(that.ours) : that.ours != null) return false;
        if (saleDate != null ? !saleDate.equals(that.saleDate) : that.saleDate != null) return false;
        if (size != null ? !size.equals(that.size) : that.size != null) return false;
        if (sold != null ? !sold.equals(that.sold) : that.sold != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + name.hashCode();
        result = 31 * result + (brand != null ? brand.hashCode() : 0);
        result = 31 * result + quantity;
        result = 31 * result + (size != null ? size.hashCode() : 0);
        temp = Double.doubleToLongBits(purchasingPriceEUR);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(transportCostEUR);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (deliveryDate != null ? deliveryDate.hashCode() : 0);
        temp = Double.doubleToLongBits(priceUAH);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(raisedPriceUAH);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(actionPriceUAH);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (sold != null ? sold.hashCode() : 0);
        result = 31 * result + (ours != null ? ours.hashCode() : 0);
        result = 31 * result + (saleDate != null ? saleDate.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ArticleJdo{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", quantity=" + quantity +
                ", size='" + size + '\'' +
                ", purchasingPriceEUR=" + purchasingPriceEUR +
                ", transportCostEUR=" + transportCostEUR +
                ", deliveryDate='" + ((deliveryDate == null) ? null : (deliveryDate.get(Calendar.MONTH) + "/" +
                deliveryDate.get(Calendar.DATE) + "/" + deliveryDate.get(Calendar.YEAR))) + '\'' +
                ", priceUAH=" + priceUAH +
                ", raisedPriceUAH=" + raisedPriceUAH +
                ", actionPriceUAH=" + actionPriceUAH +
                ", sold= '" + sold + '\'' +
                ", ours='" + ours + '\'' +
                ", saleDate='" + ((saleDate == null) ? null : (saleDate.get(Calendar.MONTH) + "/" +
                saleDate.get(Calendar.DAY_OF_MONTH) + "/" + saleDate.get(Calendar.YEAR))) + '\'' +
                ", comment='" + comment + '\'' +
                '}';
    }
}
