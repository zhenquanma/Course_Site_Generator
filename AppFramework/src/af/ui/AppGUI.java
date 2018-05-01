package af.ui;

import af.AppTemplate;
import static af.components.AppStyleComponent.*;
import af.controller.AppFileController;
import static af.settings.AppPropertyType.*;
import static af.settings.AppStartupConstants.*;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>, Richard McKenna
 */
public class AppGUI {
    
    protected AppFileController fileController;
    
    protected Stage primaryStage;
    
    protected Scene primaryScene;
    
    protected BorderPane appPane;
    
    protected HBox fileToolbarPane;   
    protected HBox leftToolbarBox;    
    protected HBox rightToolbarBox;
    
    protected Button newButton;
    protected Button loadButton;
    protected Button saveButton;
    protected Button saveAsButton;
    protected Button exportButton;
    protected Button exitButton;
    protected Button undoButton;
    protected Button redoButton;
    protected Button aboutButton;
    
    protected AppYesNoCancelDialogSingleton yesNoCancelDialogSingleton;
    
    protected String appTitle;
    
    public AppGUI(Stage initPrimaryStage, String initAppTitle, AppTemplate app) {
        primaryStage = initPrimaryStage;
        appTitle = initAppTitle;
        
        initFileToolbar(app);
        
        initWindow();
    }
    
    public AppFileController getAppFileController() { return fileController; }
    
    public BorderPane getAppPane() { return appPane; }
    
    public Scene getPrimaryScene() { return primaryScene; }
    
    public Stage getWindow() { return primaryStage; }
    
     /**
     * This method is used to activate/deactivate toolbar buttons when
     * they can and cannot be used.
     * 
     * @param saved Describes whether the loaded Page has been saved or not.
     */
    public void updateLeftToolbarControls(boolean saved) {
        // THIS TOGGLES WITH WHETHER THE CURRENT COURSE
        // HAS BEEN SAVED OR NOT
        saveButton.setDisable(saved);

        // ALL THE OTHER BUTTONS ARE ALWAYS ENABLED
        // ONCE EDITING WROKS BEGINS
	newButton.setDisable(false);
        loadButton.setDisable(false);
        saveAsButton.setDisable(false);
        exportButton.setDisable(false);
	exitButton.setDisable(false); 
        aboutButton.setDisable(false);
    }
    
    /**
     * This method is used to active/deactive the undo button
     * when it can and cannot be used
     * 
     * @param hasPreEdit Describes whether there is a previous edit
     * can be undo or not.
     */
    public void updateUndoControls(boolean hasPreEdit) {
        undoButton.setDisable(!hasPreEdit);
    }
    
    public void updateRedoControls(boolean hasUndoed) {
        redoButton.setDisable(!hasUndoed);
    }
    
    private void initFileToolbar(AppTemplate app) {
        fileToolbarPane = new HBox();
        leftToolbarBox = new HBox();
        rightToolbarBox = new HBox();
        
        newButton = initChildButton(leftToolbarBox,	NEW_ICON.toString(),	    NEW_TOOLTIP.toString(),	false);
        loadButton = initChildButton(leftToolbarBox,	LOAD_ICON.toString(),	    LOAD_TOOLTIP.toString(),	false);
        saveButton = initChildButton(leftToolbarBox,	SAVE_ICON.toString(),	    SAVE_TOOLTIP.toString(),	true);
        saveAsButton = initChildButton(leftToolbarBox, SAVE_AS_ICON.toString(),    SAVE_AS_TOOLTIP.toString(), true);
        exportButton = initChildButton(leftToolbarBox, EXPORT_ICON.toString(),     EXPORT_TOOLTIP.toString(),  true);
        exitButton = initChildButton(leftToolbarBox,	EXIT_ICON.toString(),	    EXIT_TOOLTIP.toString(),	false);
        undoButton = initChildButton(rightToolbarBox, UNDO_ICON.toString(), UNDO_TOOLTIP.toString(), true);
        redoButton = initChildButton(rightToolbarBox, REDO_ICON.toString(), REDO_TOOLTIP.toString(), true);
        aboutButton = initChildButton(rightToolbarBox, ABOUT_ICON.toString(), ABOUT_TOOLTIP.toString(), false);
        
        fileToolbarPane.getChildren().addAll(leftToolbarBox, rightToolbarBox);
 
        
        fileController = new AppFileController(app);
        newButton.setOnAction(e -> {
            fileController.handleNewRequest();
        });
        loadButton.setOnAction(e -> {
            fileController.handleLoadRequest();
        });
        saveButton.setOnAction(e ->{
            fileController.handleSaveRequest();
        });
        saveAsButton.setOnAction(e ->{
            fileController.handleSaveAsRequest();
        });
        exportButton.setOnAction(e ->{
            fileController.handleExportRequest();
        });
        exitButton.setOnAction(e ->{
            fileController.handleExitRequest();
        });
        undoButton.setOnAction(e ->{
            fileController.handleUndoRequest();
        });
        redoButton.setOnAction(e ->{
            fileController.handleRedoRequest();
        });
        aboutButton.setOnAction(e ->{
            fileController.handleAboutRequest();
        });
    }
    
