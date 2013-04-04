package org.rundeck.grails.plugins.viewmodes

/**
 * ViewModesService can change the view mode and determine if it is enabled or not.
 */
class ViewModesService {
    static transactional = false
    static scope = "singleton"
    def ViewModesControl viewModesControl

    /**
     * Return true if ViewModes is enabled
     * @return
     */
    def getEnabled() {
        viewModesControl.enabled
    }
    /**
     * Return the current mode
     * @return
     */
    def getMode(){
        viewModesControl.mode
    }
    /**
     * Set the mode
     * @param mode
     * @return
     */
    def setMode(String mode){
        viewModesControl.mode = mode
    }
}
