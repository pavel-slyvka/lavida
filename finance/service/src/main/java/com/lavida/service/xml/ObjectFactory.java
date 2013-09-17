package com.lavida.service.xml;

import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.entity.DiscountCardJdo;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the main.java.jax package. 
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 *
 * @author Ruslan
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName ARTICLES_QNAME = new QName("http://www.xml.lavida.com/schema/articles.com", "articles");
    private final static QName ARTICLE_QNAME = new QName("http://www.xml.lavida.com/schema/articles.com", "article");
    private final static QName DISCOUNT_CARDS_QNAME = new QName("http://www.xml.lavida.com/schema/discountCards.com", "discountCards");
    private final static QName DISCOUNT_CARD_QNAME = new QName("http://www.xml.lavida.com/schema/discountCards.com", "discountCard");
    private final static QName POSTPONED_TYPE_QNAME = new QName("http://www.xml.lavida.com/schema/postponed.com", "postponedType");

    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link com.lavida.service.entity.ArticleJdo }
     * 
     */
    public ArticleJdo createArticleJdo() {
        return new ArticleJdo();
    }

    /**
     * Create an instance of {@link ArticlesType }
     * 
     */
    public ArticlesType createArticlesType() {
        return new ArticlesType();
    }

    public DiscountCardJdo createDiscountCardJdo() {
        return new DiscountCardJdo();
    }

    public DiscountCardsType createDiscountCardsType() {
        return new DiscountCardsType();
    }

    public PostponedType createPostponedType () {
        return new PostponedType();
    }
    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link ArticlesType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.xml.lavida.com/schema/articles.com", name = "articles")
    public JAXBElement<ArticlesType> createArticles(ArticlesType value) {
        return new JAXBElement<ArticlesType>(ARTICLES_QNAME, ArticlesType.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link ArticleJdo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xml.lavida.com/schema/articles.com", name = "article")
    public JAXBElement<ArticleJdo> createArticle(ArticleJdo value) {
        return new JAXBElement<ArticleJdo>(ARTICLE_QNAME, ArticleJdo.class, null, value);
    }

    @XmlElementDecl(namespace = "http://www.xml.lavida.com/schema/discountCards.com", name = "discountCards")
    public JAXBElement<DiscountCardsType> createDiscountCards(DiscountCardsType value) {
        return new JAXBElement<>(DISCOUNT_CARDS_QNAME, DiscountCardsType.class, value);
    }

    @XmlElementDecl(namespace = "http://www.xml.lavida.com/schema/discountCards.com", name = "discountCard")
    public JAXBElement<DiscountCardJdo> createDiscountCard(DiscountCardJdo value) {
        return new JAXBElement<>(DISCOUNT_CARD_QNAME, DiscountCardJdo.class, value);
    }

    @XmlElementDecl(namespace = "http://www.xml.lavida.com/schema/postponed.com", name = "postponedType")
    public JAXBElement<PostponedType> createPostponed(PostponedType value) {
        return new JAXBElement<>(POSTPONED_TYPE_QNAME, PostponedType.class, value);
    }
}
