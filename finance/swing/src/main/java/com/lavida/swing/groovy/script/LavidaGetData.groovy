package com.lavida.swing.groovy.script

import com.lavida.service.entity.ProductJdo
import com.lavida.service.entity.Url
import com.lavida.swing.groovy.utils.Robot

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 23.10.13
 * Time: 12:47
 * To change this template use File | Settings | File Templates.
 */

class LavidaGetData {

    static void main(args) {
        def robot = new Robot();
        def urlStart = robot.getPage("http://lavida.biz.ua");
        if (urlStart.isProcessed()) {
            return;
        }
        def baseLink = robot.getBaseLink();
        robot.gotoUlId('menu_content');
        robot.setPosition(robot.getPosition().li[2].ul);
        robot.getElementsCount(robot.getPosition().li).times {
            def url = new Url();
            url.setTitle(robot.getPosition().li[it].a.text());
            url.setUrl(baseLink + robot.getPosition().li[it].a.@href.text());
            robot.addUrl(url);
        }
        urlStart.setProcessed(true);
        robot.updateUrl(urlStart);
        robot.getUrlList().each {
            handleUrl(it);
        }
    }

    static void handleUrl(Url url) {
        if (!url.isProcessed()) {
            def robot = new Robot();
            url = robot.getPage(url.getUrl());

            def brand = robot.gotoDivId('content').h1.text();
            String workingDir = System.getProperty("user.dir");
            String delimiter = System.getProperty("file.separator");
            workingDir = workingDir.replace(delimiter, "/") + "/";

            robot.setPosition(robot.getPosition().div.table);
            (robot.getElementsCount(robot.getPosition().tr) / 2).times {
                def product1 = robot.addEntity(new ProductJdo());
                product1.setProducerBrand(brand);
                product1.setHostURL(workingDir);
                def product2 = robot.addEntity(new ProductJdo());
                product2.setProducerBrand(brand);
                product2.setHostURL(workingDir);

                product1.setImageSrcURL(robot.getPosition().tr[it * 2].td[0].div.img.@src.text());
                product2.setImageSrcURL(robot.getPosition().tr[it * 2].td[1].div.img.@src.text());
                product1.setName(robot.getPosition().tr[it * 2 + 1].td[0].div[0].text());
                product1.setCode(robot.getPosition().tr[it * 2 + 1].td[0].div[1].text());
                product2.setName(robot.getPosition().tr[it * 2 + 1].td[1].div[0].text());
                product2.setCode(robot.getPosition().tr[it * 2 + 1].td[1].div[1].text());
            }
            robot.saveEntities();
            url.setProcessed(true);
            robot.updateUrl(url);
        }
    }
}