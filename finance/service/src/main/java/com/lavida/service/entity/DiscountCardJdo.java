package com.lavida.service.entity;

import com.lavida.service.FilterColumn;
import com.lavida.service.FilterType;
import com.lavida.service.ViewColumn;
import com.lavida.service.remote.SpreadsheetColumn;
import com.lavida.utils.ReflectionUtils;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    @SpreadsheetColumn(column = "city")
    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.city", columnWidth = 120)
    @FilterColumn(labelKey = "dialog.discounts.card.all.label.search.by.city", type = FilterType.PART_TEXT,
            orderForAllDiscountCards = 2)
    @XmlElement
    private String city;

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
            orderForAllDiscountCards = 3)
    @XmlElement
    private double sumTotalUAH;

    @SpreadsheetColumn(column = "discountRate")
    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.discountRate", columnWidth = 100)
    @FilterColumn(labelKey = "dialog.discounts.card.all.label.search.by.discountRate", type = FilterType.NUMBER_MORE,
            orderForAllDiscountCards = 5)
    @XmlElement
    private double discountRate;

    @SpreadsheetColumn(column = "bonusUAH")
    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.bonusUAH", columnWidth = 100)
    @FilterColumn(labelKey = "dialog.discounts.card.all.label.search.by.bonus", type = FilterType.NUMBER_MORE,
            orderForAllDiscountCards = 4)
    @XmlElement
    private double bonusUAH;

    @SpreadsheetColumn(column = "registrationDate")
    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.registrationDate", columnWidth = 100)
    @XmlElement
    private Calendar registrationDate;

    @SpreadsheetColumn(column = "activationDate")
    @ViewColumn(titleKey = "dialog.discounts.card.all.column.title.activationDate", columnWidth = 100)
    @FilterColumn(type = FilterType.CHECKBOXES, orderForAllDiscountCards = 6, checkBoxesNumber = 2,
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

    public DiscountCardJdo(int spreadsheetRow, String number, String name, String phone, String city, String address,
                           String eMail, double sumTotalUAH, double discountRate, double bonusUAH,
                           Calendar registrationDate, Calendar activationDate, Date postponedDate) {
        this.spreadsheetRow = spreadsheetRow;
        this.number = number;
        this.name = name;
        this.phone = phone;
        this.city = city;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public List<ChangedFieldJdo> findUpdateChanges(DiscountCardJdo oldCard, ChangedFieldJdo.RefreshOperationType refreshOperationType) {
        List<ChangedFieldJdo> changedFieldJdoList = new ArrayList<>();
        ChangedFieldJdo.RefreshOperationType operationType = refreshOperationType;
        ChangedFieldJdo.ObjectType objectType = ChangedFieldJdo.ObjectType.DISCOUNT_CARD;
        Date operationDate = new Date();
        ChangedFieldJdo changedFieldJdo;
        if (number != null && !number.isEmpty() ? !number.equals(oldCard.number) : oldCard.number != null && !oldCard.number.isEmpty()) {
            changedFieldJdo = new ChangedFieldJdo(operationDate, objectType, id, number, null, "number", oldCard.number,
                    number, operationType, null);
            changedFieldJdoList.add(changedFieldJdo);
        }
        if (name != null && !name.isEmpty() ? !name.equals(oldCard.name) : oldCard.name != null && !oldCard.name.isEmpty()) {
            changedFieldJdo = new ChangedFieldJdo(operationDate, objectType, id, number, null, "name", oldCard.name,
                    name, operationType, null);
            changedFieldJdoList.add(changedFieldJdo);
        }
        if (phone != null && !phone.isEmpty() ? !phone.equals(oldCard.phone) : oldCard.phone != null && !oldCard.phone.isEmpty()) {
            changedFieldJdo = new ChangedFieldJdo(operationDate, objectType, id, number, null, "phone", oldCard.phone,
                    phone, operationType, null);
            changedFieldJdoList.add(changedFieldJdo);
        }
        if (city != null && !city.isEmpty() ? !city.equals(oldCard.city) : oldCard.city != null && !oldCard.city.isEmpty()) {
            changedFieldJdo = new ChangedFieldJdo(operationDate, objectType, id, number, null, "city", oldCard.city,
                    city, operationType, null);
            changedFieldJdoList.add(changedFieldJdo);
        }
        if (address != null && !address.isEmpty() ? !address.equals(oldCard.address) : oldCard.address != null && !oldCard.address.isEmpty()) {
            changedFieldJdo = new ChangedFieldJdo(operationDate, objectType, id, number, null, "address", oldCard.address,
                    address, operationType, null);
            changedFieldJdoList.add(changedFieldJdo);
        }
        if (eMail != null && !eMail.isEmpty() ? !eMail.equals(oldCard.eMail) : oldCard.eMail != null && !oldCard.eMail.isEmpty()) {
            changedFieldJdo = new ChangedFieldJdo(operationDate, objectType, id, number, null, "eMail", oldCard.eMail,
                    eMail, operationType, null);
            changedFieldJdoList.add(changedFieldJdo);
        }
        if (Double.compare(oldCard.sumTotalUAH, sumTotalUAH) != 0) {
            changedFieldJdo = new ChangedFieldJdo(operationDate, objectType, id, number, null, "sumTotalUAH", String.valueOf(oldCard.sumTotalUAH),
                    String.valueOf(sumTotalUAH), operationType, null);
            changedFieldJdoList.add(changedFieldJdo);
        }
        if (Double.compare(oldCard.discountRate, discountRate) != 0) {
            changedFieldJdo = new ChangedFieldJdo(operationDate, objectType, id, number, null, "discountRate",
                    String.valueOf(oldCard.discountRate), String.valueOf(discountRate), operationType, null);
            changedFieldJdoList.add(changedFieldJdo);
        }
        if (Double.compare(oldCard.bonusUAH, bonusUAH) != 0) {
            changedFieldJdo = new ChangedFieldJdo(operationDate, objectType, id, number, null, "bonusUAH",
                    String.valueOf(oldCard.bonusUAH), String.valueOf(bonusUAH), operationType, null);
            changedFieldJdoList.add(changedFieldJdo);
        }
        if (registrationDate != null ? !registrationDate.equals(oldCard.registrationDate) : oldCard.registrationDate != null) {
            String pattern = ReflectionUtils.getFieldAnnotation(DiscountCardJdo.class, "registrationDate", SpreadsheetColumn.class).datePattern();
            DateFormat formatter = new SimpleDateFormat(pattern);
            changedFieldJdo = new ChangedFieldJdo(operationDate, objectType, id, number, null, "registrationDate",
                    oldCard.registrationDate != null ? formatter.format(oldCard.registrationDate.getTime()) : null,
                    registrationDate != null ?  formatter.format(registrationDate.getTime()) : null, operationType, null);
            changedFieldJdoList.add(changedFieldJdo);
        }
        if (activationDate != null ? !activationDate.equals(oldCard.activationDate) : oldCard.activationDate != null) {
            String pattern = ReflectionUtils.getFieldAnnotation(DiscountCardJdo.class, "activationDate", SpreadsheetColumn.class).datePattern();
            DateFormat formatter = new SimpleDateFormat(pattern);
            changedFieldJdo = new ChangedFieldJdo(operationDate, objectType, id, number, null, "activationDate",
                    oldCard.activationDate != null ? formatter.format(oldCard.activationDate.getTime()) : null,
                    activationDate != null ?  formatter.format(activationDate.getTime()) : null, operationType, null);
            changedFieldJdoList.add(changedFieldJdo);
        }

        return changedFieldJdoList;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiscountCardJdo that = (DiscountCardJdo) o;

        if (Double.compare(that.bonusUAH, bonusUAH) != 0) return false;
        if (Double.compare(that.discountRate, discountRate) != 0) return false;
        if (spreadsheetRow != that.spreadsheetRow) return false;
        if (Double.compare(that.sumTotalUAH, sumTotalUAH) != 0) return false;
        if (activationDate != null ? !activationDate.equals(that.activationDate) : that.activationDate != null)
            return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (eMail != null ? !eMail.equals(that.eMail) : that.eMail != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (number != null ? !number.equals(that.number) : that.number != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (registrationDate != null ? !registrationDate.equals(that.registrationDate) : that.registrationDate != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = spreadsheetRow;
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
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
        return result;
    }

    @Override
    public String toString() {
        return "DiscountCardJdo{" +
                "id=" + id +
                ", spreadsheetRow='" + spreadsheetRow + '\'' +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", eMail='" + eMail + '\'' +
                ", sumTotalUAH=" + sumTotalUAH +
                ", discountRate=" + discountRate +
                ", bonusUAH=" + bonusUAH +
                ", registrationDate=" + registrationDate +
                ", activationDate=" + activationDate +
                ", postponedDate=" + postponedDate +
                '}';
    }
}
