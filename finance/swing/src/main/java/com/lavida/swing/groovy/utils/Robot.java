package com.lavida.swing.groovy.utils;

import com.lavida.service.ProductService;
import com.lavida.service.UrlService;
import com.lavida.service.entity.ProductJdo;
import com.lavida.service.entity.Url;
import com.lavida.swing.groovy.http.HttpRequest;
import com.lavida.swing.groovy.http.HttpResponse;
import com.lavida.swing.groovy.http.RequestMethod;
import com.lavida.swing.groovy.http.browser.BrowserChrome;
import com.lavida.swing.groovy.http.client.UrlConnectionHttpClient;
import com.lavida.swing.groovy.utils.RobotGroovyUtils;
import groovy.lang.GroovyObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 23.10.13
 * Time: 12:57
 * To change this template use File | Settings | File Templates.
 */
public class Robot {
    private static final Logger logger = LoggerFactory.getLogger(Robot.class);

    private ApplicationContext context = new ClassPathXmlApplicationContext("spring-swing.xml");
    private ProductService productService = context.getBean(ProductService.class);
    private UrlService urlService = context.getBean(UrlService.class);

    private String baseLink;
    private String pageContent;
    private GroovyObject position;
    private List<ProductJdo> products = new ArrayList<>();
    private boolean enableDatabaseForProcessing = true;
    private boolean enableDatabaseForData = true;
    private boolean enableContentSaving = true;
    private List<Url> urlList = new ArrayList<>();
    private String baseDir;
    private long minLatency;
    private long maxLatency;


    public Robot() {
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

    public void saveEntities() {
        if (enableDatabaseForData) {
            List<ProductJdo> databaseProducts = productService.getAll();
            for (ProductJdo productJdo : products) {
                if (!databaseProducts.contains(productJdo)) {
                    productService.save(productJdo);
                }
            }
        }
    }

    public void updateUrl(Url url) {
        if (enableDatabaseForProcessing) {
            urlService.update(url);
        }
    }

    public Url addUrl(Url url) {
        if (enableDatabaseForProcessing) {
            if (urlService.findByUrlString(url.getUrl()) == null) {
                urlService.save(url);
            }
            return urlService.findByUrlString(url.getUrl());
        } else {
            urlList.add(url);
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
        FileReader fileReader = new FileReader(url.getFilePath());
        StringBuilder str = new StringBuilder();
        int ch;
        while ((ch = fileReader.read()) != -1) {
            str.append((char) ch);
        }
        return str.toString();
    }

    private String getContentFromNet(Url url) {
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

    public void clearUrlDataBase() {
        List<Url> urlList1 = urlService.getAll();
        for (Url url : urlList1) {
            urlService.delete(url.getId());
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
}
