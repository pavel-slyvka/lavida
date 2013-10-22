package com.lavida.swing.groovy

import com.lavida.swing.groovy.http.HttpRequest
import com.lavida.swing.groovy.http.RequestMethod
import com.lavida.swing.groovy.http.browser.BrowserChrome
import com.lavida.swing.groovy.http.client.UrlConnectionHttpClient

/**
 * Created: 17.10.13 12:24.
 * @author Ruslan.
 */
class Robot {
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

    static void main(args) {

//        def content = getPage("http://lavida.biz.ua/mango");
//        new File("D:/lavida/").mkdirs();
//        saveToFile("D:/lavida/mango.htm", content);
//        downloadImage("http://lavida.biz.ua/img/logo.png", "D:/lavida/img/");

        def content = getFromFile("D:/lavida/mango.htm");
        def tagsoupParser = new org.ccil.cowan.tagsoup.Parser()
        def slurper = new XmlSlurper(tagsoupParser)
        def parser = slurper.parseText(content);
//        content
        def brand = parser.'**'.find { div -> div.@id == 'content' }.h1.text();
        println(brand)
        def host = parser.'**'.find {base -> base.name() == 'base'}.@href.text();

        def tableNode = parser.'**'.find { div -> div.@id == 'content' }.div.table;
        def goodsImageList = tableNode.'**'.findAll{td -> td.@class == 'goods_img_td'}.div.
                collect{d -> d.'**'.find{it.name() == 'img' }?.@src }.findAll();
//        println(goodsImageList);
        def goodsNameList = tableNode.'**'.findAll(){td -> td.name() == 'td'}.
                collect{d -> d.'**'.find{it.@class == 'goods_name'}?.text()}.findAll();
//        println(goodsNameList);
        def goodsCodeList = tableNode.'**'.findAll(){td -> td.name() == 'td'}.
                collect(){d -> d.'**'.find{it.@class == 'goods_code'}?.text()}.findAll();
        println(goodsCodeList)

    }


}


