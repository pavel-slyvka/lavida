package com.lavida.service.entity;


import com.lavida.service.FilterColumn;
import com.lavida.service.FilterType;
import com.lavida.service.ViewColumn;
import com.lavida.service.remote.SpreadsheetColumn;
import com.lavida.service.utils.CalendarConverter;
import com.lavida.service.utils.DateConverter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
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
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "articleType", namespace = "http://www.xml.lavida.com/schema/articles.com")
public class ArticleJdo implements Cloneable {
    public static final String FIND_NOT_SOLD = "ArticleJdo.findSold";
    public static final String FIND_SOLD = "ArticleJdo.findNotSold";

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @XmlElement
    private int id;

    @XmlElement(required = true)
    private int spreadsheetRow;

    @SpreadsheetColumn(column = "num")
    @XmlElement
    private String spreadsheetNum;

    @SpreadsheetColumn(column = "code")
    @ViewColumn(titleKey = "mainForm.table.articles.column.code.title")
    @FilterColumn(labelKey = "mainForm.label.search.by.code", orderForSell = 1, orderForSold = 1)
    @XmlElement
    private String code;

    @SpreadsheetColumn(column = "name")
    @ViewColumn(titleKey = "mainForm.table.articles.column.name.title", columnWidth = 250)
    @FilterColumn(type = FilterType.PART_TEXT, labelKey = "mainForm.label.search.by.name", orderForSell = 2,
            orderForSold = 2)
    @XmlElement(required = true)
    private String name;

    @SpreadsheetColumn(column = "brand")
    @ViewColumn(titleKey = "mainForm.table.articles.column.brand.title")
    @FilterColumn(type = FilterType.PART_TEXT, labelKey = "mainForm.label.search.by.brand", orderForSell = 3,
            orderForSold = 3)
    @XmlElement
    private String brand;

    @SpreadsheetColumn(column = "quantity")
    @ViewColumn(titleKey = "mainForm.table.articles.column.quantity.title")
    @XmlElement
    private int quantity;

    @SpreadsheetColumn(column = "size")
    @ViewColumn(titleKey = "mainForm.table.articles.column.size.title")
    @FilterColumn(type = FilterType.PART_TEXT,labelKey = "mainForm.label.search.by.size", orderForSell = 4,
            orderForSold = 4)
    @XmlElement
    private String size;

    @SpreadsheetColumn(column = "deliveryDate")
    @ViewColumn(titleKey = "mainForm.table.articles.column.purchase.date.title")
    @FilterColumn(type = FilterType.DATE_DIAPASON, labelKey = "mainForm.label.search.by.delivery.date",
            orderForSell = 5, orderForSold = 5)
    @Temporal(TemporalType.DATE)
    @XmlElement
    private Calendar deliveryDate;

    @SpreadsheetColumn(column = "purchasePriceEUR")
    @ViewColumn(titleKey = "mainForm.table.articles.column.purchase.price.eur.title", forbiddenRoles = "ROLE_SELLER")
    @XmlElement
    private double purchasePriceEUR;

    @SpreadsheetColumn(column = "transportCostEUR")
    @ViewColumn(titleKey = "mainForm.table.articles.column.transport.cost.eur.title", forbiddenRoles = "ROLE_SELLER")
    @XmlElement
    private double transportCostEUR;

    @SpreadsheetColumn(column = "totalCostEUR")
    @ViewColumn(titleKey = "mainForm.table.articles.column.purchase.cost.total.eur.title", forbiddenRoles = "ROLE_SELLER")
    @XmlElement
    private double totalCostEUR;

    @SpreadsheetColumn(column = "totalCostUAH")
    @ViewColumn(titleKey = "mainForm.table.articles.column.purchase.cost.total.uah.title", forbiddenRoles = "ROLE_SELLER")
    @XmlElement
    private double totalCostUAH;

    @SpreadsheetColumn(column = "multiplier")
    @ViewColumn(titleKey = "mainForm.table.articles.column.multiplier.title", forbiddenRoles = "ROLE_SELLER")
    @XmlElement
    private double multiplier;

    @SpreadsheetColumn(column = "calculatedSalePrice")
    @XmlElement
    private double calculatedSalePrice;

    @SpreadsheetColumn(column = "salePrice")
    @ViewColumn(titleKey = "mainForm.table.articles.column.sell.price.uah.title")
    @FilterColumn(type = FilterType.NUMBER_DIAPASON, labelKey = "mainForm.label.search.by.price", orderForSell = 6,
            orderForSold = 6)
    @XmlElement
    private double salePrice;

