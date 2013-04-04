package org.rundeck.grails.plugins.viewmodes;

/**
 * Controls view modes template engine if configured.
 */
public class ViewModesControl {
    private ViewModesPagesTemplateEngine viewModesPagesTemplateEngine;
    private boolean enabled;

    public ViewModesControl() {
    }

    public ViewModesPagesTemplateEngine getViewModesPagesTemplateEngine() {
        return viewModesPagesTemplateEngine;
    }

    public void setViewModesPagesTemplateEngine(ViewModesPagesTemplateEngine viewModesPagesTemplateEngine) {
        this.viewModesPagesTemplateEngine = viewModesPagesTemplateEngine;
    }

    public void setMode(String mode) {
        if (null != viewModesPagesTemplateEngine) {
            viewModesPagesTemplateEngine.setMode(mode);
        }
    }

    public String getMode() {
        if (null != viewModesPagesTemplateEngine) {
            return viewModesPagesTemplateEngine.getMode();
        } else {
            return null;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
