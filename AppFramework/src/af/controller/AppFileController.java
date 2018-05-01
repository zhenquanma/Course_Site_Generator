package af.controller;

import af.AppTemplate;
import af.components.AppDataComponent;
import static af.settings.AppPropertyType.*;
import static af.settings.AppStartupConstants.*;
import af.ui.AppGUI;
import af.ui.AppMessageDialogSingleton;
import af.ui.AppYesNoCancelDialogSingleton;
import af.utilities.FileCopier;
import af.utilities.jTPS;
import java.io.File;
import java.io.IOException;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import properties_manager.PropertiesManager;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class AppFileController {

    AppTemplate app;
    
    jTPS jtps;
    
    boolean saved;
    
    //KEEP TRACK OF WORK WHETHER USER HAS EDITED BEFORE
    boolean hasPreEdit;
    
    //KEEP TRACK OF AVAILABILITY OF REDO CONTROL 
    boolean hasUndoed;
    
    File currentWorkFile;
    
    File currentExportFile;
    

    public AppFileController(AppTemplate initApp) {
        saved = true;
        hasPreEdit = false;
        hasUndoed = false;
        app = initApp;
        jtps = new jTPS();
    }
    
    public void markAsEdited(AppGUI gui) {
        saved = false;
        hasPreEdit = true;
        gui.updateLeftToolbarControls(saved);
        gui.updateUndoControls(hasPreEdit);
    }
    
    public jTPS getjTPS() { return jtps; }
    
    public void handleNewRequest() {
        AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        try {
            // WE MAY HAVE TO SAVE CURRENT WORK
            boolean continueToMakeNew = true;
            if (!saved) {
                // THE USER CAN OPT OUT HERE WITH A CANCEL
                continueToMakeNew = promptToSave();
            }

            // IF THE USER REALLY WANTS TO MAKE A NEW COURSE
            if (continueToMakeNew) {
                // RESET THE WORKSPACE
                app.getWorkspaceComponent().resetWorkspace();

                // RESET THE DATA
                app.getDataComponent().resetData();

                // NOW RELOAD THE WORKSPACE WITH THE RESET DATA
                app.getWorkspaceComponent().reloadWorkspace(app.getDataComponent());

                // MAKE SURE THE WORKSPACE IS ACTIVATED
                app.getWorkspaceComponent().activateWorkspace(app.getGUI().getAppPane());

                // WORK IS NOT SAVED
                saved = false;
                currentWorkFile = null;

                // REFRESH THE GUI, WHICH WILL ENABLE AND DISABLE
                // THE APPROPRIATE CONTROLS
                app.getGUI().updateLeftToolbarControls(saved);

                // TELL THE USER NEW WORK IS UNDERWAY
                dialog.show(props.getProperty(NEW_COMPLETED_TITLE), props.getProperty(NEW_COMPLETED_MESSAGE));
            }
        } catch (IOException ioe) {
            // SOMETHING WENT WRONG, PROVIDE FEEDBACK
            dialog.show(props.getProperty(NEW_ERROR_TITLE), props.getProperty(NEW_ERROR_MESSAGE));
        }
    }
    
    public void handleLoadRequest() {
        try {
            // WE MAY HAVE TO SAVE CURRENT WORK
            boolean continueToOpen = true;
            if (!saved) {
                // THE USER CAN OPT OUT HERE WITH A CANCEL
                continueToOpen = promptToSave();
            }

            // IF THE USER REALLY WANTS TO OPEN A Course
            if (continueToOpen) {
                // GO AHEAD AND PROCEED LOADING A Course
                promptToOpen();
            }
        } catch (IOException ioe) {
            // SOMETHING WENT WRONG
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            dialog.show(props.getProperty(LOAD_ERROR_TITLE), props.getProperty(LOAD_ERROR_MESSAGE));
        }
    }
    
    public void handleSaveRequest() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        try {
            // MAYBE WE ALREADY KNOW THE FILE
            if (currentWorkFile != null) {
                saveWork(currentWorkFile);
            } // OTHERWISE WE NEED TO PROMPT THE USER
            else {
                // PROMPT THE USER FOR A FILE NAME
                FileChooser fc = new FileChooser();
                fc.setInitialDirectory(new File(PATH_WORK));
                fc.setTitle(props.getProperty(SAVE_WORK_TITLE));
                fc.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter(String.format("%s (*.%s)", props.getProperty(WORK_FILE_EXT_DESC), props.getProperty(WORK_FILE_EXT)),
                                String.format("*.%s", props.getProperty(WORK_FILE_EXT))));

                File selectedFile = fc.showSaveDialog(app.getGUI().getWindow());
                if (selectedFile != null) {
                    saveWork(selectedFile);
                }
           }
        } catch (IOException ioe) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(SAVE_ERROR_TITLE), props.getProperty(SAVE_ERROR_MESSAGE));
        }
    }
    
    public void handleSaveAsRequest() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        try {
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File(PATH_WORK));
            fc.setTitle(props.getProperty(SAVE_WORK_TITLE));
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter(String.format("%s (*.%s)", props.getProperty(WORK_FILE_EXT_DESC), props.getProperty(WORK_FILE_EXT)),
                            String.format("*.%s", props.getProperty(WORK_FILE_EXT))));

            File selectedFile = fc.showSaveDialog(app.getGUI().getWindow());
            if (selectedFile != null) {
                saveWork(selectedFile);
            }
        } catch (IOException ioe) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(SAVE_ERROR_TITLE), props.getProperty(SAVE_ERROR_MESSAGE));
        }
    }
  
    public void handleExportRequest() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        //USER HAS SELECTED A DIRECTORY TO EXPORT
        if (currentExportFile != null) {
            try {
                app.getFileComponent().exportData(app.getDataComponent(), currentExportFile.getPath());
            } catch (IOException ioe) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(EXPORT_ERROR_TITLE), props.getProperty(EXPORT_ERROR_MESSAGE));
            }
        } else {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(EXPORT_DIRECTORY_EMPTY_TITLE), props.getProperty(EXPORT_DIRECTORY_EMPTY_MESSAGE));
        }  
    }
    
    public void handleExitRequest() {
        try {
            // WE MAY HAVE TO SAVE CURRENT WORK
            boolean continueToExit = true;
            if (!saved) {
                // THE USER CAN OPT OUT HERE
                continueToExit = promptToSave();
            }

            // IF THE USER REALLY WANTS TO EXIT THE APP
            if (continueToExit) {
                // EXIT THE APPLICATION
                System.exit(0);
            }
        } catch (IOException ioe) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            dialog.show(props.getProperty(SAVE_ERROR_TITLE), props.getProperty(SAVE_ERROR_MESSAGE));
        }
    }
    
    public void handleUndoRequest() {
        jtps.undoTransaction();
        hasUndoed = true;
        app.getGUI().updateRedoControls(hasUndoed);
    }
    
    public void handleRedoRequest() {
        jtps.doTransaction();
        hasPreEdit = true;
        app.getGUI().updateUndoControls(hasPreEdit);
    }
    
    public void handleAboutRequest() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
        dialog.show(props.getProperty(ABOUT_DIALOG_TITLE), props.getProperty(ABOUT_DIALOG_MESSAGE));
    }
    
    public void settExportFile(File exportFile) {
        currentExportFile = exportFile;
    }
    
    
   /**
     * This helper method verifies that the user really wants to save their
     * unsaved work, which they might not want to do. Note that it could be used
     * in multiple contexts before doing other actions, like creating new work,
     * or opening another file. Note that the user will be presented with 3
     * options: YES, NO, and CANCEL. YES means the user wants to save their work
     * and continue the other action (we return true to denote this), NO means
     * don't save the work but continue with the other action (true is
     * returned), CANCEL means don't save the work and don't continue with the
     * other action (false is returned).
     *
     * @return true if the user presses the YES option to save, true if the user
     * presses the NO option to not save, false if the user presses the CANCEL
     * option to not continue.
     */
    private boolean promptToSave() throws IOException {
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // CHECK TO SEE IF THE CURRENT WORK HAS
        // BEEN SAVED AT LEAST ONCE
        // PROMPT THE USER TO SAVE UNSAVED WORK
        AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
        yesNoDialog.show(props.getProperty(SAVE_UNSAVED_WORK_TITLE), props.getProperty(SAVE_UNSAVED_WORK_MESSAGE));

        // AND NOW GET THE USER'S SELECTION
        String selection = yesNoDialog.getSelection();

        // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
        if (selection.equals(AppYesNoCancelDialogSingleton.YES)) {
            // SAVE THE DATA FILE
            AppDataComponent dataManager = app.getDataComponent();

            if (currentWorkFile == null) {
                // PROMPT THE USER FOR A FILE NAME
                FileChooser fc = new FileChooser();
                fc.setInitialDirectory(new File(PATH_WORK));
                fc.setTitle(props.getProperty(SAVE_WORK_TITLE));
                fc.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter(String.format("%s (*.%s)", props.getProperty(WORK_FILE_EXT_DESC), props.getProperty(WORK_FILE_EXT)),
                            String.format("*.%s", props.getProperty(WORK_FILE_EXT))));

                File selectedFile = fc.showSaveDialog(app.getGUI().getWindow());
                if (selectedFile != null) {
                    saveWork(selectedFile);
                    saved = true;
                }
            } else {
                saveWork(currentWorkFile);
                saved = true;
            }
        } // IF THE USER SAID CANCEL, THEN WE'LL TELL WHOEVER
        // CALLED THIS THAT THE USER IS NOT INTERESTED ANYMORE
        else if (selection.equals(AppYesNoCancelDialogSingleton.CANCEL)) {
            return false;
        }

        // IF THE USER SAID NO, WE JUST GO ON WITHOUT SAVING
        // BUT FOR BOTH YES AND NO WE DO WHATEVER THE USER
        // HAD IN MIND IN THE FIRST PLACE
        return true;
    }

    /**
     * This helper method asks the user for a file to open. The user-selected
     * file is then loaded and the GUI updated. Note that if the user cancels
     * the open process, nothing is done. If an error occurs loading the file, a
     * message is displayed, but nothing changes.
     */
    private void promptToOpen() throws IOException{
        // WE'LL NEED TO GET CUSTOMIZED STUFF WITH THIS
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // AND NOW ASK THE USER FOR THE FILE TO OPEN
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_WORK));
        fc.setTitle(props.getProperty(LOAD_WORK_TITLE));
        fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter(String.format("%s (*.%s)", props.getProperty(WORK_FILE_EXT_DESC), props.getProperty(WORK_FILE_EXT)),
                            String.format("*.%s", props.getProperty(WORK_FILE_EXT))));
        File selectedFile = fc.showOpenDialog(app.getGUI().getWindow());

        // ONLY OPEN A NEW FILE IF THE USER SAYS OK
        if (selectedFile != null) {
            try {
                // RESET THE WORKSPACE
                app.getWorkspaceComponent().resetWorkspace();

                // RESET THE DATA
                app.getDataComponent().resetData();

                // LOAD THE FILE INTO THE DATA
                app.getFileComponent().loadData(app.getDataComponent(), selectedFile.getAbsolutePath());

                // MAKE SURE THE WORKSPACE IS ACTIVATED
                app.getWorkspaceComponent().activateWorkspace(app.getGUI().getAppPane());

                // AND MAKE SURE THE FILE BUTTONS ARE PROPERLY ENABLED
                saved = true;
                app.getGUI().updateLeftToolbarControls(saved);     
            } catch (Exception e) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(LOAD_ERROR_TITLE), props.getProperty(LOAD_ERROR_MESSAGE));
            }
        }
    }
    
    // HELPER METHOD FOR SAVING WORK
    private void saveWork(File selectedFile) throws IOException {
        // SAVE IT TO A FILE
        app.getFileComponent().saveData(app.getDataComponent(), selectedFile.getPath());

        // MARK IT AS SAVED
        currentWorkFile = selectedFile;
        saved = true;

        // TELL THE USER THE FILE HAS BEEN SAVED
        AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        dialog.show(props.getProperty(SAVE_COMPLETED_TITLE), props.getProperty(SAVE_COMPLETED_MESSAGE));

        // AND REFRESH THE GUI, WHICH WILL ENABLE AND DISABLE
        // THE APPROPRIATE CONTROLS
        app.getGUI().updateLeftToolbarControls(saved);
    }
    
    /**
     * This mutator method marks the file as not saved, which means that when
     * the user wants to do a file-type operation, we should prompt the user to
     * save current work first. Note that this method should be called any time
     * the course is changed in some way.
     */
    public void markFileAsNotSaved() {
        saved = false;
    }

    /**
     * Accessor method for checking to see if the current work has been saved
     * since it was last edited.
     *
     * @return true if the current work is saved to the file, false otherwise.
     */
    public boolean isSaved() {
        return saved;
    }

}
