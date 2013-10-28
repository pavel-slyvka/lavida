package com.lavida.swing.groovy.utils;

import com.lavida.service.ProductService;
import com.lavida.service.entity.ProductJdo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Binder for {@link com.lavida.swing.groovy.utils.Robot}
 * Created: 28.10.13 16:15.
 *
 * @author Ruslan
 */
public class Binder {
    private static final Logger logger = LoggerFactory.getLogger(Binder.class);
    private static final String DATE_PATTERN = "dd.MM.yyyy HH:mm:ss";

    private ApplicationContext context = new ClassPathXmlApplicationContext("spring-swing.xml");
    private ProductService productService = context.getBean(ProductService.class);

    private SimpleDateFormat dateFormat;
    private Map<String, String> classesQueriesMap;

    public Binder() {
        dateFormat = new SimpleDateFormat(DATE_PATTERN);
        classesQueriesMap = new HashMap<>();
        classesQueriesMap.put(ProductJdo.class.getCanonicalName(), "productJdo.sql");
    }

    public void handle(List products) {
        Properties properties = new Properties();
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream("binder.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        boolean enableFilesCopying = new Boolean(properties.getProperty("enableFilesCopying"));
        boolean enableFlatten = new Boolean(properties.getProperty("enableFlatten"));
        boolean enableDatabaseQuerying = new Boolean(properties.getProperty("enableDatabaseQuerying"));
        String destinationPrefix = properties.getProperty("destinationPrefix");
        String imagePrefix = properties.getProperty("imagePrefix");
        int startImageNumber = Integer.valueOf(properties.getProperty("startImageNumber"));
        String dbUrl = properties.getProperty("db.url");
        String dbUser = properties.getProperty("db.user");
        String dbPassword = properties.getProperty("db.password");

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        for (int i = 0; i < products.size(); ++i) {
            executeQueries(products.get(i), classesQueriesMap.get(products.get(i).getClass().getCanonicalName()), connection);
        }


    }

    private void executeQueries(Object product, String queriesFilePath, Connection connection) {
        try {
            List<String> queries = getStringListFromFile(queriesFilePath);
            PreparedStatement statement;
            for (String query : queries) {
                statement = connection.prepareStatement(query);
                ParameterMetaData parameterMetaData = statement.getParameterMetaData();
                int paramCount = parameterMetaData.getParameterCount();
                String[] paramNames = new String[paramCount];
                for (int i = 0; i < paramCount; ++i) {
                    paramNames[i] = parameterMetaData.getParameterTypeName(i);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

    }

    public List<String> getStringListFromFile(String filePath) throws IOException {
        List<String> stringList = new ArrayList<>();
        File fileToRead = new File(filePath);
        BufferedReader br = new BufferedReader(new FileReader(fileToRead));
        String line;
        while ((line = br.readLine()) != null) {
            stringList.add(line);
        }
        br.close();
        return stringList;
    }

}
