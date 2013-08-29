package com.lavida.service.xml;

import com.lavida.service.entity.ArticleJdo;

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

    private final static QName _Articles_QNAME = new QName("http://www.xml.lavida.com/schema/articles.com", "articles");
    private final static QName _Article_QNAME = new QName("http://www.xml.lavida.com/schema/articles.com", "article");

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

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link ArticlesType }{@code >}}
     *
     */
    @XmlElementDecl(namespace = "http://www.xml.lavida.com/schema/articles.com", name = "articles")
    public JAXBElement<ArticlesType> createArticles(ArticlesType value) {
        return new JAXBElement<ArticlesType>(_Articles_QNAME, ArticlesType.class, null, value);
    }

    /**
     * Create an instance of {@link javax.xml.bind.JAXBElement }{@code <}{@link ArticleJdo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.xml.lavida.com/schema/articles.com", name = "article")
    public JAXBElement<ArticleJdo> createArticle(ArticleJdo value) {
        return new JAXBElement<ArticleJdo>(_Article_QNAME, ArticleJdo.class, null, value);
    }

}
