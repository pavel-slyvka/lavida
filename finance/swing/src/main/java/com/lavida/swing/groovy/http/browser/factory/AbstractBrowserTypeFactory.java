package com.lavida.swing.groovy.http.browser.factory;

import com.lavida.swing.groovy.http.browser.*;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBrowserTypeFactory implements BrowserTypeFactory {

    protected Map<BrowserTypeEnum, BrowserType> browserTypeCache = new HashMap<>();
    private Map<BrowserTypeEnum, Class> browserTypeMapping = new HashMap<>();

    {
        browserTypeMapping.put(BrowserTypeEnum.IE, BrowserIE.class);
        browserTypeMapping.put(BrowserTypeEnum.CHROME, BrowserChrome.class);
        browserTypeMapping.put(BrowserTypeEnum.OPERA, BrowserOpera.class);
    }

    public BrowserType getBrowserTypeByEnum(BrowserTypeEnum browserTypeEnum) {
        if (!browserTypeCache.containsKey(browserTypeEnum)) {

            if (!browserTypeMapping.containsKey(browserTypeEnum)) {
                throw new RuntimeException("Browser Type mapping is absent.");
            }
            try {
                BrowserType browserType = (BrowserType) browserTypeMapping.get(browserTypeEnum).newInstance();
                browserTypeCache.put(browserTypeEnum, browserType);
            } catch (Exception e) {
                new RuntimeException(e);
            }
        }
        return browserTypeCache.get(browserTypeEnum);
    }

}
