package csg.workspace;

import af.components.AppDataComponent;
import csg.CSGeneratorApp;
import static csg.CSGeneratorProp.*;
import csg.controller.CSGController;
import csg.data.CSGeneratorData;
import csg.data.ProjectData;
import csg.utilities.Student;
import csg.utilities.Team;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class ProjectDataPage implements Page {

    CSGeneratorApp app;
    CSGController controller;
    ScrollPane basePane;
    VBox backgroundBox;

    HBox projectsHeaderBox;
    Label projectHeaderLabel;

    //UPPER BLOCK
    VBox teamsPane;
    HBox teamsHeaderBox;
    Label teamsHeaderLabel;
    Button teamDeleteButton;

    TableView<Team> teamsTable;
    TableColumn<Team, String> nameColumn;
    TableColumn<Team, String> colorColumn;
    TableColumn<Team, String> textColorColumn;
    TableColumn<Team, String> linkColumn;

    GridPane teamsAddEditPane;
    Label teamsAddEditLabel;
    Label nameLabel;
    Label colorLabel;
    Label textColorLabel;
    Label linkLabel;
    TextField nameTextField;
    TextField linkTextField;
    ColorPicker colorPicker;
    ColorPicker textColorPicker;
    Button teamAddUpdateButton;
    Button teamClearButton;

    //BUTTOM BLOCK
    VBox studentsPane;
    HBox studentsHeaderBox;
    Label studentsHeaderLabel;
    Button studentDeleteButton;

    TableView<Student> studentsTable;
    TableColumn<Student, String> firstNameColumn;
    TableColumn<Student, String> lastNameColumn;
    TableColumn<Student, String> teamColumn;
    TableColumn<Student, String> roleColumn;

    GridPane studentsAddEditPane;
    Label studentAddEditHeaderLabel;
    Label firstNameLabel;
    Label lastNameLabel;
    Label teamLabel;
    Label roleLabel;
    TextField firstNameTextField;
    TextField lastNameTextField;
    TextField roleTextField;
    ComboBox<Team> teamComboBox;
    Button studentAddUpdateButton;
    Button studentClearButton;

    public ProjectDataPage(CSGeneratorApp initApp) {
        app = initApp;
        controller = new CSGController(app);
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        ProjectData projectData = ((CSGeneratorData) app.getDataComponent()).getProjectData();
        teamsPane = new VBox();
        projectsHeaderBox = new HBox();
        projectHeaderLabel = new Label(props.getProperty(PROJECT_HEADER_TEXT));
        projectsHeaderBox.getChildren().addAll(projectHeaderLabel);
        
        teamsHeaderBox = new HBox();
        teamsHeaderLabel = new Label(props.getProperty(TEAMS_HEADER_TEXT));
        teamDeleteButton = new Button("-");
        teamsHeaderBox.getChildren().addAll(teamsHeaderLabel, teamDeleteButton);
        
        teamsTable = new TableView<>();
        teamsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<Team> teamTableData = projectData.getTeams();
        teamsTable.setItems(teamTableData);
        nameColumn = new TableColumn<>(props.getProperty(NAME_COLUMN_TEXT));
        colorColumn = new TableColumn<>(props.getProperty(COLOR_COLUMN_TEXT));
        textColorColumn = new TableColumn<>(props.getProperty(TEXT_COLOR_COLUMN_TEXT));
        linkColumn = new TableColumn<>(props.getProperty(LINK_COLUMN_TEXT));
        
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<Team,String>("name")
        );
        colorColumn.setCellValueFactory(
                new PropertyValueFactory<Team, String>("color")
        );
        textColorColumn.setCellValueFactory(
                new PropertyValueFactory<Team, String>("textColor")
        );
        linkColumn.setCellValueFactory(
                new PropertyValueFactory<Team, String>("link")
        );
               
        
        teamsTable.getColumns().addAll(nameColumn, colorColumn, textColorColumn, linkColumn);

        teamsAddEditPane = new GridPane();
        teamsAddEditLabel = new Label(props.getProperty(ADD_EDIT_HEADER_TEXT));
        nameLabel = new Label(props.getProperty(NAME_LABEL_TEXT));
        colorLabel = new Label(props.getProperty(COLOR_LABEL_TEXT));
        textColorLabel = new Label(props.getProperty(TEXT_COLOR_LABEL_TEXT));
        linkLabel = new Label(props.getProperty(LINK_LABEL_TEXT));
        nameTextField = new TextField();
        linkTextField = new TextField();
        colorPicker = new ColorPicker();
        textColorPicker = new ColorPicker();
        teamAddUpdateButton = new Button(props.getProperty(ADD_UPDATE_BUTTON_TEXT));
        teamClearButton = new Button(props.getProperty(CLEAR_BUTTON_TEXT));
        teamClearButton.setDisable(true);
        
        teamsAddEditPane.addRow(0, teamsAddEditLabel);
        teamsAddEditPane.addRow(1, nameLabel, nameTextField);
        teamsAddEditPane.addRow(2, colorLabel, colorPicker, textColorLabel, textColorPicker);
        teamsAddEditPane.addRow(3, linkLabel, linkTextField);
        teamsAddEditPane.addRow(4, teamAddUpdateButton, teamClearButton);

        teamsPane.getChildren().addAll(teamsHeaderBox, teamsTable, teamsAddEditPane);

        studentsPane = new VBox();
        studentsHeaderBox = new HBox();
        studentsHeaderLabel = new Label(props.getProperty(STUDENTS_HEADER_TEXT));
        studentDeleteButton = new Button("-");
        studentsHeaderBox.getChildren().addAll(studentsHeaderLabel, studentDeleteButton);
        
        studentsTable = new TableView<>();
        studentsTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<Student> studentTableData = projectData.getStudents();
        studentsTable.setItems(studentTableData);
        
        firstNameColumn = new TableColumn<>(props.getProperty(FIRST_NAME_COLUMN_TEXT));
        lastNameColumn = new TableColumn<>(props.getProperty(LAST_NAME_COLUMN_TEXT));
        teamColumn = new TableColumn<>(props.getProperty(TEAM_COLUMN_TEXT));
        roleColumn = new TableColumn<>(props.getProperty(ROLE_COLUMN_TEXT));
        
        firstNameColumn.setCellValueFactory(
                new PropertyValueFactory<Student, String>("firstName")
        );
        lastNameColumn.setCellValueFactory(
                new PropertyValueFactory<Student, String>("lastName")
        );
        teamColumn.setCellValueFactory(
                new PropertyValueFactory<Student, String>("teamName")
        );
        roleColumn.setCellValueFactory(
                new PropertyValueFactory<Student, String>("role")
        );
        
        studentsTable.getColumns().addAll(firstNameColumn, lastNameColumn, teamColumn, roleColumn);

        studentsAddEditPane = new GridPane();
        studentAddEditHeaderLabel = new Label(props.getProperty(ADD_EDIT_HEADER_TEXT));
        firstNameLabel = new Label(props.getProperty(FIRST_NAME_LABEL_TEXT));
        lastNameLabel = new Label(props.getProperty(LAST_NAME_LABEL_TEXT));
        teamLabel = new Label(props.getProperty(TEAM_LABEL_TEXT));
        roleLabel = new Label(props.getProperty(ROLE_LABEL_TEXT));
        firstNameTextField = new TextField();
        lastNameTextField = new TextField();
        teamComboBox = new ComboBox<>();
        teamComboBox.setItems(projectData.getTeams());
        roleTextField = new TextField();
        studentAddUpdateButton = new Button(props.getProperty(ADD_UPDATE_BUTTON_TEXT));
        studentClearButton = new Button(props.getProperty(CLEAR_BUTTON_TEXT));
        studentClearButton.setDisable(true);
            
        studentsAddEditPane.addRow(0, studentAddEditHeaderLabel);
        studentsAddEditPane.addRow(1, firstNameLabel, firstNameTextField);
        studentsAddEditPane.addRow(2, lastNameLabel, lastNameTextField);
        studentsAddEditPane.addRow(3, teamLabel, teamComboBox);
        studentsAddEditPane.addRow(4, roleLabel, roleTextField);
        studentsAddEditPane.addRow(5, studentAddUpdateButton, studentClearButton);

        studentsPane.getChildren().addAll(studentsHeaderBox, studentsTable, studentsAddEditPane);

        backgroundBox = new VBox();
        backgroundBox.getChildren().addAll(projectsHeaderBox, teamsPane, studentsPane);

        basePane = new ScrollPane();

        basePane.setContent(backgroundBox);
        backgroundBox.prefWidthProperty().bind(basePane.widthProperty());
        
        CSGController controller = new CSGController(initApp);
        
        teamAddUpdateButton.setOnAction(e -> {
            controller.handleAddTeam();
        });
        
        teamDeleteButton.setOnAction(e -> {
            controller.handleDeleteTeam();
        });
        
        teamClearButton.setOnAction(e -> {
            controller.projectPageTeamsTableHandleClear();
        });
        
        teamsTable.setOnMouseClicked(e -> {
            controller.teamsTableHandleMouseClick(e);
        });
        
        studentsTable.setOnMouseClicked(e -> {
            controller.studentsTableHandleMouseClick(e);
        });
        
        studentAddUpdateButton.setOnAction(e -> {
            controller.handleAddStudent();
        });
        
        studentDeleteButton.setOnAction(e -> {
            controller.handleDeleteStudent();
        });
        
        studentClearButton.setOnAction(e -> {
            controller.projectPageStudentTableHandleClear();
        });
        
    }
    
    public HBox getProjectHeaderBox() {
        return projectsHeaderBox;
    }
    
    public VBox getTeamsPane() {
        return teamsPane;
    }
    
    public VBox getStudentsPane() {
        return studentsPane;
    }
    
    public GridPane getTeamsAddEditPane() {
        return teamsAddEditPane;
    }
    public GridPane getStudentsAddEditPane() {
        return studentsAddEditPane;
    }

    /**
     * @return the studentDeleteButton
     */
    public Button getStudentDeleteButton() {
        return studentDeleteButton;
    }

    /**
     * @return the studentsTable
     */
    public TableView<Student> getStudentsTable() {
        return studentsTable;
    }

    /**
     * @return the firstNameTextField
     */
    public TextField getFirstNameTextField() {
        return firstNameTextField;
    }

    /**
     * @return the lastNameTextField
     */
    public TextField getLastNameTextField() {
        return lastNameTextField;
    }

    /**
     * @return the roleTextField
     */
    public TextField getRoleTextField() {
        return roleTextField;
    }

    /**
     * @return the teamComboBox
     */
    public ComboBox getTeamComboBox() {
        return teamComboBox;
    }

    /**
     * @return the studentAddUpdateButton
     */
    public Button getStudentAddUpdateButton() {
        return studentAddUpdateButton;
    }

    /**
     * @return the studentClearButton
     */
    public Button getStudentClearButton() {
        return studentClearButton;
    }

    /**
     * @return the teamDeleteButton
     */
    public Button getTeamDeleteButton() {
        return teamDeleteButton;
    }

    /**
     * @return the teamsTable
     */
    public TableView<Team> getTeamsTable() {
        return teamsTable;
    }

    /**
     * @return the nameTextField
     */
    public TextField getNameTextField() {
        return nameTextField;
    }

    /**
     * @return the linkTextField
     */
    public TextField getLinkTextField() {
        return linkTextField;
    }

    /**
     * @return the colorPicker
     */
    public ColorPicker getColorPicker() {
        return colorPicker;
    }

    /**
     * @return the textColorPicker
     */
    public ColorPicker getTextColorPicker() {
        return textColorPicker;
    }

    /**
     * @return the teamAddUpdateButton
     */
    public Button getTeamAddUpdateButton() {
        return teamAddUpdateButton;
    }

    /**
     * @return the teamClearButton
     */
    public Button getTeamClearButton() {
        return teamClearButton;
    }
    
    
    @Override
    public ScrollPane getPage() {
        return basePane;
    }

    @Override
    public void resetPage() {
        controller.projectPageTeamsTableHandleClear();
        controller.projectPageStudentTableHandleClear();
    }

    @Override
    public void reloadPage(AppDataComponent data) {
        
    }
}
