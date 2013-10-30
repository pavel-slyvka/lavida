package com.lavida.swing.groovy.utils;

import com.lavida.service.UniversalProductService;
import com.lavida.service.UrlService;
import com.lavida.service.entity.ProductJdo;
import com.lavida.service.entity.UniversalProductJdo;
import com.lavida.service.entity.Url;
import com.lavida.swing.groovy.Binder;
import com.lavida.swing.groovy.http.HttpRequest;
import com.lavida.swing.groovy.http.HttpResponse;
import com.lavida.swing.groovy.http.RequestMethod;
import com.lavida.swing.groovy.http.browser.BrowserChrome;
import com.lavida.swing.groovy.http.client.UrlConnectionHttpClient;
import groovy.lang.GroovyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 23.10.13
 * Time: 12:57
 * To change this template use File | Settings | File Templates.
 */
public class Robot {
    private static final Logger logger = LoggerFactory.getLogger(Robot.class);
    private static long objectId = 0;

    private ApplicationContext context = new ClassPathXmlApplicationContext("spring-swing.xml");
    private UrlService urlService = context.getBean(UrlService.class);
    private UniversalProductService universalProductService = context.getBean(UniversalProductService.class);

    private String baseLink;
    private String pageContent;
    private GroovyObject position;
    private List products = new ArrayList<>(); // not safe container
    private boolean enableDatabaseForProcessing;
    private boolean enableDatabaseForData;
    private boolean enableContentSaving;
    private boolean enableBinder;
    private boolean enableFilesCopying;
    private boolean enableFlatten;
    private boolean enableDatabaseQuerying;
    private String destinationPrefix;
    private String imagePrefix;
    private int startImageNumber;
    private String dbUrl;
    private String dbUser;
    private String dbPassword;
    private String dbName;
    private String mysqlPath;
    private String tableName;
    private String sourceTableSql;


    private List<Url> urlList = new ArrayList<>();
    private String baseDir;
    private long minLatency;
    private long maxLatency;
    private static final String DATE_PATTERN = "dd.MM.yyyy HH:mm:ss";
    private SimpleDateFormat dateFormat;

