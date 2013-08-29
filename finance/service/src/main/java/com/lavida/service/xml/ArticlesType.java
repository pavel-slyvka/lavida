
package com.lavida.service.xml;

import com.lavida.service.entity.ArticleJdo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * Java class for articlesType complex type.
 *
 * @author Ruslan
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "articlesType", propOrder = {
    "articles"
})
@XmlRootElement
public class ArticlesType {

    protected List<ArticleJdo> articles;

    /**
     * Gets the value of the articles property.
     * Objects of the following type(s) are allowed in the list
     * {@link ArticleJdo }
     */
    public List<ArticleJdo> getArticles() {
        if (articles == null) {
            articles = new ArrayList<ArticleJdo>();
        }
        return this.articles;
    }

}
