package com.lavida.swing.groovy;

import com.lavida.service.UniversalProductService;
import com.lavida.service.entity.ProductJdo;
import com.lavida.service.entity.UniversalProductJdo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.util.FileCopyUtils;

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
    private String sourcePrefix;
    private String destinationPrefix;
    private boolean interrupted;


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
        sourcePrefix = properties.getProperty("sourcePrefix");
        destinationPrefix = properties.getProperty("destinationPrefix");
        if (sourcePrefix.isEmpty() || destinationPrefix.isEmpty()) {
            interrupted = true;
            logger.info(dateFormat.format(new Date()) + " : empty source and/or destination prefixes, process is interrupted!");
        }

        dateFormat = new SimpleDateFormat(DATE_PATTERN);
        classesQueriesMap = new HashMap<>();
        classesQueriesMap.put(ProductJdo.class.getCanonicalName(), "productJdo.sql");
    }

    public void handle(List<UniversalProductJdo> universalProductJdoList) {
        if (interrupted) return;
        logger.info(dateFormat.format(new Date()) + " : moving images");
        universalProductJdoList = moveFilesToDestinationDirectory(universalProductJdoList);

        Set<List<UniversalProductJdo>> productFieldSet = transformProducts(universalProductJdoList);
        logger.info(dateFormat.format(new Date()) + " : inserting products " + productFieldSet.size() + " pcs.");

        DriverManagerDataSource dataSource = new DriverManagerDataSource(dbUrl, dbUser, dbPassword);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(dataSource);
//        List<UniversalProductJdo> universalProductsForUpdate = new ArrayList<>();
        for (List<UniversalProductJdo> fieldsList : productFieldSet) {
            if (enableDatabaseQuerying) {
                executeQueries(fieldsList, template);
//                universalProductsForUpdate.addAll(executeQueries(fieldsList, template));
            }
        }

//        if (universalProductsForUpdate.size() > 0) {
//            logger.info(dateFormat.format(new Date()) + " : updating universal products changes " + universalProductsForUpdate.size() + " pcs.");
//            universalProductService.update(universalProductsForUpdate);
//        }
        logger.info(dateFormat.format(new Date()) + " : finished binding.");

    }

    private List<UniversalProductJdo> moveFilesToDestinationDirectory(List<UniversalProductJdo> universalProductJdoList) {

        List<UniversalProductJdo> imageList = new ArrayList<>();
        for (UniversalProductJdo universalProductJdo : universalProductJdoList) {
            if ((universalProductJdo.getFieldName()).contains("imageSrc")) {
                imageList.add(universalProductJdo);
            }
        }

        int numericalName = 0;
        String folderName = null;
        File siteFile;
        File newDirectory = null;
        for (UniversalProductJdo universalProductJdo : imageList) {
            String srcUrl = universalProductJdo.getFieldValue();
            String[] dirParts = srcUrl.split("/");
            String sectionName = dirParts[1].trim() + "/";
            String imageFileName = dirParts[dirParts.length - 1].trim();
            String parentPrefix = srcUrl.replace(imageFileName, "");
            int extensionStartPoint = imageFileName.lastIndexOf(".");
            String extension = imageFileName.substring(extensionStartPoint);
            String imageName = imageFileName.substring(0, extensionStartPoint);
            File oldFile = new File(sourcePrefix + srcUrl);
            if (!oldFile.exists()) {
                logger.warn("There is no image for the universal product with id: " + universalProductJdo.getId() + " in source directory!");
                continue;
            }

            String newSrcUrl = parentPrefix + imageName + extension;
            siteFile = new File(destinationPrefix + newSrcUrl);

            int numberSuffix = 0;
            String namePrefix = null;
            if (!sectionName.equals(folderName)) {
                newDirectory = siteFile.getParentFile();
                newDirectory.mkdirs();
                folderName = sectionName;
                numericalName = getMaxNumericNameInFolder(siteFile.getParentFile(), extension);
            }
            if (isNumericalName(imageName)) {
                imageName = String.valueOf(++numericalName);
            }else {
                namePrefix = getNamePrefix(imageName);
                numberSuffix = getMaxNumberSuffixInFolder(siteFile, extension, namePrefix);
                imageName = namePrefix + String.valueOf(++numberSuffix);
            }

            while (folderContains(newDirectory, imageName, extension)) {
                if (isNumericalName(imageName)) {
                    imageName = String.valueOf(++numericalName);
                } else {
                    imageName = namePrefix + String.valueOf(++numberSuffix);
                }
            }
            newSrcUrl = parentPrefix + imageName + extension;
            siteFile = new File(destinationPrefix + newSrcUrl);
            if (oldFile.exists() && newDirectory.exists()) {
                try {
                    FileCopyUtils.copy(oldFile, siteFile);
                    universalProductJdo.setFieldValue(newSrcUrl);
                    universalProductService.update(universalProductJdo);
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }

        }
        return universalProductJdoList;
    }

    private boolean folderContains(File newDirectory, String imageName, String extension) {
        String[] fileNames = newDirectory.list();
        for (String fileName : fileNames) {
            if (fileName.endsWith(extension)) {
                fileName = fileName.replace(extension, "").trim();
                if (imageName.equals(fileName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getNamePrefix(String imageName) {
        char[] chars = imageName.toCharArray();
        for (int i = chars.length -1; i > -1; --i){
            if (Character.isDigit(chars[i])) {
                chars[i] = ' ';
            } else break;
        }
        String namePrefix = new String(chars);
        return namePrefix.trim();
    }

    private int getMaxNumberSuffixInFolder(File siteFile, String extension, String namePrefix) {
        int maxValue = 0;
        String[] fileNames = siteFile.getParentFile().list();
        for (String fileName : fileNames) {
            if (fileName.endsWith(extension)) {
                fileName = fileName.replace(extension, "");
                String numberSuffix = fileName.replaceFirst(namePrefix, "");
                if (isNumericalName(numberSuffix)) {
                    int number = Integer.parseInt(numberSuffix);
                    if (number > maxValue) {
                        maxValue = number;
                    }
                }
            }
        }
        return maxValue;
    }

    private int getMaxNumericNameInFolder(File parentFile, String extension) {
        int maxValue = 0;
        String[] fileNames = parentFile.list();
        for (String fileName : fileNames) {
            if (fileName.endsWith(extension)) {
                fileName = fileName.replace(extension, "");
                if (isNumericalName(fileName)) {
                   int number = Integer.parseInt(fileName);
                    if (number > maxValue) {
                        maxValue = number;
                    }
                }
            }
        }

        return maxValue;
    }

    private boolean isNumericalName(String imageName) {
        Integer number = null;
        try {
            number = Integer.parseInt(imageName);
        } catch (NumberFormatException e) {
        }
        return (number != null);
    }

    private Set<List<UniversalProductJdo>> transformProducts(List<UniversalProductJdo> universalProductJdoList) {
        Set<List<UniversalProductJdo>> products = new HashSet<>();
        Set<Long> objectIdSet = new HashSet<>();
        for (UniversalProductJdo universalProductJdo : universalProductJdoList) {
            objectIdSet.add(universalProductJdo.getObjectId());
        }
        for (Long objectId : objectIdSet) {
            List<UniversalProductJdo> fieldsList = new ArrayList<>();
            for (UniversalProductJdo universalProductJdo : universalProductJdoList) {
                if (objectId == universalProductJdo.getObjectId()) {
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
        if (productClass != null) {
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
                            product = setFieldValue(product, field, universalProductJdo);
                            break;
                        } catch (IllegalAccessException e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
            }

        }

        return product;
    }

    private Object setFieldValue(Object product, Field field, UniversalProductJdo universalProductJdo) throws IllegalAccessException {
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
        } else if (double.class == field.getType()) {
            field.set(product, Double.parseDouble(universalProductJdo.getFieldValue()));
        } else if (float.class == field.getType()) {
            field.set(product, Float.parseFloat(universalProductJdo.getFieldValue()));
        } else if (boolean.class == field.getType()) {
            field.set(product, Boolean.valueOf(universalProductJdo.getFieldValue()));
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
        } else if (Boolean.class == field.getType()) {
            field.set(product, Boolean.valueOf(universalProductJdo.getFieldValue()));
        } else {
            field.set(product, universalProductJdo.getFieldValue());
        }
        return product;
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
                int res;
                try {
                    res = template.update(query, parameterSource, keyHolder);
                } catch (DataAccessException e) {
                    logger.error(e.getMessage() + " in query " + query, e);
                    continue;
                }
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
                                if (fieldName.equals(universalProductJdo.getFieldName())) {
                                    universalProductJdo.setFieldValue(String.valueOf(key));
                                    universalProductService.update(universalProductJdo);
//                                    universalProductsForUpdate.add(universalProductJdo);
                                    break;
                                }
                            }
                        } catch (IllegalAccessException | NoSuchFieldException e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
            }

            for (UniversalProductJdo universalProductJdo : fieldsList) {
                if ("processed".equals(universalProductJdo.getFieldName())) {
                    universalProductJdo.setFieldValue("true");
                    universalProductService.update(universalProductJdo);
//                    universalProductsForUpdate.add(universalProductJdo);
                    break;
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return universalProductsForUpdate;
    }

    private List<String> getStringListFromFile(String filePath) throws IOException {
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
        if (binder.interrupted) return;
        binder.clearUniversalProductTable();
        int complete = -1;
        try {
            complete = binder.restoreUniversalPoductDatabaseTable();
        } catch (IOException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        if (complete == 0) {
            List<UniversalProductJdo> universalProductJdoList = binder.getUniversalProducts();
            binder.handle(universalProductJdoList);
        } else {
            logger.info("Can't dump in source.sql!");
        }
    }

    private void clearUniversalProductTable() {
        List<UniversalProductJdo> universalProductJdos = universalProductService.getAll();
        for (UniversalProductJdo universalProductJdo : universalProductJdos) {
            universalProductService.delete(universalProductJdo.getId());
        }
    }

    private List<UniversalProductJdo> getUniversalProducts() {
        return universalProductService.getAll();
    }

    private int restoreUniversalPoductDatabaseTable() throws IOException, InterruptedException {
        String executeCommand = "/" + mysqlPath + "/mysql -u " + dbUser + " -p" + dbPassword + " " + dbName + " -e" + "\" source " + sourceSql + "\"";
        Process runtimeProcess = Runtime.getRuntime().exec(executeCommand);
        return runtimeProcess.waitFor();
    }


}
