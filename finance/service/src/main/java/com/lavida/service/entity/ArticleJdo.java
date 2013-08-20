package com.lavida.service.entity;


import com.lavida.service.FilterColumn;
import com.lavida.service.FilterType;
import com.lavida.service.ViewColumn;
import com.lavida.service.remote.SpreadsheetColumn;
import com.lavida.service.utils.CalendarConverter;
import com.lavida.service.utils.DateConverter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

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
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    private int spreadsheetRow;

    @SpreadsheetColumn(column = "code")
    @ViewColumn(titleKey = "mainForm.table.articles.column.code.title")
    @FilterColumn(labelKey = "mainForm.label.search.by.code", orderForSell = 2, orderForSold = 2)
    private String code;

    @SpreadsheetColumn(column = "name")
    @ViewColumn(titleKey = "mainForm.table.articles.column.name.title")
    @FilterColumn(type = FilterType.PART_TEXT, labelKey = "mainForm.label.search.by.name", orderForSell = 1, orderForSold = 1)
    private String name;

    @SpreadsheetColumn(column = "brand")
    @ViewColumn(titleKey = "mainForm.table.articles.column.brand.title")
//    @FilterColumn(labelKey = "mainForm.label.search.by.brand", showForSell = false, orderForSold = 5)
    private String brand;

    @SpreadsheetColumn(column = "quantity")
    @ViewColumn(titleKey = "mainForm.table.articles.column.quantity.title")
    private int quantity;

    @SpreadsheetColumn(column = "size")
    @ViewColumn(titleKey = "mainForm.table.articles.column.size.title")
    private String size;

    @SpreadsheetColumn(column = "purchasingPriceEUR")
    @ViewColumn(titleKey = "mainForm.table.articles.column.purchase.price.eur.title", forbiddenRoles = "ROLE_SELLER")
    private double purchasingPriceEUR;

    @SpreadsheetColumn(column = "transportCostEUR")
    @ViewColumn(titleKey = "mainForm.table.articles.column.transport.cost.eur.title", forbiddenRoles = "ROLE_SELLER")
    private double transportCostEUR;

    @SpreadsheetColumn(column = "deliveryDate")
//    @SpreadsheetColumn(column = "purchaseDate")
    @ViewColumn(titleKey = "mainForm.table.articles.column.purchase.date.title")
    @Temporal(TemporalType.DATE)
    private Calendar deliveryDate;

    @SpreadsheetColumn(column = "priceUAH")
//    @SpreadsheetColumn(column = "sellPrice")
    @ViewColumn(titleKey = "mainForm.table.articles.column.sell.price.uah.title")
    @FilterColumn(type = FilterType.NUMBER_DIAPASON, labelKey = "mainForm.label.search.by.price", orderForSell = 3,
            orderForSold = 3)
    private double priceUAH;

    @SpreadsheetColumn(column = "raisedPriceUAH")
    @ViewColumn(titleKey = "mainForm.table.articles.column.raised.price.uah.title", forbiddenRoles = "ROLE_SELLER")
    private double raisedPriceUAH;

    @SpreadsheetColumn(column = "actionPriceUAH")
    @ViewColumn(titleKey = "mainForm.table.articles.column.action.price.uah.title", forbiddenRoles = "ROLE_SELLER")
    private double actionPriceUAH;

    @SpreadsheetColumn(column = "sold")
    @ViewColumn(show = false)
    private String sold;

    @SpreadsheetColumn(column = "ours")
//    @SpreadsheetColumn(column = "sellMarker")
    @ViewColumn(titleKey = "mainForm.table.articles.column.sell.marker.title")
    private String ours;

    @SpreadsheetColumn(column = "saleDate")
    @ViewColumn(titleKey = "mainForm.table.articles.column.sell.date.title")
    @FilterColumn(type = FilterType.DATE_DIAPASON, labelKey = "mainForm.label.search.by.sale.date", showForSell = false, orderForSold = 4)
    @Temporal(TemporalType.DATE)
    private Calendar saleDate;

    @SpreadsheetColumn(column = "comment")
    @ViewColumn(titleKey = "mainForm.table.articles.column.comment.title")
    private String comment;

    @Temporal(TemporalType.TIMESTAMP)
    private Date postponedOperationDate;

    @SpreadsheetColumn(column = "refundDate")
    @ViewColumn(titleKey = "mainForm.table.articles.column.refund.title", forbiddenRoles = "ROLE_SELLER", datePattern = "MM.dd.yyyy HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    private Date refundDate;

    @SpreadsheetColumn(column = "financialTags")
    @ViewColumn(titleKey = "mainForm.table.articles.column.tags.title", forbiddenRoles = "ROLE_SELLER")
    private String financialTags;

    @SpreadsheetColumn(column = "shop")
    @ViewColumn(titleKey = "mainForm.table.articles.column.shop.title", forbiddenRoles = "ROLE_SELLER")
    private String shop;

    public ArticleJdo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSpreadsheetRow() {
        return spreadsheetRow;
    }

    public void setSpreadsheetRow(int spreadsheetRow) {
        this.spreadsheetRow = spreadsheetRow;
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


    public Date getPostponedOperationDate() {
        return postponedOperationDate;
    }

    public void setPostponedOperationDate(Date postponedOperationDate) {
        this.postponedOperationDate = postponedOperationDate;
    }

    public Date getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(Date refundDate) {
        this.refundDate = refundDate;
    }

    public String getFinancialTags() {
        return financialTags;
    }

    public void setFinancialTags(String financialTags) {
        this.financialTags = financialTags;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
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
                ", deliveryDate=" + CalendarConverter.convertCalendarToString(deliveryDate) +
                ", priceUAH=" + priceUAH +
                ", raisedPriceUAH=" + raisedPriceUAH +
                ", actionPriceUAH=" + actionPriceUAH +
                ", sold='" + sold + '\'' +
                ", ours='" + ours + '\'' +
                ", saleDate=" + CalendarConverter.convertCalendarToString(saleDate) +
                ", comment='" + comment + '\'' +
                ", postponedOperationDate=" + DateConverter.convertDateToString(postponedOperationDate) +
                ", refundDate=" + DateConverter.convertDateToString(refundDate) +
                ", financialTags='" + financialTags + '\'' +
                ", shop='" + shop + '\'' +
                '}';
    }
}
