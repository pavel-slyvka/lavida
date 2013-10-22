package com.lavida.swing.groovy.http.browser.factory;


import com.lavida.swing.groovy.http.browser.BrowserType;
import com.lavida.swing.groovy.http.browser.BrowserTypeEnum;

public class OneBrowserTypeFactory extends AbstractBrowserTypeFactory {

    private BrowserTypeEnum browserTypeEnum;

    public OneBrowserTypeFactory(BrowserTypeEnum browserTypeEnum) {
        this.browserTypeEnum = browserTypeEnum;
    }

    public BrowserType getBrowserType() {
        return getBrowserTypeByEnum(browserTypeEnum);
    }

}
