package com.lavida.swing.groovy.http;

public class HttpResponse {

    private int statusCode;
    private String responsePhrase;
    private String content;

    public HttpResponse() {
    }

    public HttpResponse(int statusCode, String responsePhrase, String content) {
        this.statusCode = statusCode;
        this.responsePhrase = responsePhrase;
        this.content = content;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getResponsePhrase() {
        return responsePhrase;
    }

    public void setResponsePhrase(String responsePhrase) {
        this.responsePhrase = responsePhrase;
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

        HttpResponse that = (HttpResponse) o;

        if (statusCode != that.statusCode) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (responsePhrase != null ? !responsePhrase.equals(that.responsePhrase) : that.responsePhrase != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = statusCode;
        result = 31 * result + (responsePhrase != null ? responsePhrase.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "statusCode=" + statusCode +
                ", responsePhrase='" + responsePhrase + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
