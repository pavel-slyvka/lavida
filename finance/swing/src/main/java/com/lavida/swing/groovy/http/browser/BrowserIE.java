package com.lavida.swing.groovy.http.browser;

import java.util.HashMap;
import java.util.Map;

public class BrowserIE implements BrowserType {
    private final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1; Trident/4.0; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.1)";
    private final String ACCEPT = "*/*";
    private final String ACCEPT_LANGUAGE = "en-us";
    private final String ACCEPT_ENCODING = "deflate";      //todo gzip?
    private final String CONNECTION = "Keep-Alive";
    private final String COOKIE = ""; //todo

    public Map<String, String> getHeadersMap() {
        Map<String, String> headersMap = new HashMap<String, String>();
        headersMap.put("User-Agent", USER_AGENT);
        headersMap.put("Accept", ACCEPT);
        headersMap.put("Accept-Language", ACCEPT_LANGUAGE);
        headersMap.put("Accept-Encoding", ACCEPT_ENCODING);
        headersMap.put("Connection", CONNECTION);
        headersMap.put("Cookie", COOKIE);
        return headersMap;
    }
}
