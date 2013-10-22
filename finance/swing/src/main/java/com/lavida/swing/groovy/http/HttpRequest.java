package com.lavida.swing.groovy.http;

public class HttpRequest {
    private RequestMethod requestMethod;
    private String url;
    private String content;

    public HttpRequest() {
    }

    public HttpRequest(RequestMethod requestMethod, String url, String content) {
        this.requestMethod = requestMethod;
        this.url = url;
        this.content = content;
    }

    public RequestMethod getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpRequest that = (HttpRequest) o;

        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (requestMethod != that.requestMethod) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = requestMethod != null ? requestMethod.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "requestMethod=" + requestMethod +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
