package com.lavida.swing.groovy.script

import com.lavida.service.entity.ProductJdo
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
        robot.getPage("http://lavida.biz.ua/hm");
        def host = robot.getBaseLink();

        def brand = robot.gotoDivId('content').h1.text();
        robot.setPosition(robot.getPosition().div.table);
        (robot.getElementsCount(robot.getPosition().tr) / 2).times {
            def product1 = robot.addEntity(new ProductJdo());
            def product2 = robot.addEntity(new ProductJdo());
            product1.setImageSrcURL(robot.getPosition().tr[$it*2].td[0].div.img.@src.text());
            product2.setImageSrcURL(robot.getPosition().tr[$it*2].td[1].div.img.@src.text());
            product1.setName(robot.getPosition().tr[$it*2+1].td[0].div[0].text());
            product1.setCode(robot.getPosition().tr[$it*2+1].td[0].div[1].text());
            product2.setName(robot.getPosition().tr[$it*2+1].td[1].div[0].text());
            product2.setCode(robot.getPosition().tr[$it*2+1].td[1].div[1].text());
        }
        robot.saveEntities();
    }
}