    private void initWindow() {
        primaryStage.setTitle(appTitle);
        
        // GET THE SIZE OF THE SCREEN
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        
        //SIZE THE WINDOW
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        

        
        appPane = new BorderPane();
        appPane.setTop(fileToolbarPane);
        appPane.prefWidthProperty().bind(primaryStage.widthProperty());
        leftToolbarBox.prefWidthProperty().bind(appPane.widthProperty().multiply(0.6));
        rightToolbarBox.prefWidthProperty().bind(appPane.widthProperty().multiply(0.4));
        
        primaryScene = new Scene(appPane);
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String appIcon = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(APP_LOGO);
        primaryStage.getIcons().add(new Image(appIcon));   
        primaryStage.setScene(primaryScene);
        primaryStage.show();
    }
    
     /**
     * This is a public helper method for initializing a simple button with
     * an icon and tooltip and placing it into a toolbar.
     * 
     * @param toolbar Toolbar pane into which to place this button.
     * 
     * @param icon Icon image file name for the button.
     * 
     * @param tooltip Tooltip to appear when the user mouses over the button.
     * 
     * @param disabled true if the button is to start off disabled, false otherwise.
     * 
     * @return A constructed, fully initialized button placed into its appropriate
     * pane container.
     */
    public Button initChildButton(Pane toolbar, String icon, String tooltip, boolean disabled) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
	
	// LOAD THE ICON FROM THE PROVIDED FILE
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(icon);
        Image buttonImage = new Image(imagePath);
	
	// NOW MAKE THE BUTTON
        Button button = new Button();
        button.setDisable(disabled);
        button.setGraphic(new ImageView(buttonImage));
        Tooltip buttonTooltip = new Tooltip(props.getProperty(tooltip));
        button.setTooltip(buttonTooltip);
	
	// PUT THE BUTTON IN THE TOOLBAR
        toolbar.getChildren().add(button);
	
	// AND RETURN THE COMPLETED BUTTON
        return button;
    }
    
    
    public void initFileToolbarStyle() {
        fileToolbarPane.getStyleClass().add(CLASS_BORDERED_PANE);
        leftToolbarBox.getStyleClass().add(CLASS_LEFT_BORDERED_PANE);
        rightToolbarBox.getStyleClass().add(CLASS_RIGHT_BOARDERED_PANE);
	newButton.getStyleClass().add(CLASS_FILE_BUTTON);
	loadButton.getStyleClass().add(CLASS_FILE_BUTTON);
	saveButton.getStyleClass().add(CLASS_FILE_BUTTON);
        saveAsButton.getStyleClass().add(CLASS_FILE_BUTTON);
        exportButton.getStyleClass().add(CLASS_FILE_BUTTON);
	exitButton.getStyleClass().add(CLASS_FILE_BUTTON);
        undoButton.getStyleClass().add(CLASS_FILE_BUTTON);
        redoButton.getStyleClass().add(CLASS_FILE_BUTTON);  
        aboutButton.getStyleClass().add(CLASS_FILE_BUTTON);
    }
}
