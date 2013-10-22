package com.lavida.swing.groovy.http.browser;

import java.util.HashMap;
import java.util.Map;

public class BrowserChrome implements BrowserType {

    private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.121 Safari/535.2";
    private final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
    private final String ACCEPT_LANGUAGE = "ru-RU,ru;q=0.8,en-US;q=0.6,en;q=0.4";
    private final String ACCEPT_ENCODING = "deflate,sdch";  //todo gzip?
    private final String ACCEPT_CHARSET = "windows-1251,utf-8;q=0.7,*;q=0.3";
    private final String CONNECTION = "keep-alive";
    private final String COOKIE = "";
    private final String CONTENT_TYPE = "text/html; charset=utf-8";
//    private final String CONTENT_TYPE = "text/html; charset=utf-8";
    //todo fill cookie

    public Map<String, String> getHeadersMap() {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("User-Agent", USER_AGENT);
        headersMap.put("Accept", ACCEPT);
        headersMap.put("Accept-Language", ACCEPT_LANGUAGE);
        headersMap.put("Accept-Encoding", ACCEPT_ENCODING);
        headersMap.put("Accept-Charset", ACCEPT_CHARSET);
        headersMap.put("Connection", CONNECTION);
        headersMap.put("Cookie", COOKIE);
        headersMap.put("Content-Type", CONTENT_TYPE);
        return headersMap;
    }

}
