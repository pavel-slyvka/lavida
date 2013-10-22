package com.lavida.swing.groovy

import com.lavida.service.entity.ProductJdo
import com.lavida.swing.groovy.http.HttpRequest
import com.lavida.swing.groovy.http.RequestMethod
import com.lavida.swing.groovy.http.browser.BrowserChrome
import com.lavida.swing.groovy.http.client.UrlConnectionHttpClient

/**
 * Created: 17.10.13 12:24.
 * @author Ruslan.
 */
class LavidaBizUaRobot {
    public static  final String CODE_RU = "Код";

    static String getPage(url) {
        def httpClient = new UrlConnectionHttpClient();
        def browser = new BrowserChrome();
        def request = new HttpRequest(RequestMethod.GET, url, null);
        def response = httpClient.sendRequest(request, browser);
        return response.getContent();
    }

    static String getFromFile(String filePath) {
        def fileReader = new FileReader(filePath);
        def str = new StringBuilder();
        def ch;
        while ((ch = fileReader.read()) != -1) {
            str.append((char) ch);
        }
        return str.toString();
    }

    static void saveToFile(String filePath, String content) {
        def file = new File(filePath);
//        file.mkdirs();
        def fileWriter = new FileWriter(file);
        fileWriter.write(content);
        fileWriter.close();
    }

    static void downloadImage(def address, def directory) {
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        def file = new File(directory, "${address.tokenize('/')[-1]}");
        file.withOutputStream { out ->
            new URL(address).withInputStream { from -> out << from; }
        }
    }

    static List<ProductJdo> getProductsFromFile (String filePath) {
        def content = getFromFile(filePath);
        def tagsoupParser = new org.ccil.cowan.tagsoup.Parser()
        def slurper = new XmlSlurper(tagsoupParser)
        def parser = slurper.parseText(content);
//        content
        def brand = parser.'**'.find { div -> div.@id == 'content' }.h1.text();
        def host = parser.'**'.find {base -> base.name() == 'base'}.@href.text();
        def tableNode = parser.'**'.find { div -> div.@id == 'content' }.div.table;
        def goodsImageList = tableNode.'**'.findAll{td -> td.@class == 'goods_img_td'}.div.
                collect{d -> d.'**'.find{it.name() == 'img' }?.@src.text() }.findAll();
        def goodsNameList = tableNode.'**'.findAll(){td -> td.name() == 'td'}.
                collect{d -> d.'**'.find{it.@class == 'goods_name'}?.text()}.findAll();

        def goodsCodeList = tableNode.'**'.findAll(){td -> td.name() == 'td'}.
                collect(){d -> d.'**'.find{it.@class == 'goods_code'}?.text()}.findAll();
        def codeList = new ArrayList();
        for (String codeName : goodsCodeList) {
            String code = codeName.replaceFirst(CODE_RU, "").trim();
            codeList.add(code);
        }

        List<ProductJdo> productJdoList = new ArrayList<>();
        for (int i = 0; i < goodsImageList.size(); ++i) {
            ProductJdo productJdo = new ProductJdo(host, goodsImageList.get(i), brand, goodsNameList.get(i),
                    codeList.get(i));
            productJdoList.add(productJdo);
        }
        return productJdoList;

    }

    static List<ProductJdo> loadProducts(url, directory) {
        def file = new File(directory);
        if (!file.exists()) {
            file.mkdirs();
        }
        def fileName = "${url.tokenize('/')[-1]}".toString();
        def filePath = directory + fileName + ".htm"

        def content = getPage(url);
        saveToFile(filePath, content);
        return getProductsFromFile(filePath);

    }

    static void main(args) {
        def url = "http://lavida.biz.ua/hm";
        def directory = "D:/lavida/";
        println(loadProducts(url, directory));
   }


}


