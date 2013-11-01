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
    public static final String CODE_RU = "Код";

    static void main(args) {
        def robot = new Robot();
        robot.clearFileSystemCache();
        robot.clearUrlDataBase();
        robot.clearUniversalProductDataBase();
//        robot.setEnableDatabaseForProcessing(false);
        def startUrl = robot.getPage("http://lavida.biz.ua");
        def baseLink = robot.getBaseLink();
        robot.gotoUlId('menu_content');
        robot.setPosition(robot.getPosition().li[2].ul);
        robot.getElementsCount(robot.getPosition().li).times {
            def url = new Url();
            url.setTitle(robot.getPosition().li[it].a.text());
            url.setUrl(baseLink + robot.getPosition().li[it].a.@href.text());
            robot.addUrl(url);
        }
        startUrl.setProcessed(true);
        robot.updateUrl(startUrl);
        robot.getUrlList().each {
            handleUrl(it, robot.getBaseDir());
        }
        robot.workDone();

    }

    static void handleUrl(Url url, String baseDir) {
        if (!url.isProcessed()) {
            def robot = new Robot();
            robot.setBaseDir(baseDir);
//            robot.setEnableDatabaseForProcessing(false)
            url = robot.getPage(url.getUrl());

            robot.gotoDivId('content');
            def brand = robot.getH1Text();
            robot.setPosition(robot.getPosition().div.table);
            int rowsQuantity = robot.getElementsCount(robot.getPosition().tr);
            if (robot.tableContainsNamesAndCodes()) {
                if (rowsQuantity % 2 > 0) {
                    rowsQuantity +=1;
                }
                (rowsQuantity / 2).times {
                    def product1 = robot.addEntity(new ProductJdo());
                    product1.setProducerBrand(brand);
                    def product2 = robot.addEntity(new ProductJdo());
                    product2.setProducerBrand(brand);
                    product1.setImageSrcURL(robot.getPosition().tr[it * 2].td[0].div.img.@src.text());
                    product2.setImageSrcURL(robot.getPosition().tr[it * 2].td[1].div.img.@src.text());
                    product1.setName(robot.getPosition().tr[it * 2 + 1].td[0].div[0].text());
                    product1.setCode((robot.getPosition().tr[it * 2 + 1].td[0].div[1].text()).replaceFirst(CODE_RU, "").trim());
                    product2.setName(robot.getPosition().tr[it * 2 + 1].td[1].div[0].text());
                    product2.setCode((robot.getPosition().tr[it * 2 + 1].td[1].div[1].text()).replaceFirst(CODE_RU, "").trim());
                }

            } else {
                (rowsQuantity).times {
                    def product1 = robot.addEntity(new ProductJdo());
                    product1.setProducerBrand(brand);
                    def product2 = robot.addEntity(new ProductJdo());
                    product2.setProducerBrand(brand);
                    product1.setImageSrcURL(robot.getPosition().tr[it].td[0].div.img.@src.text());
                    product2.setImageSrcURL(robot.getPosition().tr[it].td[1].div.img.@src.text());
                    product1.setName("");
                    product1.setCode("");
                    product2.setName("");
                    product2.setCode("");
                }

            }
            robot.saveEntities();
            url.setProcessed(true);
            robot.updateUrl(url);
        }
    }
}