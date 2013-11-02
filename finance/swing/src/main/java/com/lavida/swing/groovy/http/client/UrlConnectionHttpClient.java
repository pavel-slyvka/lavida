package com.lavida.swing.groovy.http.client;


import com.lavida.swing.groovy.http.HttpRequest;
import com.lavida.swing.groovy.http.HttpResponse;
import com.lavida.swing.groovy.http.browser.BrowserType;
import com.lavida.swing.groovy.http.browser.factory.BrowserTypeFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class UrlConnectionHttpClient implements HttpClient {

    private BrowserTypeFactory browserTypeFactory;

    public void setBrowserTypeFactory(BrowserTypeFactory browserTypeFactory) {
        this.browserTypeFactory = browserTypeFactory;
    }

    public HttpResponse sendRequest(HttpRequest httpRequest) {
        return sendRequest(httpRequest, browserTypeFactory.getBrowserType());
    }

    public HttpResponse sendRequest(HttpRequest httpRequest, BrowserType browserType) {
        HttpResponse httpResponse = new HttpResponse();
        URLConnection urlConnection ;
        URL url;
        boolean gotContent = false;
        while(!gotContent) {
            try {
                url = new URL(httpRequest.getUrl());
                urlConnection = url.openConnection();
                setHeaders(urlConnection, browserType);
                httpResponse.setContent(getContent(urlConnection));
                gotContent = true;
            } catch (Exception e) {
            }
        }
        return httpResponse;
    }

    private void setHeaders(URLConnection urlConnection, BrowserType browserType) {
        Map<String, String> headersMap = browserType.getHeadersMap();
        for(Map.Entry<String, String> entry: headersMap.entrySet()){
            urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    private String getContent(URLConnection urlConnection) throws IOException {
//        urlConnection.setUseCaches(false);
        InputStream is = (InputStream)urlConnection.getContent();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line ;
        StringBuffer sb = new StringBuffer();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

}
