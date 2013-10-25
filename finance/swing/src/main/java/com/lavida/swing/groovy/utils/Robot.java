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
import com.lavida.swing.groovy.script.RobotGroovyUtils;
import groovy.lang.Closure;
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
//    private List<Url> urlList = new ArrayList<>();

    public Url getPage(String pageUrl) {
        Url url = urlService.findByUrlString(pageUrl);
        String content;
        if (url == null) {
            url = new Url();
            url.setUrl(pageUrl);
            content =  getContentFromNet(url);
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
//        urlService.update(url);
        this.pageContent = content;
        this.position = RobotGroovyUtils.getStartPosition(content);
        return url;
    }

    public Object getByDivIdFromStart(String divId) {
        return RobotGroovyUtils.getByDivIdFromStart(this.pageContent, divId);
    }

    public String getBaseLink() {
        String link = RobotGroovyUtils.getBaseLink(pageContent);
        this.baseLink = link;
        return link;
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
        List<ProductJdo> databaseProducts = productService.getAll();
        for (ProductJdo productJdo : products) {
            if (!databaseProducts.contains(productJdo)) {
                productService.save(productJdo);
            }
        }
    }

    public void updateUrl (Url url) {
        urlService.update(url);
    }
    public Url addUrl(Url url) {
//        urlList.add(url);
        if (urlService.findByUrlString(url.getUrl()) == null) {
            urlService.save(url);
        }
        return urlService.findByUrlString(url.getUrl());
    }

    public List<Url> getUrlList() {
//        return urlList;
        return urlService.getAll();
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

        saveFilesFromNet(content);
        if (url.getTitle() == null) {
            String title = getUrlTitle(content);
            url.setTitle(title);
        }
        File file = new File(url.getTitle() + ".htm");
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
        return content;
    }

    private String getUrlTitle(String content) {
        return RobotGroovyUtils.getUrlTitle(content);
    }

    private String saveFilesFromNet(String content) {
        return RobotGroovyUtils.saveFilesFromNet(content);
    }

}
