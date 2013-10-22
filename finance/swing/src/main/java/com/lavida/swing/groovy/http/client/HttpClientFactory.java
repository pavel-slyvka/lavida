package com.lavida.swing.groovy.http.client;

public interface HttpClientFactory {

    HttpClient getHttpClientByEnum(HttpClientEnum httpClientEnum);
}
