package com.lavida.swing.groovy.http.browser.factory;


import com.lavida.swing.groovy.http.browser.BrowserType;
import com.lavida.swing.groovy.http.browser.BrowserTypeEnum;

public interface BrowserTypeFactory {

    BrowserType getBrowserType();

    BrowserType getBrowserTypeByEnum(BrowserTypeEnum browserTypeEnum);
}
