package com.lavida.service.xml;

import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.entity.DiscountCardJdo;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created: 13:38 16.09.13
 *
 * @author Ruslan
 */
@Service
public class DiscountCardsXmlService {
    /**
     * Marshals the List {@code <}{@link com.lavida.service.entity.DiscountCardJdo}{@code >} to the xml file
     *
     * @param discountCardJdoList the List {@code <}{@link com.lavida.service.entity.DiscountCardJdo}{@code >} to be marshaled
     * @param filePath    the xml file receiving the DiscountCardJdo
     * @throws javax.xml.bind.JAXBException if an error was encountered while creating the
     *                       <tt>JAXBContext</tt>, such as (but not limited to):
     *                       <ol>
     *                       <li>No JAXB implementation was discovered
     *                       <li>Classes use JAXB annotations incorrectly
     *                       <li>Classes have colliding annotations (i.e., two classes with the same type name)
     *                       <li>The JAXB implementation was unable to locate
     *                       provider-specific out-of-band information (such as additional
     *                       files generated at the development time.)
     *                       </ol>
     * @throws java.io.IOException   if an I/O error occurs.
     */
    public void marshal(List<DiscountCardJdo> discountCardJdoList, String filePath) throws JAXBException, IOException {
        DiscountCardsType discountCardsType = new DiscountCardsType();
        List<DiscountCardJdo> discountCards = discountCardsType.getDiscountCards();
        discountCards.addAll(discountCardJdoList);
        OutputStream outputStream = new FileOutputStream(filePath);
        JAXBContext context = JAXBContext.newInstance(DiscountCardsType.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(discountCardsType, outputStream);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * Unmarshals the xml file to the List {@code <}{@link com.lavida.service.entity.DiscountCardJdo}{@code >}.
     *
     * @param filePath the xml file holding the DiscountCardJdo.
     * @return the List {@code <}{@link com.lavida.service.entity.DiscountCardJdo}{@code >} .
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
     * @throws java.io.FileNotFoundException if the file does not exist,
     *                               is a directory rather than a regular file,
     *                               or for some other reason cannot be opened for
     *                               reading.
     */
    public List<DiscountCardJdo> unmarshal(String filePath) throws JAXBException, FileNotFoundException {
        List<DiscountCardJdo> discountCardJdoList = new ArrayList<>();
        InputStream inputStream = new FileInputStream(new File(filePath));
        JAXBContext context = JAXBContext.newInstance(DiscountCardsType.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        DiscountCardsType discountCardsType = (DiscountCardsType) unmarshaller.unmarshal(inputStream);
        discountCardJdoList.addAll(discountCardsType.getDiscountCards());
        return discountCardJdoList;
    }



}
