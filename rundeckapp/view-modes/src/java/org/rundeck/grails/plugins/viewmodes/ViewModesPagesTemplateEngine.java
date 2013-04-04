package org.rundeck.grails.plugins.viewmodes;

import groovy.text.Template;
import org.apache.log4j.Logger;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.grails.plugins.web.taglib.RenderTagLib;
import org.codehaus.groovy.grails.web.pages.GroovyPagesTemplateEngine;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Extends GroovyPagesTemplateEngine to add additional gsp search locations when resolving a GSP.
 */
public class ViewModesPagesTemplateEngine extends GroovyPagesTemplateEngine {

    public static final Logger log = Logger.getLogger(ViewModesPagesTemplateEngine.class);
    public static final String WEB_INF_GRAILS_APP_VIEWS = "/WEB-INF/grails-app/views";
    public static final String WEB_INF = "/WEB-INF";
    public static final String GRAILS_APP_VIEWS = "/grails-app/views";
    private String mode;

    /**
     * Return the location prefixed with the mode correctly depending on the format of the location
     * @param location
     * @return
     */
    private String prefixLocation(String location) {
        if (location.startsWith(WEB_INF_GRAILS_APP_VIEWS)) {
            return WEB_INF_GRAILS_APP_VIEWS + "/" + mode + location.substring(WEB_INF_GRAILS_APP_VIEWS.length());
        } else if (location.startsWith(WEB_INF)) {
            return WEB_INF + "/" + mode + location.substring(WEB_INF.length());
        } else if (location.startsWith(GRAILS_APP_VIEWS)) {
            return GRAILS_APP_VIEWS + "/" + mode + location.substring(GRAILS_APP_VIEWS.length());
        }
        return "/" + mode + location;
    }

    /**
     * Return the location suffixed with ".mode.gsp" if it is a .gsp
     * @param location
     * @return
     */
    private String suffixLocation(String location) {
        if (location.endsWith(".gsp")) {
            return location.substring(0, location.length() - 4) + "." + mode + ".gsp";
        }
        return location;
    }

    /**
     * Override to add more uris to search for
     * @param uri
     * @return
     */
    @Override
    public Template createTemplateForUri(String[] uri) {
        if (null != mode) {
            List<String> strings = Arrays.asList(uri);
            //        log.debug("createTemplateForUri(String[]): " + uri.length + ": " + strings);
            ArrayList<String> searches = new ArrayList<String>();
            for (String s : uri) {
                searches.add(prefixLocation(s));
                searches.add(suffixLocation(s));
            }
            searches.addAll(strings);
            log.debug("createTemplateForUri(String[]): " + uri.length + ": " + searches);
            return super.createTemplateForUri(searches.toArray(new String[searches.size()]));
        } else {
            return super.createTemplateForUri(uri);
        }
    }

    /**
     * Return the mode
     * @return
     */
    public String getMode() {
        return mode;
    }

    /**
     * Set the mode to use, if null no mode is used.
     * @param mode
     */
    public void setMode(String mode) {
        this.mode = mode;
        //clear the engine page cache
        clearPageCache();
        //The RenderTagLib has its own cache of _template.gsp, which we cannot clear because it is private
//        RenderTagLib.TEMPLATE_CACHE.clear();
    }

}
