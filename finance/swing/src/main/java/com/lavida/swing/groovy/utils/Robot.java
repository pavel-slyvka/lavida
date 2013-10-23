package com.lavida.swing.groovy.utils;

import com.lavida.service.entity.ProductJdo;
import groovy.lang.Closure;

/**
 * Created with IntelliJ IDEA.
 * User: Pavel
 * Date: 23.10.13
 * Time: 12:57
 * To change this template use File | Settings | File Templates.
 */
public class Robot {

    public void getPage(String page) {
        // todo get page from file or from url (if file is absent)
    }

    public Object getByDivIdFromStart(String divId) {
        return null;  // todo
    }

    public String getBaseLink() {
        return null; // todo get base tag, return link from it (and cache it).
    }

    public Closure getGotoDivId() {
        return null;  // todo
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

    public ProductJdo addEntity(ProductJdo p) {
        return null;  // todo add entity to entity list, and return it
    }

    public Closure getSaveEntities() {
        return null;  // todo save entities to the DB using service
    }
}
