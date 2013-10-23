package com.lavida.swing.groovy.utils;

import com.lavida.service.entity.ProductJdo;
import com.lavida.swing.groovy.model.Url;
import groovy.lang.Closure;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 23.10.13
 * Time: 12:57
 * To change this template use File | Settings | File Templates.
 */
public class Robot {

    public String getPage(String page) {
        // todo get page from file or from url (if file is absent)
    }

    public Object getByDivIdFromStart(String divId) {
        return null;  // todo
    }

    public String getBaseLink() {
        return null; // todo get base tag, return link from it (and cache it).
    }

    public Closure gotoDivId(String id) {
        return null;  // todo move to tag div with id
    }

    public Closure gotoUlId(String id) {
        return null;  // todo move to tag ul with id
    }

    public Closure getGetPosition() {
        return null;  // todo return current position
    }

    public Closure getSetPosition() {
        return null;  // todo set new current position and return it
    }

    public Integer getElementsCount(Closure elementList) {
        return null;  // todo return elements count in the list
    }

    public ProductJdo addEntity(ProductJdo product) {
        return null;  // todo add entity to entity list, and return it
    }

    public Closure getSaveEntities() {
        return null;  // todo save entities to the DB using service
    }

    public Url addUrl(Url url) {
        return null;  // todo save url to url list
    }

    public List<Url> getUrlList() {
        return null;  // todo get url list
    }
}
