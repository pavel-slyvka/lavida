package com.lavida.swing.groovy.http.client;


import com.lavida.swing.groovy.http.HttpRequest;
import com.lavida.swing.groovy.http.HttpResponse;
import com.lavida.swing.groovy.http.browser.BrowserType;

public interface HttpClient {

    HttpResponse sendRequest(HttpRequest httpRequest);

    HttpResponse sendRequest(HttpRequest httpRequest, BrowserType browserType);
}
