package com.lavida.service.xml;

import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

/**
 * Created: 14:05 16.09.13
 *
 * @author Ruslan
 */
@Service
public class PostponedXmlService {
    /**
     * Marshals the {@link com.lavida.service.xml.PostponedType} to the xml file
     *
     * @param postponedType the postponedType to be marshaled
     * @param file    the xml file receiving the postponedType.
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
    public void marshal(PostponedType postponedType, File file) throws JAXBException, IOException {
        OutputStream outputStream = new FileOutputStream(file);
        JAXBContext context = JAXBContext.newInstance(PostponedType.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(postponedType, outputStream);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * Unmarshals the xml file to the {@link com.lavida.service.xml.PostponedType}.
     *
//     * @param filePath the xml file holding the PostponedType.
     * @param file the xml file holding the PostponedType.
     * @return the {@link com.lavida.service.xml.PostponedType}.
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
    public PostponedType unmarshal(File file) throws JAXBException, FileNotFoundException {
        InputStream inputStream = new FileInputStream(file);
        JAXBContext context = JAXBContext.newInstance(PostponedType.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return  (PostponedType) unmarshaller.unmarshal(inputStream);
    }

}
