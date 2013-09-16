package com.lavida.service.entity;

import com.lavida.service.FilterColumn;
import com.lavida.service.FilterType;
import com.lavida.service.ViewColumn;
import com.lavida.service.remote.SpreadsheetColumn;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Calendar;
import java.util.Date;

/**
 * The entity for discount cards.
 * Created: 8:27 06.09.13
 *
 * @author Ruslan
 */
@Entity
@Table(name = "discounts")
@NamedQueries({
        @NamedQuery(name = DiscountCardJdo.FIND_BY_NUMBER, query = "select d from DiscountCardJdo d where d.number = :number ")
        , @NamedQuery(name = DiscountCardJdo.FIND_ALL, query = "select d from DiscountCardJdo d ")
})
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "discountCardType", namespace = "http://www.xml.lavida.com/schema/discountCards.com")
public class DiscountCardJdo implements Cloneable {
    public static final String FIND_BY_NUMBER = "DiscountCardJdo.findByNumber";
    public static final String FIND_ALL = "DiscountCardJdo.findAll";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlElement
    private int id;

    @XmlElement(required = true)
    private int spreadsheetRow;

    @SpreadsheetColumn(column = "number")
    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.number", columnWidth = 50)
    @FilterColumn(labelKey = "dialog.discounts.card.all.label.search.by.number", type = FilterType.PART_TEXT,
            orderForAllDiscountCards = 1)
    @XmlElement
    private String number;

    @SpreadsheetColumn(column = "name")
    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.name", columnWidth = 300)
    @XmlElement
    private String name;

    @SpreadsheetColumn(column = "phone")
    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.phone", columnWidth = 150)
    @XmlElement
    private String phone;

    @SpreadsheetColumn(column = "address")
    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.address", columnWidth = 300)
    @XmlElement
    private String address;

    @SpreadsheetColumn(column = "eMail")
    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.eMail", columnWidth = 150)
    @XmlElement
    private String eMail;

    @SpreadsheetColumn(column = "sumTotalUAH")
    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.sumTotalUAH", columnWidth = 100)
    @FilterColumn(labelKey = "dialog.discounts.card.all.label.search.by.sumTotalUAH", type = FilterType.NUMBER_MORE,
            orderForAllDiscountCards = 2)
    @XmlElement
    private double sumTotalUAH;

    @SpreadsheetColumn(column = "discountRate")
    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.discountRate", columnWidth = 100)
    @FilterColumn(labelKey = "dialog.discounts.card.all.label.search.by.discountRate", type = FilterType.NUMBER_MORE,
            orderForAllDiscountCards = 3)
    @XmlElement
    private double discountRate;

    @SpreadsheetColumn(column = "bonusUAH")
    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.bonusUAH", columnWidth = 100)
    @XmlElement
    private double bonusUAH;

    @SpreadsheetColumn(column = "registrationDate")
    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.registrationDate", columnWidth = 100)
    @XmlElement
    private Calendar registrationDate;

    @SpreadsheetColumn(column = "activationDate")
    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.activationDate", columnWidth = 100)
    @FilterColumn(type = FilterType.CHECKBOXES, orderForAllDiscountCards = 4, checkBoxesNumber = 2,
            checkBoxesText = {"dialog.discounts.card.all.checkBox.show.not.active", "dialog.discounts.card.all.checkBox.show.active"},
            checkBoxesAction = {"dialog.discounts.card.all.checkBox.show.not.active.actionCommand", "dialog.discounts.card.all.checkBox.show.active.actionCommand"})
    @XmlElement
    private Calendar activationDate;

    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.postponedDate", datePattern = "dd.MM.yyyy HH:mm:ss"
            , columnWidth = 150)
    @XmlElement
    private Date postponedDate;

    public DiscountCardJdo() {
    }

    public DiscountCardJdo(int spreadsheetRow, String number, String name, String phone, String address,
                           String eMail, double sumTotalUAH, double discountRate, double bonusUAH,
                           Calendar registrationDate, Calendar activationDate, Date postponedDate) {
        this.spreadsheetRow = spreadsheetRow;
        this.number = number;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.eMail = eMail;
        this.sumTotalUAH = sumTotalUAH;
        this.discountRate = discountRate;
        this.bonusUAH = bonusUAH;
        this.registrationDate = registrationDate;
        this.activationDate = activationDate;
        this.postponedDate = postponedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
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

    public Calendar getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Calendar disablingDate) {
        this.activationDate = disablingDate;
    }

    public double getBonusUAH() {
        return bonusUAH;
    }

    public void setBonusUAH(double bonusUAH) {
        this.bonusUAH = bonusUAH;
    }

    public Date getPostponedDate() {
        return postponedDate;
    }

    public void setPostponedDate(Date postponedDate) {
        this.postponedDate = postponedDate;
    }

    public int getSpreadsheetRow() {
        return spreadsheetRow;
    }

    public void setSpreadsheetRow(int spreadsheetRow) {
        this.spreadsheetRow = spreadsheetRow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiscountCardJdo that = (DiscountCardJdo) o;

        if (Double.compare(that.bonusUAH, bonusUAH) != 0) return false;
        if (Double.compare(that.discountRate, discountRate) != 0) return false;
        if (id != that.id) return false;
        if (spreadsheetRow != that.spreadsheetRow) return false;
        if (Double.compare(that.sumTotalUAH, sumTotalUAH) != 0) return false;
        if (activationDate != null ? !activationDate.equals(that.activationDate) : that.activationDate != null)
            return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (eMail != null ? !eMail.equals(that.eMail) : that.eMail != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (number != null ? !number.equals(that.number) : that.number != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (postponedDate != null ? !postponedDate.equals(that.postponedDate) : that.postponedDate != null)
            return false;
        if (registrationDate != null ? !registrationDate.equals(that.registrationDate) : that.registrationDate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + spreadsheetRow;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (eMail != null ? eMail.hashCode() : 0);
        temp = Double.doubleToLongBits(sumTotalUAH);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(discountRate);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(bonusUAH);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (registrationDate != null ? registrationDate.hashCode() : 0);
        result = 31 * result + (activationDate != null ? activationDate.hashCode() : 0);
        result = 31 * result + (postponedDate != null ? postponedDate.hashCode() : 0);
        return result;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "DiscountCardJdo{" +
                "postponedDate=" + postponedDate +
                ", activationDate=" + activationDate +
                ", registrationDate=" + registrationDate +
                ", bonusUAH=" + bonusUAH +
                ", discountRate=" + discountRate +
                ", sumTotalUAH=" + sumTotalUAH +
                ", eMail='" + eMail + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", spreadsheetRow=" + spreadsheetRow +
                ", id=" + id +
                '}';
    }
}
