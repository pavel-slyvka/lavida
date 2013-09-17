package com.lavida.service.xml;

import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.entity.DiscountCardJdo;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created: 13:53 16.09.13
 *
 * @author Ruslan
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "postponedType", propOrder = {
        "articles", "discountCards"
})
@XmlRootElement
public class PostponedType {
    @XmlElement
    private List<ArticleJdo> articles;

    @XmlElement
    private List<DiscountCardJdo> discountCards;

    public List<ArticleJdo> getArticles() {
        return articles;
    }

    public void setArticles(List<ArticleJdo> articles) {
        this.articles = articles;
    }

    public List<DiscountCardJdo> getDiscountCards() {
        return discountCards;
    }

    public void setDiscountCards(List<DiscountCardJdo> discountCards) {
        this.discountCards = discountCards;
    }
}
