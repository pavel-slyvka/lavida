package com.lavida.service.entity;

import com.lavida.service.FilterColumn;
import com.lavida.service.FilterType;
import com.lavida.service.ViewColumn;

import javax.persistence.*;
import java.util.Calendar;

/** The entity for discount cards.
 * Created: 8:27 06.09.13
 *
 * @author Ruslan
 */
@Entity
@Table(name = "discounts")
@NamedQueries( {
        @NamedQuery(name = DiscountCardJdo.FIND_BY_NUMBER, query = "select d from DiscountCardJdo d where d.number = :number ")
        , @NamedQuery(name = DiscountCardJdo.FIND_ALL, query = "select d from DiscountCardJdo d ")
})
public class DiscountCardJdo implements Cloneable{
    public static final String FIND_BY_NUMBER = "DiscountCardJdo.findByNumber";
    public static final String FIND_ALL = "DiscountCardJdo.findAll";

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int id;

    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.number")
    @FilterColumn(labelKey = "dialog.discounts.card.all.label.search.by.number", type = FilterType.NUMBER)
    private int number;

    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.name", columnWidth = 200)
    private String name;

    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.phone", columnWidth = 150)
    private String phone;

    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.address")
    private String address;

    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.eMail")
    private String eMail;

    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.sumTotalUAH")
    @FilterColumn(labelKey = "dialog.discounts.card.all.label.search.by.sumTotalUAH", type = FilterType.NUMBER_DIAPASON)
    private double sumTotalUAH;

    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.discountRate")
    @FilterColumn(labelKey = "dialog.discounts.card.all.label.search.by.discountRate", type = FilterType.NUMBER)
    private double discountRate;

    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.registrationDate")
    private Calendar registrationDate;

    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.disablingDate")
    private Calendar disablingDate;

    public DiscountCardJdo() {
    }

    public DiscountCardJdo(int number, String name, String phone, String address, String eMail, double sumTotalUAH, double discountRate, Calendar registrationDate, Calendar disablingDate) {
        this.number = number;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.eMail = eMail;
        this.sumTotalUAH = sumTotalUAH;
        this.discountRate = discountRate;
        this.registrationDate = registrationDate;
        this.disablingDate = disablingDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public double getSumTotalUAH() {
        return sumTotalUAH;
    }

    public void setSumTotalUAH(double sumTotalUAH) {
        this.sumTotalUAH = sumTotalUAH;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public Calendar getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Calendar registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Calendar getDisablingDate() {
        return disablingDate;
    }

    public void setDisablingDate(Calendar disablingDate) {
        this.disablingDate = disablingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiscountCardJdo that = (DiscountCardJdo) o;

        if (Double.compare(that.discountRate, discountRate) != 0) return false;
        if (id != that.id) return false;
        if (number != that.number) return false;
        if (Double.compare(that.sumTotalUAH, sumTotalUAH) != 0) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (disablingDate != null ? !disablingDate.equals(that.disablingDate) : that.disablingDate != null)
            return false;
        if (eMail != null ? !eMail.equals(that.eMail) : that.eMail != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (registrationDate != null ? !registrationDate.equals(that.registrationDate) : that.registrationDate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + number;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (eMail != null ? eMail.hashCode() : 0);
        temp = Double.doubleToLongBits(sumTotalUAH);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(discountRate);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (registrationDate != null ? registrationDate.hashCode() : 0);
        result = 31 * result + (disablingDate != null ? disablingDate.hashCode() : 0);
        return result;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "DiscountCardJdo{" +
                "id=" + id +
                ", number=" + number +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", eMail='" + eMail + '\'' +
                ", sumTotalUAH=" + sumTotalUAH +
                ", discountRate=" + discountRate +
                ", registrationDate=" + registrationDate +
                ", disablingDate=" + disablingDate +
                '}';
    }
}
