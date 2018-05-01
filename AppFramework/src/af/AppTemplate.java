package af;

import af.components.AppDataComponent;
import af.components.AppFileComponent;
import af.components.AppStyleComponent;
import af.components.AppWorkspaceComponent;
import static af.settings.AppPropertyType.*;
import static af.settings.AppStartupConstants.*;
import af.ui.AppGUI;
import af.ui.AppMessageDialogSingleton;
import af.ui.AppYesNoCancelDialogSingleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.application.Application;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;
import properties_manager.InvalidXMLFileFormatException;
import properties_manager.PropertiesManager;


/**
 * This is the framework's JavaFX application. It provides the start method
 * that begins the program initialization, which delegates component
 * initialization to the application-specific child class' hook function.
 * @author Zhenquan
 */
public abstract class AppTemplate extends Application{
    protected AppGUI gui;
    protected AppDataComponent dataComponent;
    protected AppFileComponent fileComponent;
    protected AppWorkspaceComponent workspaceComponent;
    protected AppStyleComponent styleComponent;
    
    public abstract void buildAppComponentsHook();
    
    public AppGUI getGUI() { return gui; }
    
    public AppDataComponent getDataComponent(){ return dataComponent; }
    
    public AppFileComponent getFileComponent() { return fileComponent; }
    
    public AppWorkspaceComponent getWorkspaceComponent() { return workspaceComponent; }
    
    public AppStyleComponent getStyleComponent() { return styleComponent; }
    
    @Override
    public void start(Stage primaryStage) {
        AppMessageDialogSingleton messageDialog = AppMessageDialogSingleton.getSingleton();
        messageDialog.init(primaryStage);
        AppYesNoCancelDialogSingleton yesNoCancelDialog = AppYesNoCancelDialogSingleton.getSingleton();
        yesNoCancelDialog.init(primaryStage);
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        List<String> choices = new ArrayList<>();
        choices.add(ENGLISH_STRING);
        choices.add(CHINESE_STRING);
        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>(ENGLISH_STRING, choices);
        choiceDialog.setTitle(ALERT_TITLE);
        choiceDialog.setHeaderText(ALERT_HEADER_TEXT);
        Optional<String> result = choiceDialog.showAndWait();
        if (result.isPresent()) {
            if (choiceDialog.getSelectedItem().equalsIgnoreCase(ENGLISH_STRING)) {
//                try {
                    boolean success = loadProperties(APP_PROPERTIES_FILE_ENGLISH_NAME);
                    if (success) {
                        String appTitle = props.getProperty(APP_TITLE);
                        gui = new AppGUI(primaryStage, appTitle, this);
                        buildAppComponentsHook();
                    }
//                } catch (Exception e) {
//                    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
//                    dialog.show(props.getProperty(PROPERTIES_LOAD_ERROR_TITLE), props.getProperty(PROPERTIES_FILE_ERROR_ENGLISH_MESSAGE));
//                }
            } else {
                try {
                    boolean success = loadProperties(APP_PROPERTIES_FILE_CHINESE_NAME);
                    if (success) {
                        String appTitle = props.getProperty(APP_TITLE);
                        gui = new AppGUI(primaryStage, appTitle, this);
                        buildAppComponentsHook();
                    }
                } catch (Exception e) {
                    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                    dialog.show(props.getProperty(PROPERTIES_LOAD_ERROR_TITLE), props.getProperty(PROPERTIES_FILE_ERROR_CHINESE_MESSAGE));
                }
            }
        }
           
    }
     /**
     * Loads this application's properties file, which has a number of settings
     * for initializing the user interface.
     *
     * @param propertiesFileName The XML file containing properties to be
     * loaded in order to initialize the UI.
     * 
     * @return true if the properties file was loaded successfully, false
     * otherwise.
     */
    public boolean loadProperties(String propertiesFileName) {
	    PropertiesManager props = PropertiesManager.getPropertiesManager();
	try {
	    // LOAD THE SETTINGS FOR STARTING THE APP
	    props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, PATH_DATA);
	    props.loadProperties(propertiesFileName, PROPERTIES_SCHEMA_FILE_NAME);
	    return true;
	} catch (InvalidXMLFileFormatException ixmlffe) {
	    // SOMETHING WENT WRONG INITIALIZING THE XML FILE
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show(props.getProperty(PROPERTIES_LOAD_ERROR_TITLE), props.getProperty(PROPERTIES_LOAD_ERROR_MESSAGE));
	    return false;
	}
    }  
                
}
