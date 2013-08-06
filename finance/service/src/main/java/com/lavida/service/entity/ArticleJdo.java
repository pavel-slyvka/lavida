package com.lavida.service.entity;

//import com.lavida.service.entity.enums.Brand;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Created: 8:15 05.08.13
 *
 * @author Ruslan
 */
@Entity
@Table(name = "goods")
public class ArticleJdo {
    @Id
    @GeneratedValue
    private int id;
    private String code;
    private String name;
    private String brand;
    private int quantity;
    private String size;
    private double purchasingPriceEUR;
    private double transportCostEUR;
    @Temporal(TemporalType.DATE)
    private Calendar deliveryDate;
    private double priceUAH;
    private double raisedPriceUAH;
    private double actionPriceUAH;
    private boolean soled;
    private String ours;
    @Temporal(TemporalType.DATE)
    private Calendar saleDate;
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

    public boolean isSoled() {
        return soled;
    }

    public void setSoled(boolean soled) {
        this.soled = soled;
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

        if (id != that.id) return false;
        if (brand != null ? !brand.equals(that.brand) : that.brand != null) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (!name.equals(that.name)) return false;
        if (size != null ? !size.equals(that.size) : that.size != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + name.hashCode();
        result = 31 * result + (brand != null ? brand.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
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
                ", deliveryDate=" + deliveryDate +
                ", priceUAH=" + priceUAH +
                ", raisedPriceUAH=" + raisedPriceUAH +
                ", actionPriceUAH=" + actionPriceUAH +
                ", soled=" + soled +
                ", ours='" + ours + '\'' +
                ", saleDate=" + saleDate +
                ", comment='" + comment + '\'' +
                '}';
    }
}
