package com.lavida.swing.groovy.http.client;



import com.lavida.swing.groovy.http.HttpRequest;
import com.lavida.swing.groovy.http.HttpResponse;
import com.lavida.swing.groovy.http.browser.BrowserType;
import com.lavida.swing.groovy.http.browser.factory.BrowserTypeFactory;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;
import java.util.Map;

public class ApacheHttpClient implements HttpClient {

    private BrowserTypeFactory browserTypeFactory;

    public void setBrowserTypeFactory(BrowserTypeFactory browserTypeFactory) {
        this.browserTypeFactory = browserTypeFactory;
    }

    public HttpResponse sendRequest(HttpRequest httpRequest) {
        return sendRequest(httpRequest, browserTypeFactory.getBrowserType());
    }

    public HttpResponse sendRequest(HttpRequest httpRequest, BrowserType browserType) {
        org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
        HttpMethod method = getHttpMethod(httpRequest);
        setHeaders(method, browserType);
        HttpResponse httpResponse = new HttpResponse();

        try {
//            method.getParams().setContentCharset("utf-8");
            httpClient.executeMethod(method);
            httpResponse.setContent(method.getResponseBodyAsString());
            httpResponse.setStatusCode(method.getStatusCode());
            httpResponse.setResponsePhrase(method.getStatusText());

        } catch (IOException e) {
            e.printStackTrace();  //todo
        } finally {
            method.releaseConnection();
        }
        return httpResponse;
    }

    private void setHeaders(HttpMethod method, BrowserType browserType) {
        Map<String, String> headersMap = browserType.getHeadersMap();
        for(Map.Entry<String, String> entry: headersMap.entrySet()){
            method.addRequestHeader(entry.getKey(), entry.getValue());
        }
    }

    private HttpMethod getHttpMethod(HttpRequest httpRequest) {
        HttpMethod method = null;
        switch (httpRequest.getRequestMethod()) {
            case GET:
                method = new GetMethod(httpRequest.getUrl());
                break;
            case POST:
                method = new PostMethod(httpRequest.getUrl());
                break;
        }
        return method;
    }
}
