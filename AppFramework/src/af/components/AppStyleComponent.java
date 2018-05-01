/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package af.components;

import af.AppTemplate;
import static af.settings.AppPropertyType.APP_CSS;
import static af.settings.AppPropertyType.APP_PATH_CSS;
import java.net.URL;
import properties_manager.PropertiesManager;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public abstract class AppStyleComponent {
    
    public static final String CLASS_BORDERED_PANE = "bordered_pane";
    public static final String CLASS_LEFT_BORDERED_PANE = "left_bordered_pane";
    public static final String CLASS_RIGHT_BOARDERED_PANE = "right_bordered_pane";

    public static final String CLASS_FILE_BUTTON = "file_button";
    
    public void initStylesheet(AppTemplate app) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String stylesheet = props.getProperty(APP_PATH_CSS);
        stylesheet += props.getProperty(APP_CSS);
        Class appClass = app.getClass();
        URL styleSheetURL = appClass.getResource(stylesheet);
        String stylesheetPath = styleSheetURL.toExternalForm();
        app.getGUI().getPrimaryScene().getStylesheets().add(stylesheetPath);
    }
}
