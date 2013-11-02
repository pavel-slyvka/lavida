package com.lavida.swing.groovy.utils

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

    static String saveFilesFromNet(String content, String baseDir) {
        def parser = slurper.parseText(content);
        def host = parser.'**'.find { base -> base.name() == 'base' };
        // download css-files
        def styleSheetList = parser.'**'.findAll { link -> link.@rel == 'stylesheet' }.collect { d ->
            d.'**'.
                    find { it.name() == 'link' }?.@href.text()
        }.findAll();
        styleSheetList.each {
            sheet ->
                if (((String)sheet).startsWith ("http:")) {
                    def newSheetPath = removeServerPartFromUrl(sheet);
                    newSheetPath = downloadFile(sheet, baseDir, newSheetPath);
                    if (content.contains(sheet)) {
                        content.replaceFirst(sheet, newSheetPath);
                    }



                }
                downloadFile(host.@href.text() + sheet, baseDir, sheet)
        }
        // download js-files
        def scriptNodeList = parser.'**'.findAll { script -> script.name() == "script" }.collect() { d ->
            d.'**'.find { !(it.@src.text()).isEmpty() }
        }.findAll();
        scriptNodeList.each { scriptNode ->
            def scriptSrc = scriptNode.@src.text();
            if ((scriptSrc).startsWith("http:")){
                def newScriptSrc = removeServerPartFromUrl(scriptSrc);
                newScriptSrc = downloadFile(scriptSrc, baseDir , newScriptSrc);
                String oldScriptTag = scriptSrc ;
                String newScriptTag = newScriptSrc;
                if (content.contains(oldScriptTag)) {
                    content = content.replaceFirst(oldScriptTag, newScriptTag);
                }

            }else {
                downloadFile(host.@href.text() + scriptSrc, baseDir, scriptSrc);

            }
        }

        // download img-files
        def imgNodeList = parser.'**'.findAll{tag -> tag.name() == 'img'}.collect{ d ->
            d.'**'.find{!(it.@src.text().isEmpty())}
        }.findAll();
        imgNodeList.each{imgNode ->
            def imgPath = imgNode.@src.text();
            if(imgPath.startsWith("http:")) {
                def newImgPath = removeServerPartFromUrl(imgPath);
                newImgPath = downloadFile(imgPath, baseDir, newImgPath);
                def oldImgTag = imgPath ;
                def newImgTag = newImgPath ;
                if (content.contains(oldImgTag)) {
                    content = content.replaceFirst(oldImgTag, newImgTag);
                }
            }else {
                downloadFile(host.@href.text() + imgPath, baseDir, imgPath);
            }
        }

        String baseOld = "<base" + " href=\"" + host.@href.text() + "\"/>";
        String baseNew = "<base" + " href=\"" + baseDir + "\"/>";
        if (content.contains(baseOld)) {
            content = content.replaceFirst(baseOld, baseNew);
        }

        return content;
    }

    static String removeServerPartFromUrl(String urlString) {
        String[] parts = urlString.split("/");
        StringBuilder builder = new StringBuilder();
        for (int i = 3; i < parts.length; ++i) {
            builder.append(parts[i] + "/");
        }
        String fixedUrl = new String(builder);
        return fixedUrl.substring(0, fixedUrl.length() - 1);
    }

    static String downloadFile(def address, String baseDir, String filePath) {
        String directory = (new File(baseDir + filePath)).getParent();
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = "${filePath.tokenize('/')[-1]}";
        def file;
        if (fileName.contains('?')) {
            int end = fileName.indexOf("?");
            fileName = fileName.substring(0, end);
            file = new File(directory, fileName);
        } else {
            file = new File(directory, fileName);
        }
        if(! file.exists()) {
            file.withOutputStream { out ->
                new URL(address).withInputStream { from -> out << from; }
            }
        }
        return ((File)file).getPath().replaceFirst(baseDir,"");
    }

    static String getBaseLink(String content) {
        def parser = slurper.parseText(content);
        return  parser.'**'.find { tag -> tag.name() == 'base' }.@href.text();

    }

    static Object getByDivIdFromStart(String content, String divId) {
        def parser = slurper.parseText(content);
        return parser.'**'.find{tag -> tag.name() == 'div' & tag.@id == divId};
    }

    static GroovyObject getStartPosition(String content) {
        return slurper.parseText(content);
    }

    static GroovyObject gotoDivId(GroovyObject position, String id) {
        return position.'**'.find{tag -> tag.name() == 'div' & tag.@id == id};
    }

    static GroovyObject gotoUlId(GroovyObject position, String id) {
        return position.'**'.find{tag -> tag.name() == 'ul' & tag.@id == id};
    }

    static Integer getElementsCount(GroovyObject elementList) {
        int count;
        elementList.each { count ++}
        return count.intValue();
    }

    static Object getH1Text(GroovyObject position) {
        def h1 = position.'**'.find{tag -> tag.name() == 'h1'}.text();
        return h1;
    }

    static boolean containsClass(GroovyObject position, String tagClassName) {
        def nodeList = position.'**'.findAll{tag -> tag.@class == tagClassName};
        return (nodeList.size() > 0 );
    }
}
