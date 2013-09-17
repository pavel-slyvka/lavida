package com.lavida.service.xml;

import com.lavida.service.entity.DiscountCardJdo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created: 13:20 16.09.13
 *
 * @author Ruslan
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "discountCardsType", propOrder = {
        "discountCards"
})
@XmlRootElement
public class DiscountCardsType {

    protected List<DiscountCardJdo> discountCards;

    public List<DiscountCardJdo> getDiscountCards() {
        if (discountCards == null) {
            discountCards = new ArrayList<>();
        }
        return discountCards;
    }
}
