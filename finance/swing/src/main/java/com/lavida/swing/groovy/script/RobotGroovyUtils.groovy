package com.lavida.swing.groovy.script

/**
 * Groovy utils for the {@link com.lavida.swing.groovy.utils.Robot}.
 * Created: 24.10.13 14:21.
 * @author Ruslan.
 */
class RobotGroovyUtils {
    private static XmlSlurper slurper = new XmlSlurper(new org.ccil.cowan.tagsoup.Parser());

    static String getUrlTitle(String content) {
        def parser = slurper.parseText(content);
        return parser.'**'.find{tag -> tag.name() == 'title'}.text();
    }

    static String saveFilesFromNet(String content) {
        // todo saving all needed files (img, css, js ) to file system and changing their references to local paths in content

        return content;
    }

    static String getBaseLink(String content) {
        def parser = slurper.parseText(content);
        return  parser.'**'.find { tag -> tag.name() == 'base' }.@href.text();

    }

    static Object getByDivIdFromStart(String content, String divId) {
        def parser = slurper.parseText(content);
        return parser.'**'.find{tag -> tag.name() == 'div' || tag.@id == divId};
    }

    static GroovyObject getStartPosition(String content) {
        return slurper.parseText(content);
    }

    static GroovyObject gotoDivId(GroovyObject position, String id) {
        return position.'**'.find{tag -> tag.name() == 'div' || tag.@id == id};
    }

    static GroovyObject gotoUlId(GroovyObject position, String id) {
        return position.'**'.find{tag -> tag.name() == 'ul' || tag.@id == id};
    }

    static Integer getElementsCount(GroovyObject elementList) {
        int count;
        elementList.each { count ++}
        return count.intValue();
    }
}
