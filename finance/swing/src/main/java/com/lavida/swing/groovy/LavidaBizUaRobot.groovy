package com.lavida.swing.groovy

import com.lavida.service.entity.ProductJdo
import com.lavida.swing.groovy.http.HttpRequest
import com.lavida.swing.groovy.http.RequestMethod
import com.lavida.swing.groovy.http.browser.BrowserChrome
import com.lavida.swing.groovy.http.client.UrlConnectionHttpClient
import groovy.xml.StreamingMarkupBuilder

import java.util.regex.Pattern

/**
 * Created: 17.10.13 12:24.
 * @author Ruslan.
 */
class LavidaBizUaRobot {
    public static final String CODE_RU = "Код";

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
        def tagsoupParser = new org.ccil.cowan.tagsoup.Parser()
        def slurper = new XmlSlurper(tagsoupParser)
        def parser = slurper.parseText(content);
        def host = parser.'**'.find { base -> base.name() == 'base' };

        def styleSheetList = parser.'**'.findAll { link -> link.@rel == 'stylesheet' }.collect { d ->
            d.'**'.
                    find { it.name() == 'link' }?.@href.text()
        }.findAll();
        styleSheetList.each {
            sheet ->
                def sheetFile = new File(sheet);
                downloadFile(host.@href.text() + sheet, file.getParent() + "/" + sheetFile.getParent())
        }

        def scriptNodeList = parser.'**'.findAll { script -> script.name() == "script" }.collect() { d ->
            d.'**'.find { !(it.@src.text()).isEmpty() }
        }.findAll();
        scriptNodeList.each { scriptNode ->
            def scriptSrc = scriptNode.@src.text();
            if ((scriptSrc).startsWith("http:")){
                def newScriptSrc = downloadFile(scriptSrc, file.getParent() + "/js");
                String oldScriptTag = "<script type=\"text/javascript\" src=\"" + scriptSrc + "\">";
                String newScriptTag = "<script type=\"text/javascript\" src=\"" + newScriptSrc + "\">";
                if (content.contains(oldScriptTag)) {
                    content = content.replaceFirst(oldScriptTag, newScriptTag);
                }

            }else {
                def scriptFile = new File(scriptSrc);
                downloadFile(host.@href.text() + scriptSrc, file.getParent() + "/" + scriptFile.getParent())

            }
        }

        def javascriptList = parser.'**'.findAll { script -> script.name() == "script" }.collect { d ->
            d.'**'.find { it.@src != null }?.@src.text()
        }.findAll();
        javascriptList.each { script ->
            if (script.toString().startsWith("http:")) {
                downloadFile(script, file.getParent() + "/js");
            } else {
                def scriptFile = new File(script);
                downloadFile(host.@href.text() + script, file.getParent() + "/" + scriptFile.getParent())
            }
        }


        String baseOld = "<" + host.name() + " href=\"" + host.@href.text() + "\"/>";
//        parser.head.base.@href = ".";
        host.@href = ".";
        String baseNew = "<" + host.name() + " href=\"" + host.@href.text() + "\"/>";
        if (content.contains(baseOld)) {
            content = content.replaceFirst(baseOld, baseNew);
        }

        def fileWriter = new FileWriter(file);
        fileWriter.write(content);
        fileWriter.close();

    }

    static String downloadFile(def address, String directory) {
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = "${address.tokenize('/')[-1]}";
        def file;
        if (fileName.contains('?')) {
            int end = fileName.indexOf("?");
            fileName = fileName.substring(0, end);
            file = new File(directory, fileName);
        } else {
            file = new File(directory, fileName);
        }
        file.withOutputStream { out ->
            new URL(address).withInputStream { from -> out << from; }
        }
//        return ((File)file).getPath().replaceFirst(directory,"");
        return fileName;
    }

    static List<ProductJdo> getProductsFromFile(String filePath) {
        def content = getFromFile(filePath);
        def tagsoupParser = new org.ccil.cowan.tagsoup.Parser()
        def slurper = new XmlSlurper(tagsoupParser)
        def parser = slurper.parseText(content);
//        content
        def brand = parser.'**'.find { div -> div.@id == 'content' }.h1.text();
        def host = parser.'**'.find { base -> base.name() == 'base' }.@href.text();

        def li = parser.'**'.find{tag -> tag.name() == 'ul' || tag.@id == 'menu_content'}.li;
        int count=0;
        li[2].ul.li.each{
            count++
        }
        def liT = li[2].ul.li[0].a.text();
        def li2 = li[2].ul.'**'.findAll{tag -> tag.name() == 'li'}.collect{tag -> tag.'**'.find{it.name() == 'li'}}.findAll();

        def tableNode = parser.'**'.find { div -> div.@id == 'content' }.div.table;
        def goodsImageList = tableNode.'**'.findAll { td -> td.@class == 'goods_img_td' }.div.
                collect { d -> d.'**'.find { it.name() == 'img' }?.@src.text() }.findAll();
        def goodsNameList = tableNode.'**'.findAll() { td -> td.name() == 'td' }.
                collect { d -> d.'**'.find { it.@class == 'goods_name' }?.text() }.findAll();

        def goodsCodeList = tableNode.'**'.findAll() { td -> td.name() == 'td' }.
                collect() { d -> d.'**'.find { it.@class == 'goods_code' }?.text() }.findAll();
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
        String[] parts = url.split("/");
        def directory = "D:/lavida/";
        println(loadProducts(url, directory));
    }

}



