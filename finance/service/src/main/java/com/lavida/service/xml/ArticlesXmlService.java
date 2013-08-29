package com.lavida.service.xml;

import com.lavida.service.ArticleService;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.utils.CalendarConverter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created: 12:00 28.08.13
 * The ArticlesXmlService marshals the existing List {@code <}{@link com.lavida.service.entity.ArticleJdo}{@code >}
 * to the xml file and unmarshals the xml file to the List {@code <}{@link com.lavida.service.entity.ArticleJdo}{@code >}
 *
 * @author Ruslan
 */
@Service
public class ArticlesXmlService {
    /**
     * Marshals the List {@code <}{@link com.lavida.service.entity.ArticleJdo}{@code >} to the xml file
     *
     * @param articlesJdo the List {@code <}{@link com.lavida.service.entity.ArticleJdo}{@code >} to be marshaled
     * @param filePath    the xml file receiving the articlesJdo
     * @throws JAXBException if an error was encountered while creating the
     *                       <tt>JAXBContext</tt>, such as (but not limited to):
     *                       <ol>
     *                       <li>No JAXB implementation was discovered
     *                       <li>Classes use JAXB annotations incorrectly
     *                       <li>Classes have colliding annotations (i.e., two classes with the same type name)
     *                       <li>The JAXB implementation was unable to locate
     *                       provider-specific out-of-band information (such as additional
     *                       files generated at the development time.)
     *                       </ol>
     * @throws IOException   if an I/O error occurs.
     */
    public void marshal(List<ArticleJdo> articlesJdo, String filePath) throws JAXBException, IOException {
        ArticlesType articlesType = new ArticlesType();
        List<ArticleJdo> articles = articlesType.getArticles();
        articles.addAll(articlesJdo);
        OutputStream outputStream = new FileOutputStream(filePath);
        JAXBContext context = JAXBContext.newInstance(ArticlesType.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(articlesType, outputStream);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * Unmarshals the xml file to the List {@code <}{@link com.lavida.service.entity.ArticleJdo}{@code >}.
     *
     * @param filePath the xml file holding the articlesJdo.
     * @return the List {@code <}{@link com.lavida.service.entity.ArticleJdo}{@code >} articlesJdo.
     * @throws JAXBException         if an error was encountered while creating the
     *                               <tt>JAXBContext</tt>, such as (but not limited to):
     *                               <ol>
     *                               <li>No JAXB implementation was discovered
     *                               <li>Classes use JAXB annotations incorrectly
     *                               <li>Classes have colliding annotations (i.e., two classes with the same type name)
     *                               <li>The JAXB implementation was unable to locate
     *                               provider-specific out-of-band information (such as additional
     *                               files generated at the development time.)
     *                               </ol>
     * @throws FileNotFoundException if the file does not exist,
     *                               is a directory rather than a regular file,
     *                               or for some other reason cannot be opened for
     *                               reading.
     */
    public List<ArticleJdo> unmarshal(String filePath) throws JAXBException, FileNotFoundException {
        List<ArticleJdo> articlesJdo = new ArrayList<ArticleJdo>();
        InputStream inputStream = new FileInputStream(new File(filePath));
        JAXBContext context = JAXBContext.newInstance(ArticlesType.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        ArticlesType articlesType = (ArticlesType) unmarshaller.unmarshal(inputStream);
        articlesJdo.addAll(articlesType.getArticles());
        return articlesJdo;
    }


}