    @SpreadsheetColumn(column = "raisedSalePrice")
    @ViewColumn(titleKey = "mainForm.table.articles.column.raised.price.uah.title", forbiddenRoles = "ROLE_SELLER")
    @XmlElement
    private double raisedSalePrice;

    @SpreadsheetColumn(column = "oldSalePrice")
    @ViewColumn(titleKey = "mainForm.table.articles.column.old.price.uah.title", forbiddenRoles = "ROLE_SELLER")
    @XmlElement
    private double oldSalePrice;

    @SpreadsheetColumn(column = "sold")
//    @ViewColumn(show = false)
    @XmlElement
    private String sold;

    @SpreadsheetColumn(column = "sellType")
    @ViewColumn(titleKey = "mainForm.table.articles.column.sell.marker.title")
    @XmlElement
    private String sellType;

    @SpreadsheetColumn(column = "saleDate")
    @ViewColumn(titleKey = "mainForm.table.articles.column.sell.date.title")
    @FilterColumn(type = FilterType.DATE_DIAPASON, labelKey = "mainForm.label.search.by.sale.date",
            showForSell = false, orderForSold = 7)
    @Temporal(TemporalType.DATE)
    @XmlElement
    private Calendar saleDate;

    @SpreadsheetColumn(column = "refundDate" , datePattern = "MM.dd.yyyy HH:mm:ss")
    @ViewColumn(titleKey = "mainForm.table.articles.column.refund.title", forbiddenRoles = "ROLE_SELLER",
            datePattern = "MM.dd.yyyy HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @XmlElement
    private Date refundDate;

    @SpreadsheetColumn(column = "tags")
    @ViewColumn(titleKey = "mainForm.table.articles.column.tags.title", forbiddenRoles = "ROLE_SELLER")
    @XmlElement
    private String tags;

    @SpreadsheetColumn(column = "comment")
    @ViewColumn(titleKey = "mainForm.table.articles.column.comment.title")
    @XmlElement
    private String comment;

    @Temporal(TemporalType.TIMESTAMP)
    @XmlElement
    private Date postponedOperationDate;

    @SpreadsheetColumn(column = "shop")
    @XmlElement
    @ViewColumn(titleKey = "mainForm.table.articles.column.shop.title")
    @FilterColumn(type = FilterType.PART_TEXT,labelKey = "mainForm.label.search.by.shop", showForSell = false,
            orderForSold = 8)
    private String shop;

    @SpreadsheetColumn(column = "sellerName")
    @ViewColumn(titleKey = "mainForm.table.articles.column.sellerName.title")
    @FilterColumn(type = FilterType.PART_TEXT,labelKey = "mainForm.label.search.by.sellerName", showForSell = false,
            orderForSold = 9)
    private String sellerName;

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

    public double getPurchasePriceEUR() {
        return purchasePriceEUR;
    }

    public void setPurchasePriceEUR(double purchasingPriceEUR) {
        this.purchasePriceEUR = purchasingPriceEUR;
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

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double priceUAH) {
        this.salePrice = priceUAH;
    }

    public double getRaisedSalePrice() {
        return raisedSalePrice;
    }

    public void setRaisedSalePrice(double raisedPriceUAH) {
        this.raisedSalePrice = raisedPriceUAH;
    }

    public double getOldSalePrice() {
        return oldSalePrice;
    }

    public void setOldSalePrice(double actionPriceUAH) {
        this.oldSalePrice = actionPriceUAH;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getSellType() {
        return sellType;
    }

    public void setSellType(String ours) {
        this.sellType = ours;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String financialTags) {
        this.tags = financialTags;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getSpreadsheetNum() {
        return spreadsheetNum;
    }

    public void setSpreadsheetNum(String num) {
        this.spreadsheetNum = num;
    }

    public double getTotalCostEUR() {
        return totalCostEUR;
    }

    public void setTotalCostEUR(double totalCostEUR) {
        this.totalCostEUR = totalCostEUR;
    }

    public double getTotalCostUAH() {
        return totalCostUAH;
    }

    public void setTotalCostUAH(double totalCostUAH) {
        this.totalCostUAH = totalCostUAH;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public double getCalculatedSalePrice() {
        return calculatedSalePrice;
    }

    public void setCalculatedSalePrice(double calculatedSalePrice) {
        this.calculatedSalePrice = calculatedSalePrice;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ArticleJdo that = (ArticleJdo) o;

        if (Double.compare(that.oldSalePrice, oldSalePrice) != 0) return false;
        if (Double.compare(that.salePrice, salePrice) != 0) return false;
        if (Double.compare(that.purchasePriceEUR, purchasePriceEUR) != 0) return false;

        if (Double.compare(that.totalCostEUR, totalCostEUR) != 0) return false;
        if (Double.compare(that.totalCostUAH, totalCostUAH) != 0) return false;
        if (Double.compare(that.raisedSalePrice, raisedSalePrice) != 0) return false;
        if (Double.compare(that.multiplier, multiplier) != 0) return false;

        if (quantity != that.quantity) return false;
        if (Double.compare(that.raisedSalePrice, raisedSalePrice) != 0) return false;
        if (spreadsheetRow != that.spreadsheetRow) return false;
        if (spreadsheetNum != that.spreadsheetNum) return false;
        if (Double.compare(that.transportCostEUR, transportCostEUR) != 0) return false;
        if (brand != null ? !brand.equals(that.brand) : that.brand != null) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (comment != null ? !comment.equals(that.comment) : that.comment != null) return false;
        if (deliveryDate != null ? !deliveryDate.equals(that.deliveryDate) : that.deliveryDate != null) return false;
        if (tags != null ? !tags.equals(that.tags) : that.tags != null)
            return false;
        if (!name.equals(that.name)) return false;
        if (sellType != null ? !sellType.equals(that.sellType) : that.sellType != null) return false;
        if (refundDate != null ? refundDate.getTime()!=that.refundDate.getTime() : that.refundDate!=null) return false;
        if (saleDate != null ? !saleDate.equals(that.saleDate) : that.saleDate != null) return false;
        if (shop != null ? !shop.equals(that.shop) : that.shop != null) return false;
        if (size != null ? !size.equals(that.size) : that.size != null) return false;
        if (sold != null ? !sold.equals(that.sold) : that.sold != null) return false;
        if ((sellerName != null)? !sellerName.equals(that.sellerName) :that.sellerName != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = spreadsheetRow;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + name.hashCode();
        result = 31 * result + (brand != null ? brand.hashCode() : 0);
        result = 31 * result + quantity;
        result = 31 * result + (size != null ? size.hashCode() : 0);
        temp = Double.doubleToLongBits(purchasePriceEUR);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(totalCostEUR);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(totalCostUAH);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(raisedSalePrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(multiplier);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(transportCostEUR);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + deliveryDate.hashCode();
        temp = Double.doubleToLongBits(salePrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(raisedSalePrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(oldSalePrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (sold != null ? sold.hashCode() : 0);
        result = 31 * result + (sellType != null ? sellType.hashCode() : 0);
        result = 31 * result + (saleDate != null ? saleDate.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (refundDate != null ? refundDate.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (shop != null ? shop.hashCode() : 0);
        result = 31 * result + (spreadsheetNum != null ? spreadsheetNum.hashCode() : 0);
        result = 31 * result + (sellerName != null ? sellerName.hashCode() : 0);
        return result;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "ArticleJdo{" +
                "id=" + id +
                ", spreadsheetRow=" + spreadsheetRow +
                ", spreadsheetNum='" + spreadsheetNum + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", quantity=" + quantity +
                ", size='" + size + '\'' +
                ", deliveryDate=" + deliveryDate +
                ", purchasePriceEUR=" + purchasePriceEUR +
                ", transportCostEUR=" + transportCostEUR +
                ", totalCostEUR=" + totalCostEUR +
                ", totalCostUAH=" + totalCostUAH +
                ", multiplier=" + multiplier +
                ", calculatedSalePrice=" + calculatedSalePrice +
                ", salePrice=" + salePrice +
                ", raisedSalePrice=" + raisedSalePrice +
                ", oldSalePrice=" + oldSalePrice +
                ", sold='" + sold + '\'' +
                ", sellType='" + sellType + '\'' +
                ", saleDate=" + saleDate +
                ", refundDate=" + refundDate +
                ", comment='" + comment + '\'' +
                ", postponedOperationDate=" + postponedOperationDate +
                ", tags='" + tags + '\'' +
                ", shop='" + shop + '\'' +
                ", sellerName='" + sellerName + '\'' +
                '}';
    }
}