    public Robot() {
        Properties properties = new Properties();
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream("robot.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        enableDatabaseForProcessing = new Boolean(properties.getProperty("enableDatabaseForProcessing"));
        enableDatabaseForData = new Boolean(properties.getProperty("enableDatabaseForData"));
        enableContentSaving = new Boolean(properties.getProperty("enableContentSaving"));
        enableBinder = new Boolean(properties.getProperty("enableBinder"));

        enableFilesCopying = new Boolean(properties.getProperty("enableFilesCopying"));
        enableFlatten = new Boolean(properties.getProperty("enableFlatten"));
        enableDatabaseQuerying = new Boolean(properties.getProperty("enableDatabaseQuerying"));
        destinationPrefix = properties.getProperty("destinationPrefix");
        imagePrefix = properties.getProperty("imagePrefix");
        startImageNumber = Integer.valueOf(properties.getProperty("startImageNumber"));
        dbUrl = properties.getProperty("db.url");
        dbUser = properties.getProperty("db.user");
        dbPassword = properties.getProperty("db.password");
        dbName = properties.getProperty("db.name");
        mysqlPath = properties.getProperty("mysql.path");
        tableName = properties.getProperty("table.name");
        sourceTableSql = properties.getProperty("source.table.sql");


        dateFormat = new SimpleDateFormat(DATE_PATTERN);
        logger.info("Started at time: " + dateFormat.format(new Date()));
        File file = new File(System.getProperty("user.dir") + "/robotCache");
        if (!file.exists()) file.mkdirs();
        String delimiter = System.getProperty("file.separator");
        baseDir = file.getPath().replace(delimiter, "/") + "/";
    }

    public Url getPage(String pageUrl) {
        Url url = null;
        if (enableDatabaseForProcessing) {
            url = urlService.findByUrlString(pageUrl);
        }
        String content;
        if (url == null) {
            url = new Url();
            url.setUrl(pageUrl);
            content = getContentFromNet(url);
            addUrl(url);
            this.pageContent = content;
            this.position = RobotGroovyUtils.getStartPosition(content);
            return url;
        }
        urlList.add(url);
        if (url.getFilePath() != null) {
            try {
                content = getContentFromFile(url);
            } catch (IOException e) {
                logger.warn(e.getMessage(), e);
                content = getContentFromNet(url);
            }
        } else {
            content = getContentFromNet(url);
        }
        this.pageContent = content;
        this.position = RobotGroovyUtils.getStartPosition(content);
        return url;
    }

    public Object getByDivIdFromStart(String divId) {
        return RobotGroovyUtils.getByDivIdFromStart(this.pageContent, divId);
    }

    public String getBaseLink() {
//        String link = RobotGroovyUtils.getBaseLink(pageContent);
//        this.baseLink = link;
        return baseLink;
    }

    public GroovyObject gotoDivId(String id) {
        position = RobotGroovyUtils.gotoDivId(position, id);
        return position;
    }

    public GroovyObject gotoUlId(String id) {
        position = RobotGroovyUtils.gotoUlId(position, id);
        return position;
    }

    public GroovyObject getPosition() {
        return position;
    }

    public GroovyObject setPosition(GroovyObject newPosition) {
        position = newPosition;
        return position;
    }

    public Integer getElementsCount(GroovyObject elementList) {
        return RobotGroovyUtils.getElementsCount(elementList);
    }

    public ProductJdo addEntity(ProductJdo product) {
        products.add(product);
        return product;
    }

    public void workDone() {
        if (enableBinder) {
                logger.info(dateFormat.format(new Date()) + ": pass products to Binder.");
                Binder binder = new Binder();
                binder.handle(universalProductService.getAll());

        }else {
            logger.info(dateFormat.format(new Date()) + ": export universal_product table.");
            int complete = -1;
            try {
                complete = exportUniversalPoductDatabaseTable ();
            } catch (IOException | InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
            if (complete != 0) {
                logger.warn(dateFormat.format(new Date()) + ": can't dump out table.");
            }
        }
        logger.info(dateFormat.format(new Date()) + ": pass finished work.");

    }

    private int exportUniversalPoductDatabaseTable() throws IOException, InterruptedException {
        String exportTable = "/" + mysqlPath + "/mysqldump -u" + dbUser + " -p" + dbPassword + " " + dbName + " " + tableName + " -r" + sourceTableSql;
        Process runtimeProcess = Runtime.getRuntime().exec(exportTable);

        return runtimeProcess.waitFor();

    }

    private void saveProducts(List products) {
        List<UniversalProductJdo> universalProductList = new ArrayList<>();
        for (Object object: products) {
            String className = object.getClass().getCanonicalName();
            ++ objectId ;
            for (Field field : object.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    UniversalProductJdo universalProductJdo = new UniversalProductJdo(className, objectId, field.getName(), field.get(object).toString());
                    universalProductList.add(universalProductJdo);
                } catch (IllegalAccessException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        for (UniversalProductJdo universalProductJdo : universalProductList) {
            universalProductService.save(universalProductJdo);
        }
    }

    public void saveEntities() {
        if (enableDatabaseForData) {
            if (products.size() > 0) {
                logger.info(dateFormat.format(new Date()) + ": saving universal products.");
                saveProducts(products);
                logger.info(dateFormat.format(new Date()) + ": finished process for " + urlList.get(0).getUrl());
            }
        }
    }

    public void updateUrl(Url url) {
        if (enableDatabaseForProcessing) {
            urlService.update(url);
        }
    }

    public Url addUrl(Url url) {
        urlList.add(url);
        if (enableDatabaseForProcessing) {
            if (urlService.findByUrlString(url.getUrl()) == null) {
                urlService.save(url);
            }
            return urlService.findByUrlString(url.getUrl());
        } else {
            return url;
        }
    }

    public List<Url> getUrlList() {
        if (enableDatabaseForProcessing) {
            return urlService.getAll();
        } else {
            return urlList;
        }
    }


    private String getContentFromFile(Url url) throws IOException {
        logger.info(dateFormat.format(new Date()) + ": getting content from file " + url.getFilePath());
        FileReader fileReader = new FileReader(url.getFilePath());
        StringBuilder str = new StringBuilder();
        int ch;
        while ((ch = fileReader.read()) != -1) {
            str.append((char) ch);
        }
        return str.toString();
    }

    private String getContentFromNet(Url url) {
        logger.info(dateFormat.format(new Date()) + ": getting content from Net " + url.getUrl());
        UrlConnectionHttpClient httpClient = new UrlConnectionHttpClient();
        BrowserChrome browser = new BrowserChrome();
        HttpRequest request = new HttpRequest(RequestMethod.GET, url.getUrl(), null);
        HttpResponse response = httpClient.sendRequest(request, browser);
        String content = response.getContent();

        if (url.getTitle() == null) {
            String title = getUrlTitle(content);
            title = title.replace("|", ", ").replace(":", ", ");
            url.setTitle(title);
        }
        this.baseLink = RobotGroovyUtils.getBaseLink(content);

        if (enableContentSaving) {
            content = saveFilesFromNet(content); // may be changed

            File file = new File(baseDir + url.getTitle() + ".htm");
            if (!file.exists()) {
                FileWriter fileWriter;
                try {
                    fileWriter = new FileWriter(file);
                    fileWriter.write(content);
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
            url.setFilePath(file.getPath());
        }

        return content;
    }

    private String getUrlTitle(String content) {
        return RobotGroovyUtils.getUrlTitle(content);
    }

    private String saveFilesFromNet(String content) {
        return RobotGroovyUtils.saveFilesFromNet(content, baseDir);
    }

    public void clearUrlDataBase() {
        List<Url> urlList1 = urlService.getAll();
        for (Url url : urlList1) {
            urlService.delete(url.getId());
        }
    }

    public void clearUniversalProductDataBase() {
        List<UniversalProductJdo> universalProductJdoList = universalProductService.getAll();
        for (UniversalProductJdo universalProductJdo : universalProductJdoList) {
            universalProductService.delete(universalProductJdo.getId());
        }
    }

    public void clearFileSystemCache() throws IOException {
        File baseDirFile = new File(baseDir);
        String files[] = baseDirFile.list();
        for (String temp : files) {
            //construct the file structure
            File fileDelete = new File(baseDirFile, temp);
            //recursive delete
            delete(fileDelete);
        }
    }

    private static void delete(File file) throws IOException{
        if(file.isDirectory()){
            //directory is empty, then delete it
            if(file.list().length==0){
                file.delete();
            }else{
                //list all the directory contents
                String files[] = file.list();
                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);
                    //recursive delete
                    delete(fileDelete);
                }
                //check the directory again, if empty then delete it
                if(file.list().length==0){
                    file.delete();
                }
            }
        }else{
            //if file, then delete it
            file.delete();
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

    public void await () {
        // todo latency for awaiting
    }

    public boolean isEnableDatabaseForProcessing() {
        return enableDatabaseForProcessing;
    }

    public void setEnableDatabaseForProcessing(boolean enableDatabaseForProcessing) {
        this.enableDatabaseForProcessing = enableDatabaseForProcessing;
    }

    public boolean isEnableDatabaseForData() {
        return enableDatabaseForData;
    }

    public void setEnableDatabaseForData(boolean enableDatabaseForData) {
        this.enableDatabaseForData = enableDatabaseForData;
    }

    public boolean isEnableContentSaving() {
        return enableContentSaving;
    }

    public void setEnableContentSaving(boolean enableContentSaving) {
        this.enableContentSaving = enableContentSaving;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        File file = new File(baseDir);
        if (!file.exists()) file.mkdirs();
        String delimiter = System.getProperty("file.separator");
        this.baseDir = file.getPath().replace(delimiter, "/") + "/";
    }

    public long getMinLatency() {
        return minLatency;
    }

    public void setMinLatency(long minLatency) {
        this.minLatency = minLatency;
    }

    public long getMaxLatency() {
        return maxLatency;
    }

    public void setMaxLatency(long maxLatency) {
        this.maxLatency = maxLatency;
    }

    public boolean isEnableBinder() {
        return enableBinder;
    }

    public void setEnableBinder(boolean enableBinder) {
        this.enableBinder = enableBinder;
    }


    public static void main(String[] args) {
        Robot robot = new Robot();
        robot.workDone();
    }
}
