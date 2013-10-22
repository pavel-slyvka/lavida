package com.lavida.swing.groovy.http.browser;

import java.util.HashMap;
import java.util.Map;

public class BrowserOpera implements BrowserType {

    private final String USER_AGENT = "Opera/9.80 (Windows NT 6.1; U; ru) Presto/2.10.229 Version/11.60";
    private final String ACCEPT = "text/html, application/xml;q=0.9, application/xhtml+xml, image/png, image/webp, image/jpeg, image/gif, image/x-xbitmap, */*;q=0.1";
    private final String ACCEPT_LANGUAGE = "en-US,en;q=0.9";
    private final String ACCEPT_ENCODING = "deflate";  //todo gzip?
    private final String CONNECTION = "Keep-Alive";
    private final String COOKIE = "";   //todo
    private final String  XOPERA_INFO = "ID=875, p=4, f=15, sw=1366, sh=728";
    private final String  XOPERA_ID = "X-Opera-ID: f9f2d7e6e176b38cb25a6cfab8414d4981141d8e41932c475175468f2a0e060a";
    private final String  XOPERA_HOST = "v07-15:12428";
    private final String  XOA = "1947 9500926be936d3006af2c44be9822ac195264b8d602685042c8d37595799cc44"; //todo
    private final String  XOB = "evenes";

    public Map<String, String> getHeadersMap() {
        Map<String, String> headersMap = new HashMap<String, String>();
        headersMap.put("User-Agent", USER_AGENT);
        headersMap.put("Accept", ACCEPT);
        headersMap.put("Accept-Language", ACCEPT_LANGUAGE);
        headersMap.put("Accept-Encoding", ACCEPT_ENCODING);
        headersMap.put("Connection", CONNECTION);
        headersMap.put("Cookie", COOKIE);
        headersMap.put("X-Opera-Info", XOPERA_INFO);
        headersMap.put("X-Opera-ID", XOPERA_ID);
        headersMap.put("X-Opera-Host", XOPERA_HOST);
        headersMap.put("X-OA", XOA);
        headersMap.put("X-OB", XOB);
        return headersMap;
    }
}
