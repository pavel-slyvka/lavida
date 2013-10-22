package com.lavida.swing.groovy.http.client;


import com.lavida.swing.groovy.http.HttpRequest;
import com.lavida.swing.groovy.http.HttpResponse;
import com.lavida.swing.groovy.http.browser.BrowserType;
import com.lavida.swing.groovy.http.browser.factory.BrowserTypeFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestFullHttpClient implements HttpClient {

    private BrowserTypeFactory browserTypeFactory;

    public void setBrowserTypeFactory(BrowserTypeFactory browserTypeFactory) {
        this.browserTypeFactory = browserTypeFactory;
    }

    public HttpResponse sendRequest(HttpRequest httpRequest) {
        return sendRequest(httpRequest, browserTypeFactory.getBrowserType());
    }

    public HttpResponse sendRequest(HttpRequest httpRequest, BrowserType browserType) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> result = getResult(httpRequest, restTemplate, browserType);
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setContent(result.getBody());
        httpResponse.setStatusCode(result.getStatusCode().value());
        httpResponse.setResponsePhrase(result.getStatusCode().getReasonPhrase());
        return httpResponse;
    }

    private ResponseEntity<String> getResult(HttpRequest httpRequest, RestTemplate restTemplate, BrowserType browserType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(browserType.getHeadersMap());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> result = null;
        switch (httpRequest.getRequestMethod()) {
            case GET:
                result = restTemplate.exchange(httpRequest.getUrl(), HttpMethod.GET, entity, String.class);
                break;
            case POST:
                result = restTemplate.exchange(httpRequest.getUrl(), HttpMethod.POST, entity, String.class);
        }
        return result;
    }

}
