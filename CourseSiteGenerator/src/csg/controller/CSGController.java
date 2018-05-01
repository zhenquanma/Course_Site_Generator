package csg.controller;

import static af.settings.AppPropertyType.*;
import static af.settings.AppStartupConstants.PATH_IMAGES;
import af.ui.AppGUI;
import af.ui.AppMessageDialogSingleton;
import af.ui.AppYesNoCancelDialogSingleton;
import af.utilities.jTPS;
import csg.CSGeneratorApp;
import static csg.CSGeneratorProp.*;
import csg.data.CSGeneratorData;
import csg.data.CourseData;
import static csg.data.CourseData.HWS_HTML;
import static csg.data.CourseData.INDEX_HTML;
import static csg.data.CourseData.PROJECTS_HTML;
import static csg.data.CourseData.SCHEDULE_HTML;
import static csg.data.CourseData.SYLLABUS_HTML;
import csg.data.RecitationData;
import csg.data.TAData;
import static csg.style.TAStyle.*;
import csg.transactions.AddRecitation_Transaction;
import csg.transactions.AddSchedule_Transaction;
import csg.transactions.AddStudent_Transaction;
import csg.transactions.AddTA_Transaction;
import csg.transactions.AddTeam_Transaction;
import csg.transactions.DeleteRecitation_Transaction;
import csg.transactions.DeleteSchedule_Transaction;
import csg.transactions.DeleteStudent_Transaction;
import csg.transactions.DeleteTA_Transaction;
import csg.transactions.DeleteTeam_Transaction;
import csg.transactions.ToggleTAOfficeHour_Transaction;
import csg.transactions.UpdateRecitation_Transaction;
import csg.transactions.UpdateSchedule_Transaction;
import csg.transactions.UpdateStudent_Transaction;
import csg.transactions.UpdateTA_Transaction;
import csg.transactions.UpdateTeam_Transaction;
import csg.transactions.Update_Time_Transaction;
import csg.utilities.EmailValidator;
import csg.utilities.Recitation;
import csg.utilities.ScheduleItem;
import csg.utilities.Student;
import csg.utilities.TeachingAssistant;
import csg.utilities.Team;
import csg.workspace.CSGWorkspace;
import csg.workspace.CourseDetailPage;
import csg.workspace.Page;
import csg.workspace.ProjectDataPage;
import csg.workspace.RecitationDataPage;
import csg.workspace.ScheduleDataPage;
import csg.workspace.TADataPage;
import java.io.File;
import java.net.MalformedURLException;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import properties_manager.PropertiesManager;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class CSGController {

    CSGeneratorApp app;
    jTPS jtps;

    public CSGController(CSGeneratorApp initApp) {
        app = initApp;
        jtps = app.getGUI().getAppFileController().getjTPS();
    }

    public void markWorkAsEdited() {
        // MARK WORK AS EDITED
        AppGUI gui = app.getGUI();
        gui.getAppFileController().markAsEdited(gui);
    }

    /**
     * This method responds to when the user requests to add a new TA via the
     * UI. Note that it must first do some validation to make sure a unique name
     * and email address has been provided.
     */
    public void handleAddTA() {
        TADataPage taDataPage = ((CSGWorkspace) app.getWorkspaceComponent()).getTADataPage();
        TextField nameTextField = taDataPage.getNameTextField();
        TextField emailTextField = taDataPage.getEmailTextField();
        String name = nameTextField.getText();
        String email = emailTextField.getText();
        EmailValidator emailValidator = new EmailValidator();

        // WE'LL NEED TO ASK THE DATA SOME QUESTIONS TOO
        TAData data = ((CSGeneratorData) app.getDataComponent()).getTAData();

        // WE'LL NEED THIS IN CASE WE NEED TO DISPLAY ANY ERROR MESSAGES
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // DID THE USER NEGLECT TO PROVIDE A TA NAME?
        if (name.isEmpty()) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(MISSING_TA_NAME_TITLE), props.getProperty(MISSING_TA_NAME_MESSAGE));
        } // DID THE USER NEGLECT TO PROVIDE A TA EMAIL?
        else if (email.isEmpty()) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(MISSING_TA_EMAIL_TITLE), props.getProperty(MISSING_TA_EMAIL_MESSAGE));
        } //Validate the email
        else if (!emailValidator.validate(email)) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(INVALID_EMAIL_TITLE), props.getProperty(INVALID_EMAIL));
        } // DOES A TA ALREADY HAVE THE SAME NAME OR EMAIL?
        else if (data.containsTA(name, email)) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE), props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE));
        } // EVERYTHING IS FINE, ADD A NEW TA
        else {
            AddTA_Transaction addTA = new AddTA_Transaction(app, name, email);
            jtps.addTransaction(addTA);

            nameTextField.requestFocus();
        }
    }
    
    public <T extends Page> void handleMouseClicked(T t, MouseEvent e) {
        if (t instanceof TADataPage) {
            if ((e.getButton() == MouseButton.PRIMARY) && (e.getClickCount() == 1)) {
                TADataPage taDataPage = (TADataPage) t;
                TableView taTable = taDataPage.getTATable();
                Object selectedItem = taTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    taPageAddUpdateSwitch(true);
                }
            }
        } 
        
        else if (t instanceof RecitationDataPage) {
            if ((e.getButton() == MouseButton.PRIMARY) && (e.getClickCount() == 1)) {
                RecitationDataPage recDataPage = (RecitationDataPage) t;
                TableView recTable = recDataPage.getRecTable();
                Object selectedItem = recTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    TextField sectionField = recDataPage.getSectionTextField();
                    TextField instructorField = recDataPage.getInstructorTextField();
                    TextField dayTimeField = recDataPage.getDayTimeTextField();
                    TextField locationField = recDataPage.getLocationTextField();
                    ComboBox ta1ComboBox = recDataPage.getTA1ComboBox();
                    ComboBox ta2ComboBox = recDataPage.getTA2ComboBox();
                    Button clearButton = recDataPage.getClearButton();

                    Recitation rec = (Recitation) selectedItem;
                    sectionField.setText(rec.getSection());
                    instructorField.setText(rec.getInstructor());
                    dayTimeField.setText(rec.getDayTime());
                    locationField.setText(rec.getLocation());
                    ta1ComboBox.getSelectionModel().select(rec.getTa1());
                    ta2ComboBox.getSelectionModel().select(rec.getTa2());

                    recDataPage.getAddUpdateButton().setOnAction(event -> {
                        handleUpdateRecitation();
                    });
                    clearButton.setDisable(false);
                }
            }
        }
        
        else if (t instanceof ScheduleDataPage) {
            if ((e.getButton() == MouseButton.PRIMARY) && (e.getClickCount() == 1)) {
                ScheduleDataPage schedulePage = (ScheduleDataPage) t;
                TableView scheduleTable = schedulePage.getScheduleTable();
                Object selectedItem = scheduleTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    ScheduleItem scheduleItem = (ScheduleItem) selectedItem;
                    String type = scheduleItem.getType();
                    String date = scheduleItem.getDate();
                    String time = scheduleItem.getTime();
                    String title = scheduleItem.getTitle();
                    String topic = scheduleItem.getTopic();
                    String link = scheduleItem.getLink();
                    String criteria = scheduleItem.getCriteria();
                    schedulePage.setTypeComboBox(type);
                    schedulePage.setDatePicker(date);
                    schedulePage.setTimeTextField(time);
                    schedulePage.setTitleTextField(title);
                    schedulePage.setTopicTextField(topic);
                    schedulePage.setLinkTextField(link);
                    schedulePage.setCriteriaTextField(criteria);

                    schedulePage.getAddUpdateButton().setOnAction(event -> {
                        handleUpdateSchedule();
                    });
                    schedulePage.getClearButton().setDisable(false);
                }
            }
        }
    }
    
    public void teamsTableHandleMouseClick(MouseEvent event) {
        if ((event.getButton() == MouseButton.PRIMARY) && (event.getClickCount() == 1)) {
            ProjectDataPage projectPage = ((CSGWorkspace) app.getWorkspaceComponent()).getProjectDataPage();
            TableView teamsTable = projectPage.getTeamsTable();
            Object selectedItem = teamsTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                Team team = (Team) selectedItem;
                String name = team.getName();
                String color = "#" + team.getColor();
                String textColor = "#" + team.getTextColor();
                String link = team.getLink();
                projectPage.getNameTextField().setText(name);
                projectPage.getColorPicker().setValue(Color.web(color));
                projectPage.getTextColorPicker().setValue(Color.web(textColor));
                projectPage.getLinkTextField().setText(link);
                projectPage.getTeamAddUpdateButton().setOnAction(e -> {
                    handleUpdateTeam();
                });
                projectPage.getTeamClearButton().setDisable(false);
            }
        }
    }

    public void studentsTableHandleMouseClick(MouseEvent event) {
        if ((event.getButton() == MouseButton.PRIMARY) && (event.getClickCount() == 1)) {
            ProjectDataPage projectPage = ((CSGWorkspace) app.getWorkspaceComponent()).getProjectDataPage();
            TableView studentTable = projectPage.getStudentsTable();
            Object selectedItem = studentTable.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                Student student = (Student) selectedItem;
                String firstName = student.getFirstName();
                String lastName = student.getLastName();
                String teamName = student.getTeamName();
                String role = student.getRole();
                projectPage.getFirstNameTextField().setText(firstName);
                projectPage.getLastNameTextField().setText(lastName);
                projectPage.getTeamComboBox().setValue(teamName);
                projectPage.getRoleTextField().setText(role);
                projectPage.getStudentAddUpdateButton().setOnAction(e -> {
                    handleUpdateStudent();
                });
                projectPage.getStudentClearButton().setDisable(false);
            }
        }
    }
        
    
    /**
     * If the @param hasSelectedItem is true, then switch the add button to
     * update button. If the @param hasSelectedItem is false, then switch it
     * back to add button.
     *
     * @param hasSelectedItem
     */
    public void taPageAddUpdateSwitch(boolean hasSelectedItem) {
        TADataPage taDataPage = ((CSGWorkspace) app.getWorkspaceComponent()).getTADataPage();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        TableView taTable = taDataPage.getTATable();
        Button addUpdateBt = taDataPage.getAddButton();
        Button clearButton = taDataPage.getClearButton();
        clearButton.setDisable(false);
        TextField nameTextField = taDataPage.getNameTextField();
        TextField emailTextField = taDataPage.getEmailTextField();
        if (hasSelectedItem) {
            //Switch the button to the status of UPDATE TA
            TeachingAssistant ta = (TeachingAssistant) taTable.getSelectionModel().getSelectedItem();
            String oldName = ta.getName();
            String oldEmail = ta.getEmail();
            nameTextField.setText(oldName);
            emailTextField.setText(oldEmail);
            //Switch the button to the status of UPDATE TA
            addUpdateBt.setText(props.getProperty(UPDATE_BUTTON_TEXT));
            addUpdateBt.setOnAction(e -> {
                handleUpdateTA();
            });
            taDataPage.getNameTextField().setOnAction(e -> {
                handleUpdateTA();
            });
            taDataPage.getEmailTextField().setOnAction(e -> {
                handleUpdateTA();
            });

        } //No selected item
        else {
            addUpdateBt.setText(props.getProperty(ADD_BUTTON_TEXT));
            addUpdateBt.setOnAction(e -> {
                this.handleAddTA();
            });
            clearButton.setDisable(true);
        }
    }

    public void handleUpdateTA() {
        TADataPage taDataPage = ((CSGWorkspace) app.getWorkspaceComponent()).getTADataPage();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        TableView taTable = taDataPage.getTATable();

        TextField nameTextField = taDataPage.getNameTextField();
        TextField emailTextField = taDataPage.getEmailTextField();
        TeachingAssistant ta = (TeachingAssistant) taTable.getSelectionModel().getSelectedItem();
        String oldName = ta.getName();
        String oldEmail = ta.getEmail();

        EmailValidator emailValidator = new EmailValidator();

        TAData data = ((CSGeneratorData) app.getDataComponent()).getTAData();

        String newName = nameTextField.getText();
        String newEmail = emailTextField.getText();

        if (newName.isEmpty()) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(MISSING_TA_NAME_TITLE), props.getProperty(MISSING_TA_NAME_MESSAGE));
            nameTextField.setText(oldName);
        } // DID THE USER NEGLECT TO PROVIDE A TA EMAIL?
        else if (newEmail.isEmpty()) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(MISSING_TA_EMAIL_TITLE), props.getProperty(MISSING_TA_EMAIL_MESSAGE));
            emailTextField.setText(oldEmail);
        } //Validate the email
        else if (!emailValidator.validate(newEmail)) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(INVALID_EMAIL_TITLE), props.getProperty(INVALID_EMAIL));
        } // DOES ANOTHER TAs ALREADY HAVE THE SAME NAME OR EMAIL?
        else if (data.containsTAExclude(ta, newName, newEmail)) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE), props.getProperty(TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE));
        } // EVERYTHING IS FINE, ADD A NEW TA
        else {
            UpdateTA_Transaction updateTA = new UpdateTA_Transaction(app, oldName, oldEmail, newName, newEmail);
            jtps.addTransaction(updateTA);

            //CLEAR SELECTION
            taTable.getSelectionModel().clearSelection();
        }
    }

    public void taPageHandleClearButton() {
        TADataPage taDataPage = ((CSGWorkspace) app.getWorkspaceComponent()).getTADataPage();
        TableView taTable = taDataPage.getTATable();

        //CLEAR SELECTION
        taTable.getSelectionModel().clearSelection();

        //CLEAR TEXT FIELDS
        taDataPage.getNameTextField().setText("");
        taDataPage.getEmailTextField().setText("");
        taPageAddUpdateSwitch(false);
        // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
        taDataPage.getNameTextField().requestFocus();
    }

    public void recPageHandleClear() {
        RecitationDataPage recPage = ((CSGWorkspace) app.getWorkspaceComponent()).getRecitationDataPage();
        TableView recTable = recPage.getRecTable();

        recTable.getSelectionModel().clearSelection();

        recPage.getSectionTextField().setText("");
        recPage.getInstructorTextField().setText("");
        recPage.getDayTimeTextField().setText("");
        recPage.getLocationTextField().setText("");
        recPage.getTA1ComboBox().setValue(null);
        recPage.getTA2ComboBox().setValue(null);
        recPage.getAddUpdateButton().setOnAction(e -> {
            handleAddRecitation();
        });
        recPage.getClearButton().setDisable(true);
    }

    public void schedulePageHandleClear() {
        ScheduleDataPage schedulePage = ((CSGWorkspace) app.getWorkspaceComponent()).getScheduleDataPage();
        TableView scheduleTable = schedulePage.getScheduleTable();

        scheduleTable.getSelectionModel().clearSelection();

        schedulePage.getTypeComboBox().setValue(null);
        schedulePage.getDatePicker().setValue(null);
        schedulePage.getTimeTextField().setText("");
        schedulePage.getTitleTextField().setText("");
        schedulePage.getTopicTextField().setText("");
        schedulePage.getLinkTextField().setText("");
        schedulePage.getCriteriaTextField().setText("");
        schedulePage.getAddUpdateButton().setOnAction(e -> {
            handleAddSchedule();
        });
        schedulePage.getClearButton().setDisable(true);
    }

    public void projectPageTeamsTableHandleClear() {
        ProjectDataPage projectPage = ((CSGWorkspace) app.getWorkspaceComponent()).getProjectDataPage();
        projectPage.getTeamsTable().getSelectionModel().clearSelection();
        projectPage.getNameTextField().setText("");
        projectPage.getColorPicker().setValue(null);
        projectPage.getTextColorPicker().setValue(null);
        projectPage.getLinkTextField().setText("");
        projectPage.getTeamAddUpdateButton().setOnAction(e -> {
            handleAddTeam();
        });
        projectPage.getTeamClearButton().setDisable(true);
    }
    
    public void projectPageStudentTableHandleClear() {
        ProjectDataPage projectPage = ((CSGWorkspace) app.getWorkspaceComponent()).getProjectDataPage();
        projectPage.getFirstNameTextField().setText("");
        projectPage.getLastNameTextField().setText("");
        projectPage.getTeamComboBox().setValue(null);
        projectPage.getRoleTextField().setText("");
        projectPage.getStudentAddUpdateButton().setOnAction(e -> {
            handleAddStudent();
        });
        projectPage.getStudentClearButton().setDisable(true);
    }
    
    /**
     * This function provides a response for when the user presses a keyboard
     * key. Note that we're only responding to Delete, to remove a TA.
     *
     * @param code The keyboard code pressed.
     */
    public void handleKeyPress(KeyCode code) {
        // DID THE USER PRESS THE DELETE KEY?
        if (code == KeyCode.DELETE) {
            handleDeleteTA();
        }
    }

    public void handleDeleteTA() {
        TADataPage taDataPage = ((CSGWorkspace) app.getWorkspaceComponent()).getTADataPage();
        TableView taTable = taDataPage.getTATable();

        // IS A TA SELECTED IN THE TABLE?
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {

            // GET THE TA AND REMOVE IT
            TeachingAssistant ta = (TeachingAssistant) selectedItem;
            DeleteTA_Transaction deleteTA = new DeleteTA_Transaction(app, ta.getName(), ta.getEmail());
            jtps.addTransaction(deleteTA);
        }
    }

    /**
     * This function provides a response for when the user clicks on the office
     * hours grid to add or remove a TA to a time slot.
     *
     * @param pane The pane that was toggled.
     */
    public void handleCellToggle(Pane pane) {
        // GET THE TABLE
        TADataPage taDataPage = ((CSGWorkspace) app.getWorkspaceComponent()).getTADataPage();
        TableView taTable = taDataPage.getTATable();

        // IS A TA SELECTED IN THE TABLE?
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // GET THE TA
            TeachingAssistant ta = (TeachingAssistant) selectedItem;
            String taName = ta.getName();
            String cellKey = pane.getId();

            ToggleTAOfficeHour_Transaction toggleTAOfficeHour = new ToggleTAOfficeHour_Transaction(app, cellKey, taName);
            jtps.addTransaction(toggleTAOfficeHour);
        }
    }

    public void handleGridCellMouseExited(Pane pane) {
        String cellKey = pane.getId();
        TAData data = ((CSGeneratorData) app.getDataComponent()).getTAData();
        int column = Integer.parseInt(cellKey.substring(0, cellKey.indexOf("_")));
        int row = Integer.parseInt(cellKey.substring(cellKey.indexOf("_") + 1));
        TADataPage taDataPage = ((CSGWorkspace) app.getWorkspaceComponent()).getTADataPage();

        Pane mousedOverPane = taDataPage.getTACellPane(data.getCellKey(column, row));
        mousedOverPane.getStyleClass().clear();
        mousedOverPane.getStyleClass().add(CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE);

        // THE MOUSED OVER COLUMN HEADER
        Pane headerPane = taDataPage.getOfficeHoursGridDayHeaderPanes().get(data.getCellKey(column, 0));
        headerPane.getStyleClass().remove(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);

        // THE MOUSED OVER ROW HEADERS
        headerPane = taDataPage.getOfficeHoursGridTimeCellPanes().get(data.getCellKey(0, row));
        headerPane.getStyleClass().remove(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        headerPane = taDataPage.getOfficeHoursGridTimeCellPanes().get(data.getCellKey(1, row));
        headerPane.getStyleClass().remove(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);

        // AND NOW UPDATE ALL THE CELLS IN THE SAME ROW TO THE LEFT
        for (int i = 2; i < column; i++) {
            cellKey = data.getCellKey(i, row);
            Pane cell = taDataPage.getTACellPane(cellKey);
            cell.getStyleClass().remove(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
            cell.getStyleClass().add(CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE);
        }

        // AND THE CELLS IN THE SAME COLUMN ABOVE
        for (int i = 1; i < row; i++) {
            cellKey = data.getCellKey(column, i);
            Pane cell = taDataPage.getTACellPane(cellKey);
            cell.getStyleClass().remove(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
            cell.getStyleClass().add(CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE);
        }
    }

    public void handleGridCellMouseEntered(Pane pane) {
        String cellKey = pane.getId();
        TAData data = ((CSGeneratorData) app.getDataComponent()).getTAData();
        int column = Integer.parseInt(cellKey.substring(0, cellKey.indexOf("_")));
        int row = Integer.parseInt(cellKey.substring(cellKey.indexOf("_") + 1));
        TADataPage taDataPage = ((CSGWorkspace) app.getWorkspaceComponent()).getTADataPage();

        // THE MOUSED OVER PANE
        Pane mousedOverPane = taDataPage.getTACellPane(data.getCellKey(column, row));
        mousedOverPane.getStyleClass().clear();
        mousedOverPane.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_CELL);

        // THE MOUSED OVER COLUMN HEADER
        Pane headerPane = taDataPage.getOfficeHoursGridDayHeaderPanes().get(data.getCellKey(column, 0));
        headerPane.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);

        // THE MOUSED OVER ROW HEADERS
        headerPane = taDataPage.getOfficeHoursGridTimeCellPanes().get(data.getCellKey(0, row));
        headerPane.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        headerPane = taDataPage.getOfficeHoursGridTimeCellPanes().get(data.getCellKey(1, row));
        headerPane.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);

        // AND NOW UPDATE ALL THE CELLS IN THE SAME ROW TO THE LEFT
        for (int i = 2; i < column; i++) {
            cellKey = data.getCellKey(i, row);
            Pane cell = taDataPage.getTACellPane(cellKey);
            cell.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        }

        // AND THE CELLS IN THE SAME COLUMN ABOVE
        for (int i = 1; i < row; i++) {
            cellKey = data.getCellKey(column, i);
            Pane cell = taDataPage.getTACellPane(cellKey);
            cell.getStyleClass().add(CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN);
        }
    }

    public void handleStartTimeChange() {
        TAData data = ((CSGeneratorData) app.getDataComponent()).getTAData();
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        TADataPage taDataPage = workspace.getTADataPage();
        CSGController controller = workspace.getCSGController();
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        String newStartTime = taDataPage.getStartTimeBox().getSelectionModel().getSelectedItem().toString();
        String endTime = taDataPage.getEndTimeBox().getSelectionModel().getSelectedItem().toString();
        int oldStartHour = data.getStartHour();
        String endHour = controller.getHourFrom(endTime, false);
        String newStartHour = controller.getHourFrom(newStartTime, true);

        if (!newStartTime.equals("12:00am") && !endTime.equals("12:00am")) {
            //SELECTED START TIME IS LATTER THAN THE END TIME
            if (Integer.parseInt(newStartHour) >= Integer.parseInt(endHour)) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(TIME_SLOT_ERROR_TITLE), props.getProperty(TIME_SLOT_ERROR_MESSAGE));
                return;
            }
        }
        boolean TAWillBeDeleted = false;
        for (int row = 1; row <= (Integer.parseInt(newStartHour) - oldStartHour) * 2; row++) {
            for (int col = 2; col < 7; col++) {
                String cellKey = data.getCellKey(col, row);
                if (!data.getOfficeHours().get(cellKey).getValue().equals("")) {
                    TAWillBeDeleted = true;
                    break;
                }
            }
            if (TAWillBeDeleted) {
                break;
            }
        }
        if (TAWillBeDeleted) {
            AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
            yesNoDialog.show(props.getProperty(TIMESLOT_VERIFICATION_TITLE), props.getProperty(TIMESLOT_VERIFICATION_MESSAGE));

            String selection = yesNoDialog.getSelection();

            if (selection.equals(AppYesNoCancelDialogSingleton.YES)) {

                Update_Time_Transaction update_Time_Transaction = new Update_Time_Transaction(Integer.valueOf(newStartHour), Integer.valueOf(endHour), data);
                jtps.addTransaction(update_Time_Transaction);
                markWorkAsEdited();

            }
        } //NO TA WILL BE DELETED
        if (!TAWillBeDeleted) {

            Update_Time_Transaction update_Time_Transaction = new Update_Time_Transaction(Integer.valueOf(newStartHour), Integer.valueOf(endHour), data);
            jtps.addTransaction(update_Time_Transaction);
            markWorkAsEdited();
        }
    }

    public void handleEndTimeChange() {
        TAData data = ((CSGeneratorData) app.getDataComponent()).getTAData();
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        TADataPage taDataPage = workspace.getTADataPage();
        CSGController controller = workspace.getCSGController();
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        String startTime = taDataPage.getStartTimeBox().getSelectionModel().getSelectedItem().toString();
        String newEndTime = taDataPage.getEndTimeBox().getSelectionModel().getSelectedItem().toString();
        int oldEndHour = data.getEndHour();
        String newEndHour = controller.getHourFrom(newEndTime, false);
        String startHour = controller.getHourFrom(startTime, true);

        if (!startTime.equals("12:00am") && !newEndTime.equals("12:00am")) {
            //SELECTED END TIME IS EARLIER THAN THE START TIME
            if (Integer.parseInt(startHour) >= Integer.parseInt(newEndHour)) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
                dialog.show(props.getProperty(TIME_SLOT_ERROR_TITLE), props.getProperty(TIME_SLOT_ERROR_MESSAGE));
                return;
            }
        }
        boolean TAWillBeDeleted = false;
        int startRow = (Integer.parseInt(newEndHour) - data.getStartHour()) * 2 + 1;
        int endRow = (oldEndHour - data.getStartHour()) * 2;
        for (int row = startRow; row <= endRow; row++) {
            for (int col = 2; col < 7; col++) {
                String cellKey = data.getCellKey(col, row);
                if (!data.getOfficeHours().get(cellKey).getValue().equals("")) {
                    TAWillBeDeleted = true;
                    break;
                }
            }
            if (TAWillBeDeleted) {
                break;
            }
        }
        if (TAWillBeDeleted) {
            AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
            yesNoDialog.show(props.getProperty(TIMESLOT_VERIFICATION_TITLE), props.getProperty(TIMESLOT_VERIFICATION_MESSAGE));

            String selection = yesNoDialog.getSelection();

            if (selection.equals(AppYesNoCancelDialogSingleton.YES)) {
                Update_Time_Transaction update_Time_Transaction = new Update_Time_Transaction(Integer.valueOf(startHour), Integer.valueOf(newEndHour), data);
                jtps.addTransaction(update_Time_Transaction);
                markWorkAsEdited();
            }
        } //NO TA WILL BE DELETED
        else {
            Update_Time_Transaction update_Time_Transaction = new Update_Time_Transaction(Integer.valueOf(startHour), Integer.valueOf(newEndHour), data);
            jtps.addTransaction(update_Time_Transaction);
            markWorkAsEdited();
        }

    }

    public String getHourFrom(String time, boolean isStartHour) {
        String hour = time.substring(0, time.indexOf(":"));
        if (time.substring(time.length() - 2).equals("pm") && !hour.equals("12")) {
            hour = String.valueOf(Integer.parseInt(hour) + 12);
        } else if (time.substring(time.length() - 2).equals("am") && hour.equals("12")) {
            if (isStartHour) {
                hour = "0";
            } else {
                hour = "24";
            }
        }
        return hour;
    }

    public void handleAddRecitation() {
        RecitationDataPage recDataPage = ((CSGWorkspace) app.getWorkspaceComponent()).getRecitationDataPage();
        RecitationData recData = ((CSGeneratorData) app.getDataComponent()).getRecitationData();
        TextField sectionField = recDataPage.getSectionTextField();
        TextField instructorField = recDataPage.getInstructorTextField();
        TextField dayTimeField = recDataPage.getDayTimeTextField();
        TextField locationField = recDataPage.getLocationTextField();
        ComboBox ta1ComboBox = recDataPage.getTA1ComboBox();
        ComboBox ta2ComboBox = recDataPage.getTA2ComboBox();

        String section = sectionField.getText();
        String instructor = instructorField.getText();
        String dayTime = dayTimeField.getText();
        String location = locationField.getText();
        String ta1 = "";
        String ta2 = "";
        if (ta1ComboBox.getSelectionModel().getSelectedItem() != null) {
            ta1 = ta1ComboBox.getSelectionModel().getSelectedItem().toString();
        }
        if (ta2ComboBox.getSelectionModel().getSelectedItem() != null) {
            ta2 = ta2ComboBox.getSelectionModel().getSelectedItem().toString();
        }

        PropertiesManager props = PropertiesManager.getPropertiesManager();

        if (section.isEmpty()) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(MISSING_SECTION_TITLE), props.getProperty(MISSING_SECTION_MESSAGE));
        } else if (instructor.isEmpty()) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(MISSING_INSTRUCTOR_TITLE), props.getProperty(MISSING_INSTRUCTOR_MESSAGE));
        } else if (dayTime.isEmpty()) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(MISSING_DAY_TIME_TITLE), props.getProperty(MISSING_DAY_TIME_MESSAGE));
        } else if (location.isEmpty()) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(MISSING_LOCATION_TITLE), props.getProperty(MISSING_LOCATION_MESSAGE));
        } else if (recData.containsRecitation(section)) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(RECITATION_NOT_UNIQUE_TITLE), props.getProperty(RECITATION_NOT_UNIQUE_MESSAGE));
        } // EVERYTHING IS FINE, ADD A NEW TA
        else {
            AddRecitation_Transaction addRec_Transaction = new AddRecitation_Transaction(app, section,
                    instructor, dayTime, location, ta1, ta2);
            jtps.addTransaction(addRec_Transaction);
        }
        sectionField.setText("");
        instructorField.setText("");
        dayTimeField.setText("");
        locationField.setText("");
        ta1ComboBox.getSelectionModel().clearSelection();
        ta2ComboBox.getSelectionModel().clearSelection();

        sectionField.requestFocus();
    }

    public void handleUpdateRecitation() {
        RecitationDataPage recDataPage = ((CSGWorkspace) app.getWorkspaceComponent()).getRecitationDataPage();
        RecitationData recData = ((CSGeneratorData) app.getDataComponent()).getRecitationData();

        TextField sectionField = recDataPage.getSectionTextField();
        TextField instructorField = recDataPage.getInstructorTextField();
        TextField dayTimeField = recDataPage.getDayTimeTextField();
        TextField locationField = recDataPage.getLocationTextField();
        ComboBox ta1ComboBox = recDataPage.getTA1ComboBox();
        ComboBox ta2ComboBox = recDataPage.getTA2ComboBox();

        TableView recTable = recDataPage.getRecTable();
        Recitation recitation = (Recitation) recTable.getSelectionModel().getSelectedItem();
        String oldSection = recitation.getSection();
        String oldInstructor = recitation.getInstructor();
        String oldDayTime = recitation.getDayTime();
        String oldLocation = recitation.getLocation();
        String oldTA1 = recitation.getTa1();
        String oldTA2 = recitation.getTa2();

        String newSection = sectionField.getText();
        String newInstructor = instructorField.getText();
        String newDayTime = dayTimeField.getText();
        String newLocation = locationField.getText();
        String newTA1 = ta1ComboBox.getSelectionModel().getSelectedItem().toString();
        String newTA2 = ta2ComboBox.getSelectionModel().getSelectedItem().toString();

        PropertiesManager props = PropertiesManager.getPropertiesManager();

        if (newSection.isEmpty()) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(MISSING_SECTION_TITLE), props.getProperty(MISSING_SECTION_MESSAGE));
        } else if (newInstructor.isEmpty()) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(MISSING_INSTRUCTOR_TITLE), props.getProperty(MISSING_INSTRUCTOR_MESSAGE));
        } else if (newDayTime.isEmpty()) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(MISSING_DAY_TIME_TITLE), props.getProperty(MISSING_DAY_TIME_MESSAGE));
        } else if (newLocation.isEmpty()) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(MISSING_LOCATION_TITLE), props.getProperty(MISSING_LOCATION_MESSAGE));
        } else if (recData.containsRecitationExclude(recitation, newSection)) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(RECITATION_NOT_UNIQUE_TITLE), props.getProperty(RECITATION_NOT_UNIQUE_MESSAGE));
        } else {
            UpdateRecitation_Transaction updateRec = new UpdateRecitation_Transaction(app, oldSection, oldInstructor, oldDayTime, oldLocation,
                    oldTA1, oldTA2, newSection, newInstructor, newDayTime, newLocation, newTA1, newTA2);
            jtps.addTransaction(updateRec);            
        }
        recPageHandleClear();
    }
    
    public void handleUpdateSchedule() {
        ScheduleDataPage schedulePage = ((CSGWorkspace) app.getWorkspaceComponent()).getScheduleDataPage();
        ComboBox typeComboBox = schedulePage.getTypeComboBox();
        DatePicker datePicker = schedulePage.getDatePicker();
        TextField timeTextField = schedulePage.getTimeTextField();
        TextField titleTextField = schedulePage.getTitleTextField();
        TextField topicTextField = schedulePage.getTopicTextField();
        TextField linkTextField = schedulePage.getLinkTextField();
        TextField criteriaTextField = schedulePage.getCriteriaTextField();
        
        TableView scheduTable = schedulePage.getScheduleTable();
        ScheduleItem selectedItem =(ScheduleItem) scheduTable.getSelectionModel().getSelectedItem();
        String oldType = selectedItem.getType();
        String oldDate = selectedItem.getDate();
        String oldTime = selectedItem.getTime();
        String oldTitle = selectedItem.getTitle();
        String oldTopic = selectedItem.getTopic();
        String oldLink = selectedItem.getLink();
        String oldCriteria = selectedItem.getCriteria();
        String newType = typeComboBox.getSelectionModel().getSelectedItem().toString();
        String newDate = datePicker.getValue().toString();
        String newTime = timeTextField.getText();
        String newTitle = titleTextField.getText();
        String newTopic = topicTextField.getText();
        String newLink = linkTextField.getText();
        String newCriteria = criteriaTextField.getText();
        UpdateSchedule_Transaction updateSchedule = new UpdateSchedule_Transaction(app, oldType, oldDate, oldTime, oldTitle,
                oldTopic, oldLink, oldCriteria, newType, newDate, newTime, newTitle, newTopic, newLink, newCriteria);
        jtps.addTransaction(updateSchedule);
        schedulePageHandleClear();
    }
    
    public void handleUpdateTeam() {
        ProjectDataPage projectPage = ((CSGWorkspace) app.getWorkspaceComponent()).getProjectDataPage();
        TableView teamsTable = projectPage.getTeamsTable();
        Object selectedItem = teamsTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Team team = (Team)selectedItem;
            String oldName = team.getName();
            String oldColor = team.getColor();
            String oldTextColor = team.getTextColor();
            String oldLink = team.getLink();
            
            TextField nameTextField = projectPage.getNameTextField();
            ColorPicker colorPicker = projectPage.getColorPicker();
            ColorPicker textColorPicker = projectPage.getTextColorPicker();
            TextField linkTextField = projectPage.getLinkTextField();
            
            String newName = nameTextField.getText();
            String newColor = colorPicker.getValue().toString().substring(2, 8);            
            String newTextColor = textColorPicker.getValue().toString().substring(2, 8);
            String newlink = linkTextField.getText();
            
            UpdateTeam_Transaction updateTeam = new UpdateTeam_Transaction(app, oldName, oldColor, oldTextColor, oldLink,
                    newName, newColor, newTextColor, newlink);
            jtps.addTransaction(updateTeam);
            
            projectPageTeamsTableHandleClear();
        }
    }
    
    public void handleUpdateStudent() {
        ProjectDataPage projectPage = ((CSGWorkspace)app.getWorkspaceComponent()).getProjectDataPage();
        TableView studentTable = projectPage.getStudentsTable();
        Object selectedItem = studentTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Student student = (Student) selectedItem;
            String oldFirstName = student.getFirstName();
            String oldLastName = student.getLastName();
            String oldTeamName = student.getTeamName();
            String oldRole = student.getRole();
            TextField firstNameTextField = projectPage.getFirstNameTextField();
            TextField lastNameTextField = projectPage.getLastNameTextField();
            ComboBox teamsComboBox = projectPage.getTeamComboBox();
            TextField roleTextField = projectPage.getRoleTextField();
            String newFirstName = firstNameTextField.getText();
            String newLastName = lastNameTextField.getText();
            String newTeamName = teamsComboBox.getSelectionModel().getSelectedItem().toString();
            String newRole = roleTextField.getText();
            
            UpdateStudent_Transaction updateStudent = new UpdateStudent_Transaction(app, oldFirstName, oldLastName, oldTeamName, oldRole,
                    newFirstName, newLastName, newTeamName, newRole);
            jtps.addTransaction(updateStudent);
            
            projectPageStudentTableHandleClear();
            
        }
        
    }

    public void handleDeleteRecitation() {
        RecitationDataPage recPage = ((CSGWorkspace) app.getWorkspaceComponent()).getRecitationDataPage();
        TableView recTable = recPage.getRecTable();
        Object selectedItem = recTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // GET THE TA AND REMOVE IT
            Recitation rec = (Recitation) selectedItem;
            String section = rec.getSection();
            String instructor = rec.getInstructor();
            String dayTime = rec.getDayTime();
            String location = rec.getLocation();
            DeleteRecitation_Transaction deleteRecitation = new DeleteRecitation_Transaction(app, section, instructor,
                    dayTime, location, dayTime, dayTime);
            jtps.addTransaction(deleteRecitation);
            
            recPage.getSectionTextField().setText("");
            recPage.getInstructorTextField().setText("");
            recPage.getDayTimeTextField().setText("");
            recPage.getLocationTextField().setText("");
            recPage.getTA1ComboBox().setValue(null);
            recPage.getTA2ComboBox().setValue(null);
            recPage.getAddUpdateButton().setOnAction(e -> {
                handleAddRecitation();
            });
            recPage.getClearButton().setDisable(true);
        }
    }

    public void handleDeleteSchedule() {
        ScheduleDataPage schedulePage = ((CSGWorkspace) app.getWorkspaceComponent()).getScheduleDataPage();
        TableView scheduleTable = schedulePage.getScheduleTable();
        Object selectedItem = scheduleTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            ScheduleItem item = (ScheduleItem) selectedItem;
            String type = item.getType();
            String date = item.getDate();
            String time = item.getTime();
            String title = item.getTitle();
            String topic = item.getTopic();
            String link = item.getLink();
            String criteria = item.getCriteria();

            DeleteSchedule_Transaction deleteSchedule = new DeleteSchedule_Transaction(app, type, date, time, title, topic, link, criteria);
            jtps.addTransaction(deleteSchedule);

            schedulePage.getTypeComboBox().getSelectionModel().clearSelection();
            schedulePage.getDatePicker().setValue(null);
            schedulePage.getTimeTextField().setText("");
            schedulePage.getTitleTextField().setText("");
            schedulePage.getTopicTextField().setText("");
            schedulePage.getLinkTextField().setText("");
            schedulePage.getCriteriaTextField().setText("");
            schedulePage.getAddUpdateButton().setOnAction(e -> {
                handleAddSchedule();
            });
            schedulePage.getClearButton().setDisable(true);
        }
    }
    
    public void handleDeleteTeam() {
        ProjectDataPage projectPage = ((CSGWorkspace)app.getWorkspaceComponent()).getProjectDataPage();
        TableView teamsTable = projectPage.getTeamsTable();
        Object selectedItem = teamsTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Team team = (Team) selectedItem;
            String name = team.getName();
            String color = team.getColor();
            String textColor = team.getTextColor();
            String link = team.getLink();
            
            DeleteTeam_Transaction deleteTeam = new DeleteTeam_Transaction(app, name, color, textColor, link);
            jtps.addTransaction(deleteTeam);
            
            projectPage.getNameTextField().setText("");
            projectPage.getColorPicker().setValue(null);
            projectPage.getTextColorPicker().setValue(null);
            projectPage.getLinkTextField().setText("");
            projectPage.getTeamAddUpdateButton().setOnAction(e -> {
                handleAddTeam();
            });
            projectPage.getTeamClearButton().setDisable(true);
        }
    }
    
    public void handleDeleteStudent() {
        ProjectDataPage projectPage = ((CSGWorkspace)app.getWorkspaceComponent()).getProjectDataPage();
        TableView studentsTable = projectPage.getStudentsTable();
        Object selectedItem = studentsTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            Student student = (Student) selectedItem;
            String firstName = student.getFirstName();
            String lastName = student.getLastName();
            String teamName = student.getTeamName();
            String role = student.getRole();
            
            DeleteStudent_Transaction deleteStudent = new DeleteStudent_Transaction(app, firstName, lastName, teamName, role);
            jtps.addTransaction(deleteStudent);
            
            projectPage.getFirstNameTextField().setText("");
            projectPage.getLastNameTextField().setText("");
            projectPage.getTeamComboBox().setValue(null);
            projectPage.getRoleTextField().setText("");
            projectPage.getStudentClearButton().setDisable(true);
            projectPage.getStudentAddUpdateButton().setOnAction(e -> {
                handleAddStudent();
            });
            projectPage.getStudentClearButton().setDisable(true);
        }
    }

    public void handleAddSchedule() {
        ScheduleDataPage schedulePage = ((CSGWorkspace) app.getWorkspaceComponent()).getScheduleDataPage();
        ComboBox typeComboBox = schedulePage.getTypeComboBox();
        DatePicker datePicker = schedulePage.getDatePicker();
        TextField timeTextField = schedulePage.getTimeTextField();
        TextField titleTextField = schedulePage.getTitleTextField();
        TextField topicTextField = schedulePage.getTopicTextField();
        TextField linkTextField = schedulePage.getLinkTextField();
        TextField criteriaTextField = schedulePage.getCriteriaTextField();
        String type = typeComboBox.getSelectionModel().getSelectedItem().toString();
        String date = datePicker.getValue().toString();
        String time = timeTextField.getText();
        String title = titleTextField.getText();
        String topic = topicTextField.getText();
        String link = linkTextField.getText();
        String criteria = criteriaTextField.getText();

        AddSchedule_Transaction addSchedule = new AddSchedule_Transaction(app, type, date, time,
                title, topic, link, criteria);
        jtps.addTransaction(addSchedule);

        schedulePageHandleClear();               
    }
    
    public void handleAddTeam() {
        ProjectDataPage projectPage = ((CSGWorkspace)app.getWorkspaceComponent()).getProjectDataPage();
        TextField nameField = projectPage.getNameTextField();
        ColorPicker colorPicker = projectPage.getColorPicker();
        ColorPicker textColorPicker = projectPage.getTextColorPicker();
        TextField linkField = projectPage.getLinkTextField();
        String name = nameField.getText();       
        String color = colorPicker.getValue().toString().substring(2,8);
        String textColor =textColorPicker.getValue().toString().substring(2,8);
        String link = linkField.getText();

        AddTeam_Transaction addTeam = new AddTeam_Transaction(app, name, color, textColor, link);
        jtps.addTransaction(addTeam);
        
        projectPageTeamsTableHandleClear();
        
        nameField.requestFocus();
    }
    
    public void handleAddStudent() {
        ProjectDataPage projectPage = ((CSGWorkspace)app.getWorkspaceComponent()).getProjectDataPage();
        String firstName = projectPage.getFirstNameTextField().getText();
        String lastName = projectPage.getLastNameTextField().getText();
        String teamName = projectPage.getTeamComboBox().getSelectionModel().getSelectedItem().toString();
        String role = projectPage.getRoleTextField().getText();
        
        AddStudent_Transaction addStudent = new AddStudent_Transaction(app, firstName, lastName, teamName, role);
        jtps.addTransaction(addStudent);
        
        projectPageStudentTableHandleClear();
        
        projectPage.getFirstNameTextField().requestFocus();
    }
    
   
    
    public void handleBannerChange() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_IMAGES));
        fc.setTitle(props.getProperty(CHOOSE_IMAGE_TITLE));
        fc.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter(String.format("%s (*.%s) (*.%s)", props.getProperty(IMAGE_FILE_EXT_DESC),
                    props.getProperty(PNG_FILE_EXT), props.getProperty(JPG_FILE_EXT)), String.format("*.%s", props.getProperty(PNG_FILE_EXT)), String.format("*.%s",props.getProperty(JPG_FILE_EXT))));
        File selectedFile = fc.showOpenDialog(app.getGUI().getWindow());

        if (selectedFile != null) {
            try {
                CourseDetailPage courseDetailPage = ((CSGWorkspace)app.getWorkspaceComponent()).getCourseDetailPage();
                CourseData courseData = ((CSGeneratorData)app.getDataComponent()).getCourseData();
                courseDetailPage.getBannerImageBox().getChildren().clear();
                courseData.setBannerImagePath(selectedFile.toURI().toURL().toString());
                courseDetailPage.loadBannerImage();
                markWorkAsEdited();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    public void handleLeftFooterImageChange() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_IMAGES));
        fc.setTitle(props.getProperty(CHOOSE_IMAGE_TITLE));
        fc.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter(String.format("%s (*.%s) (*.%s)", props.getProperty(IMAGE_FILE_EXT_DESC),
                    props.getProperty(PNG_FILE_EXT), props.getProperty(JPG_FILE_EXT)), String.format("*.%s", props.getProperty(PNG_FILE_EXT)), String.format("*.%s",props.getProperty(JPG_FILE_EXT))));
        File selectedFile = fc.showOpenDialog(app.getGUI().getWindow());

        if (selectedFile != null) {
            try {
                CourseDetailPage courseDetailPage = ((CSGWorkspace) app.getWorkspaceComponent()).getCourseDetailPage();
                CourseData courseData = ((CSGeneratorData) app.getDataComponent()).getCourseData();
                courseDetailPage.getLeftFooterImageBox().getChildren().clear();
                courseData.setLeftFooterImagePath(selectedFile.toURI().toURL().toString());
                courseDetailPage.loadLeftFooterImage();
                markWorkAsEdited();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleRightFooterImageChange() {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_IMAGES));
        fc.setTitle(props.getProperty(CHOOSE_IMAGE_TITLE));
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(String.format("%s (*.%s) (*.%s)", props.getProperty(IMAGE_FILE_EXT_DESC),
                        props.getProperty(PNG_FILE_EXT), props.getProperty(JPG_FILE_EXT)), String.format("*.%s", props.getProperty(PNG_FILE_EXT)), String.format("*.%s", props.getProperty(JPG_FILE_EXT))));
        File selectedFile = fc.showOpenDialog(app.getGUI().getWindow());

        if (selectedFile != null) {
            try {
                CourseDetailPage courseDetailPage = ((CSGWorkspace) app.getWorkspaceComponent()).getCourseDetailPage();
                CourseData courseData = ((CSGeneratorData) app.getDataComponent()).getCourseData();
                courseDetailPage.getRightFooterImageBox().getChildren().clear();
                courseData.setRightFooterImagePath(selectedFile.toURI().toURL().toString());
                courseDetailPage.loadRightFooterImage();
                markWorkAsEdited();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void handleExportDirChange() {
        CourseData courseData = ((CSGeneratorData)app.getDataComponent()).getCourseData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(CourseData.EXPORT_INITIAL_DIR));
        dc.setTitle(props.getProperty(CHOOSE_EXPORT_DIR_TITLE));
        File selectedFile = dc.showDialog(app.getGUI().getWindow());
        if (selectedFile != null) {
            courseData.setExportDir(selectedFile.getPath());
        }
        markWorkAsEdited();
    }
    
    public void handleTemplateDirChange() {
        CourseData courseData = ((CSGeneratorData)app.getDataComponent()).getCourseData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(CourseData.TEMPLATE_INITAL_DIR));
        dc.setTitle(props.getProperty(CHOOSE_TEMPLATE_DIR_TITLE));
        File selectedFile = dc.showDialog(app.getGUI().getWindow());
        if (selectedFile != null) {
            courseData.setSiteTempDir(selectedFile.getPath());
            courseData.getSitePages().clear();
            findTemplate(selectedFile);
        }
        markWorkAsEdited();
    } 
    
    public void handleStylesheetChange() {
        CourseDetailPage courseDetailPage = ((CSGWorkspace) app.getWorkspaceComponent()).getCourseDetailPage();
        CourseData courseData = ((CSGeneratorData)app.getDataComponent()).getCourseData();
        String stylsheet = courseDetailPage.getStylesheetComboBox().getSelectionModel().getSelectedItem().toString();
        courseData.setStylesheet(stylsheet);
        markWorkAsEdited();
    }

    public void findTemplate(File selectedFile) {
        CourseData courseData = ((CSGeneratorData) app.getDataComponent()).getCourseData();
        File[] files = selectedFile.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                findTemplate(file);
            } else if (file.getName().equalsIgnoreCase(INDEX_HTML)) {
                courseData.addHomePage(true);
            } else if (file.getName().equalsIgnoreCase(SYLLABUS_HTML)) {
                courseData.addSyllabusPage(true);
            } else if (file.getName().equalsIgnoreCase(SCHEDULE_HTML)) {
                courseData.addSchedulePage(true);
            } else if (file.getName().equalsIgnoreCase(HWS_HTML)) {
                courseData.addHWsPage(true);
            } else if (file.getName().equalsIgnoreCase(PROJECTS_HTML)) {
                courseData.addProjectsPage(true);
            }
        }
    }   
    
    
}