package com.lavida.swing.groovy.http.client;

import java.util.HashMap;
import java.util.Map;

public class HttpClientFactoryImpl implements HttpClientFactory{

    private Map<HttpClientEnum, HttpClient> httpClientCache = new HashMap<>();
    private Map<HttpClientEnum, Class> httpClientMappings = new HashMap<>();
    {
        httpClientMappings.put(HttpClientEnum.APACHE, ApacheHttpClient.class);
        httpClientMappings.put(HttpClientEnum.RESTFULL, RestFullHttpClient.class);
        httpClientMappings.put(HttpClientEnum.URLCONNECTION, UrlConnectionHttpClient.class);
    }

    public HttpClient getHttpClientByEnum(HttpClientEnum httpClientEnum) {
        if (!httpClientCache.containsKey(httpClientEnum)){
            if (!httpClientMappings.containsKey(httpClientEnum)) {
                throw new RuntimeException("HttpClient mapping is absent.");
            }
            try {
                HttpClient httpClient = (HttpClient)httpClientMappings.get(httpClientEnum).newInstance();
                httpClientCache.put(httpClientEnum, httpClient);
            } catch (Exception e) {
                new RuntimeException(e);
            }
        }
        return httpClientCache.get(httpClientEnum);
    }

}
