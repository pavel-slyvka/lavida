package com.lavida.swing.groovy;

import com.lavida.service.ProductService;
import com.lavida.service.UniversalProductService;
import com.lavida.service.entity.ProductJdo;
import com.lavida.service.entity.UniversalProductJdo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.io.*;
import java.lang.reflect.Field;
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
    private UniversalProductService universalProductService = context.getBean(UniversalProductService.class);

    private SimpleDateFormat dateFormat;
    private Map<String, String> classesQueriesMap;
    private boolean enableDatabaseQuerying;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private String dbName;
    private String sourceSql;
    private String mysqlPath;


    public Binder() {
        Properties properties = new Properties();
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream("binder.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        enableDatabaseQuerying = new Boolean(properties.getProperty("enableDatabaseQuerying"));
        dbUrl = properties.getProperty("db.url");
        dbUser = properties.getProperty("db.user");
        dbPassword = properties.getProperty("db.password");
        dbName = properties.getProperty("db.name");
        sourceSql = properties.getProperty("source.sql");
        mysqlPath = properties.getProperty("mysql.path");

        dateFormat = new SimpleDateFormat(DATE_PATTERN);
        classesQueriesMap = new HashMap<>();
        classesQueriesMap.put(ProductJdo.class.getCanonicalName(), "productJdo.sql");
    }

    public void handle(List<UniversalProductJdo> universalProductJdoList) {
        Set<List<UniversalProductJdo>> productFieldSet = transformProducts(universalProductJdoList);

        DriverManagerDataSource dataSource = new DriverManagerDataSource(dbUrl, dbUser, dbPassword);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
        List<UniversalProductJdo> universalProductsForUpdate = new ArrayList<>();
        for (List<UniversalProductJdo> fieldsList : productFieldSet) {
            if (enableDatabaseQuerying) {
                universalProductsForUpdate.addAll(executeQueries(fieldsList, template));
            }
        }

        if (universalProductsForUpdate.size() > 0) {
            for (UniversalProductJdo universalProductJdo : universalProductsForUpdate) {
                universalProductService.update(universalProductJdo);
            }
        }

    }

    private Set<List<UniversalProductJdo>> transformProducts(List<UniversalProductJdo> universalProductJdoList) {
        Set<List<UniversalProductJdo>> products = new HashSet<>();
        Set<Long> objectIdSet = new HashSet<>();
        for (UniversalProductJdo universalProductJdo : universalProductJdoList){
            objectIdSet.add(universalProductJdo.getObjectId());
        }
        for (Long objectId : objectIdSet) {
            List<UniversalProductJdo> fieldsList = new ArrayList<>();
            for (UniversalProductJdo universalProductJdo : universalProductJdoList) {
                if (objectId.longValue() == universalProductJdo.getObjectId() ) {
                    fieldsList.add(universalProductJdo);
                }
            }
            products.add(fieldsList);
        }

        return products;
    }

    private Object createProduct(List<UniversalProductJdo> fieldsList) {
        Class productClass = null;
        try {
            productClass = Class.forName(fieldsList.get(0).getClassName());
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        Object product = null;
        if (productClass != null){
            try {
                product = productClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error(e.getMessage(), e);
            }
        }
        if (product != null) {
            for (Field field : product.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                for (UniversalProductJdo universalProductJdo : fieldsList) {
                    if (field.getName().equals(universalProductJdo.getFieldName())) {
                        try {
                            setFieldValue(product, field, universalProductJdo);
                        } catch (IllegalAccessException e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
            }

        }

        return product;
    }

    private void setFieldValue(Object product, Field field, UniversalProductJdo universalProductJdo) throws IllegalAccessException {
        if (byte.class == field.getType()) {
            field.set(product, Byte.parseByte(universalProductJdo.getFieldValue()));
        } else if (short.class == field.getType()) {
            field.set(product, Short.parseShort(universalProductJdo.getFieldValue()));
        } else if (char.class == field.getType()) {
            field.set(product, universalProductJdo.getFieldValue().charAt(0));
        } else if (int.class == field.getType()) {
            field.set(product, Integer.parseInt(universalProductJdo.getFieldValue()));
        } else if (long.class == field.getType()) {
            field.set(product, Long.parseLong(universalProductJdo.getFieldValue()));
        } else  if (double.class == field.getType()) {
            field.set(product, Double.parseDouble(universalProductJdo.getFieldValue()));
        } else  if (float.class == field.getType()) {
            field.set(product, Float.parseFloat(universalProductJdo.getFieldValue()));
        } else if (Byte.class == field.getType()) {
            field.set(product, Byte.parseByte(universalProductJdo.getFieldValue()));
        } else if (Short.class == field.getType()) {
            field.set(product, Short.parseShort(universalProductJdo.getFieldValue()));
        } else if (Character.class == field.getType()) {
            field.set(product, universalProductJdo.getFieldValue().charAt(0));
        } else if (Integer.class == field.getType()) {
            field.set(product, Integer.parseInt(universalProductJdo.getFieldValue()));
        } else if (Long.class == field.getType()) {
            field.set(product, Long.parseLong(universalProductJdo.getFieldValue()));
        } else if (Double.class == field.getType()) {
            field.set(product, Double.parseDouble(universalProductJdo.getFieldValue()));
        } else if (Float.class == field.getType()) {
            field.set(product, Float.parseFloat(universalProductJdo.getFieldValue()));
        } else {
            field.set(product, universalProductJdo.getFieldValue());
        }

    }

    private List<UniversalProductJdo> executeQueries(List<UniversalProductJdo> fieldsList, NamedParameterJdbcTemplate template) {
        List<UniversalProductJdo> universalProductsForUpdate = new ArrayList<>();
        Object product = createProduct(fieldsList);
        String queriesFilePath = classesQueriesMap.get(product.getClass().getCanonicalName());

        try {
            List<String> queries = getStringListFromFile(queriesFilePath);
            MapSqlParameterSource parameterSource = new MapSqlParameterSource();
            for (Field field : product.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    parameterSource.addValue(field.getName(), field.get(product));
                } catch (IllegalAccessException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            for (String query : queries) {
                String fieldName = null;
                if (query.contains("#")) {
                    String[] queryParts = query.split("#");
                    fieldName = queryParts[0].trim();
                    query = queryParts[1].trim();
                }
                GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
                int res = template.update(query, parameterSource, keyHolder);
                if (res > 0) {
                    Number key = keyHolder.getKey();
                    if (fieldName != null) {
                        try {
                            Field field = product.getClass().getDeclaredField(fieldName);
                            field.setAccessible(true);
                            if (int.class == field.getType()) {
                                field.set(product, key.intValue());
                            } else if (byte.class == field.getType()) {
                                field.set(product, key.byteValue());
                            } else if (short.class == field.getType()) {
                                field.set(product, key.shortValue());
                            } else if (double.class == field.getType()) {
                                field.set(product, key.doubleValue());
                            } else if (long.class == field.getType()) {
                                field.set(product, key.longValue());
                            } else if (float.class == field.getType()) {
                                field.set(product, key.floatValue());
                            } else if (Integer.class == field.getType()) {
                                field.set(product, key.intValue());
                            } else if (Byte.class == field.getType()) {
                                field.set(product, key.byteValue());
                            } else if (Short.class == field.getType()) {
                                field.set(product, key.shortValue());
                            } else if (Double.class == field.getType()) {
                                field.set(product, key.doubleValue());
                            } else if (Long.class == field.getType()) {
                                field.set(product, key.longValue());
                            } else if (Float.class == field.getType()) {
                                field.set(product, key.floatValue());
                            }
                            parameterSource.addValue(field.getName(), field.get(product));

                            for (UniversalProductJdo universalProductJdo : fieldsList) {
                                if (fieldName.equals(universalProductJdo.getFieldName())){
                                    universalProductJdo.setFieldValue(String.valueOf(key));
                                    universalProductsForUpdate.add(universalProductJdo);
                                }
                            }

                        } catch (IllegalAccessException e) {
                            logger.error(e.getMessage(), e);
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return universalProductsForUpdate;
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

    public static void main(String[] args) {
        Binder binder = new Binder();
//        binder.clearDatabaseTable();
        int complete = -1;
        try {
           complete = binder.restoreUniversalPoductDatabaseTable();
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        if (complete == 0){
            List<UniversalProductJdo> universalProductJdoList = binder.getUniversalProducts();
            binder.handle(universalProductJdoList);
        } else {
            logger.info("Can't dump in source.sql!");
        }
    }

    private void clearDatabaseTable() {
        List<UniversalProductJdo> universalProductJdos = universalProductService.getAll();
        for (UniversalProductJdo universalProductJdo : universalProductJdos) {
            universalProductService.delete(universalProductJdo.getId());
        }
    }

    private  List<UniversalProductJdo> getUniversalProducts() {
        return universalProductService.getAll();
    }

    private int restoreUniversalPoductDatabaseTable() throws IOException, InterruptedException {
//        String executeCommand = "/" + mysqlPath + "/mysql -u"+ dbUser + " -p" + dbPassword + " " + dbName + " < " + sourceSql;
//        String executeCommand = "/" + mysqlPath + "/mysql -u" + dbUser + " -p" + dbPassword + " " + dbName + " -e " + sourceSql;
//        String executeCommand = "/" + mysqlPath + "/mysql -u" + dbUser + " -p" + dbPassword + " " + dbName + " < " + sourceSql;
        String[] ex = new String[]{ "/" + mysqlPath + "/mysql", "-u" + dbUser," -p" + dbPassword, dbName , "-e ", " source " + sourceSql};
    Process runtimeProcess = Runtime.getRuntime().exec(ex);
//    Process runtimeProcess = Runtime.getRuntime().exec(executeCommand);
        return runtimeProcess.waitFor();
    }


}
