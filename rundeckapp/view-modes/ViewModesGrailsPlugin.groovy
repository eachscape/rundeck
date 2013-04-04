import grails.util.GrailsUtil
import org.apache.commons.logging.LogFactory
import org.rundeck.grails.plugins.viewmodes.ViewModesControl
import org.rundeck.grails.plugins.viewmodes.ViewModesPagesTemplateEngine
import org.springframework.beans.factory.config.BeanDefinition

class ViewModesGrailsPlugin {
    static final LOG = LogFactory.getLog(ViewModesGrailsPlugin)
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.3.7 > *"
    // the other plugins this plugin depends on
    def dependsOn = [groovyPages:GrailsUtil.grailsVersion]

    def loadAfter = ['groovyPages']
    def observe = []
    def watchedResources = []
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def author = "Greg Schueler"
    def authorEmail = "greg@dtosolutions.com"
    def title = "view-modes"
    def description = '''\\
Dynamically switch to use a secondary set of views and templates.

Either create a new views subdirectory "grails-app/views/*mode*"
containing the views, or create gsp files named "view.*mode*.gsp".

When "/controller/view.gsp" is to be loaded, then grails will look in "/mode/controller/view.gsp" and
"/controller/view.mode.gsp" first before falling back to the original. Also works for templates ("_template.gsp").

The View Mode can be changed dynamically or disabled (set to null), and can be controlled via a
config value: `plugin.viewModes.mode`.

The plugin must be enabled with `plugin.viewModes.enabled=true`.  In war deployment,
it enables gsp reloading which may have a performance penalty and is governed by the
`grails.gsp.reload.interval`. For this reason it must be explicitly enabled with the
`plugin.viewModes.enabled` config.

Use `ViewModesService` in a controller to change the mode in the running app.
'''

    // URL to the plugin's documentation
    def documentation = "https://github.com/dtolabs/rundeck"

    def doWithWebDescriptor = { xml ->
    }

    def doWithSpring = {
        if(configuredEnabled(application.config)){
            def String configMode=configuredViewMode(application.config)
            if (configMode) {
                LOG.info "View mode: ${configMode}"
            }
            BeanDefinition templateEngine = getBeanDefinition("groovyPagesTemplateEngine")
            templateEngine.beanClass = ViewModesPagesTemplateEngine
            templateEngine.propertyValues.add("mode", configMode)
            templateEngine.propertyValues.add("reloadEnabled", true)
            viewModesControl(ViewModesControl){
                enabled=true
                viewModesPagesTemplateEngine=ref("groovyPagesTemplateEngine")
            }
        }else{
            viewModesControl(ViewModesControl) {
                enabled=false
            }
        }
    }


    def doWithDynamicMethods = { ctx ->
    }

    def doWithApplicationContext = { applicationContext ->
    }

    def onChange = { event ->
    }

    def onConfigChange = { event ->
        def viewmode=configuredViewMode(application.config)
        def bean = event.ctx.getBean("viewModesControl")
        if(bean instanceof ViewModesControl){
            ViewModesControl engine=(ViewModesControl)bean
            if(engine.enabled){
                engine.mode=viewmode
                LOG.info "View mode: ${viewmode}"
            }
        }
    }

    private String configuredViewMode(ConfigObject config) {
        if (config.plugin?.viewModes?.mode) {
            return config.plugin?.viewModes?.mode
        } else {
            return null
        }
    }
    private boolean configuredEnabled(ConfigObject config) {
        if (config.plugin?.viewModes?.enabled) {
            return config.plugin?.viewModes?.enabled in [true,'true']
        } else {
            return false
        }
    }
}
