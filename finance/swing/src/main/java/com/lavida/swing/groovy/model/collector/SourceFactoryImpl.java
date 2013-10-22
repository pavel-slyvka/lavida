package com.lavida.swing.groovy.model.collector;

public class SourceFactoryImpl implements SourceFactory {

    public Source createSource(Class clazz) {
        Source source = new Source();
        //todo add fields as map keys
        return source;
    }
}
