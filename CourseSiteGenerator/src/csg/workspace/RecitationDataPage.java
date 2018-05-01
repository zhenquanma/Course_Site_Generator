/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import af.components.AppDataComponent;
import csg.CSGeneratorApp;
import static csg.CSGeneratorProp.*;
import csg.controller.CSGController;
import csg.data.CSGeneratorData;
import csg.data.RecitationData;
import csg.data.TAData;
import csg.utilities.Recitation;
import csg.utilities.TeachingAssistant;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
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
public class RecitationDataPage implements Page{
    
    CSGeneratorApp app;
    CSGController controller;
    
    ScrollPane basePane;
    VBox backgroundBox;
    
    HBox recitationHeaderBox;
    Label recitationHeaderLabel;
    Button deleteButton;
    
    TableView<Recitation> recitationTable;
    TableColumn<Recitation, String> sectionColumn;
    TableColumn<Recitation, String> instructorColumn;
    TableColumn<Recitation, String> dayTimeColumn;
    TableColumn<Recitation, String> locationColumn;
    TableColumn<Recitation, String> ta1Column;
    TableColumn<Recitation, String> ta2Column;
    
    VBox addEditPane;
    GridPane addEditGridPane;
    HBox addEditHeaderBox;
    Label addEditHeaderLabel;
    Label sectionLabel;
    Label instructorLabel;
    Label dayAndTimeLabel;
    Label locationLabel;
    Label supervisingTA1Label;
    Label supervisingTA2Label;
    TextField sectionTextField;
    TextField instructorTextField;
    TextField dayAndTimeTextField;
    TextField locationTextField;
    ComboBox<TeachingAssistant> ta1ComboBox;
    ComboBox<TeachingAssistant> ta2ComboBox;
    Button addUpdateButton;
    Button clearButton;

    public RecitationDataPage(CSGeneratorApp initApp) {
        app = initApp;
        RecitationData recData =((CSGeneratorData)app.getDataComponent()).getRecitationData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        recitationHeaderBox = new HBox();
        recitationHeaderLabel = new Label(props.getProperty(RECITATION_HEARDER_TEXT));
        deleteButton = new Button();
        deleteButton.setText("-");
        recitationHeaderBox.getChildren().addAll(recitationHeaderLabel, deleteButton);
        
        //TABLE BLOCK
        recitationTable = new TableView<>();
        recitationTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<Recitation> recitationsTableData = recData.getRecitations();
        recitationTable.setItems(recitationsTableData);
        sectionColumn = new TableColumn(props.getProperty(SECTION_TEXT));
        instructorColumn = new TableColumn(props.getProperty(INSTRUCTOR_TEXT));
        dayTimeColumn = new TableColumn(props.getProperty(DAY_TIME_TEXT));
        locationColumn = new TableColumn(props.getProperty(LOCATION_TEXT));
        ta1Column = new TableColumn(props.getProperty(TA1_TEXT));
        ta2Column = new TableColumn(props.getProperty(TA2_TEXT));
        
        sectionColumn.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("section")
        );
        instructorColumn.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("instructor")
        );
        dayTimeColumn.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("dayTime")
        );
        locationColumn.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("location")
        );
        ta1Column.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("ta1")
        );
        ta2Column.setCellValueFactory(
                new PropertyValueFactory<Recitation, String>("ta2")
        );
        
        recitationTable.getColumns().add(sectionColumn);
        recitationTable.getColumns().add(instructorColumn);
        recitationTable.getColumns().add(dayTimeColumn);
        recitationTable.getColumns().add(locationColumn);
        recitationTable.getColumns().add(ta1Column);
        recitationTable.getColumns().add(ta2Column);
        
        
        //ADD/EDIT BLOCK
        addEditPane = new VBox();
        addEditHeaderBox = new HBox();
        addEditHeaderLabel = new Label(props.getProperty(ADD_EDIT_HEADER_TEXT));
        addEditHeaderBox.getChildren().add(addEditHeaderLabel);
        addEditGridPane = new GridPane();
        sectionLabel = new Label(props.getProperty(SECTION_TEXT));
        instructorLabel = new Label(props.getProperty(INSTRUCTOR_TEXT));
        dayAndTimeLabel = new Label(props.getProperty(DAY_TIME_TEXT));
        locationLabel = new Label(props.getProperty(LOCATION_TEXT));
        supervisingTA1Label = new Label(props.getProperty(SUPERVISING_TA_TEXT));
        supervisingTA2Label = new Label(props.getProperty(SUPERVISING_TA_TEXT));
        
        sectionTextField = new TextField();
        instructorTextField = new TextField();
        dayAndTimeTextField = new TextField();
        locationTextField = new TextField();
        ta1ComboBox = new ComboBox<>();
        ta2ComboBox = new ComboBox<>();       
        TAData taData = ((CSGeneratorData)app.getDataComponent()).getTAData();
        ta1ComboBox.setItems(taData.getTeachingAssistants());
        ta2ComboBox.setItems(taData.getTeachingAssistants());
        
        addUpdateButton = new Button(props.getProperty(ADD_UPDATE_BUTTON_TEXT));
        clearButton = new Button(props.getProperty(CLEAR_BUTTON_TEXT));
        clearButton.setDisable(true);
        
        addEditGridPane.add(addEditHeaderLabel, 0, 0);
        addEditGridPane.addRow(1, sectionLabel, sectionTextField);
        addEditGridPane.addRow(2, instructorLabel, instructorTextField);
        addEditGridPane.addRow(3, dayAndTimeLabel, dayAndTimeTextField);
        addEditGridPane.addRow(4, locationLabel, locationTextField);
        addEditGridPane.addRow(5, supervisingTA1Label, ta1ComboBox);
        addEditGridPane.addRow(6, supervisingTA2Label, ta2ComboBox);
        addEditGridPane.addRow(7, addUpdateButton, clearButton);

        addEditPane.getChildren().addAll(addEditHeaderBox,addEditGridPane);

        
        basePane = new ScrollPane();
        backgroundBox = new VBox();
        backgroundBox.getChildren().addAll(recitationHeaderBox, recitationTable, addEditPane);
        basePane.setContent(backgroundBox);
        backgroundBox.prefWidthProperty().bind(basePane.widthProperty());
        
        controller = new CSGController(app);
        
        addUpdateButton.setOnAction(e -> {
            controller.handleAddRecitation();
        });
        
        clearButton.setOnAction(e -> {
            controller.recPageHandleClear();
        });
        
        deleteButton.setOnAction(e -> {
            controller.handleDeleteRecitation();
        });
        
        recitationTable.setFocusTraversable(true);

        recitationTable.setOnMouseClicked(e -> {
            controller.handleMouseClicked(this, e);
        });
    }
    
    public HBox getRecitaitonHeaderBox() {
        return recitationHeaderBox;
    }
    

    public VBox getAddEditBox() {
        return addEditPane;
    }

    public  Button getAddUpdateButton() {
        return addUpdateButton;
    }
    
    public Button getClearButton() {
         return clearButton;
    }
    
    public GridPane getAddEditGridPane() {
        return addEditGridPane;
    }
    
    public TextField getSectionTextField() {
        return sectionTextField;
    }
    
    public TextField getInstructorTextField() {
        return instructorTextField;
    }
    
    public TextField getDayTimeTextField() {
        return dayAndTimeTextField;
    }
    
    public TextField getLocationTextField() {
        return locationTextField;
    }
    
    public ComboBox getTA1ComboBox() {
        return ta1ComboBox;
    }
    
    public ComboBox getTA2ComboBox() {
        return ta2ComboBox;
    }
    
    public TableView getRecTable() {
        return recitationTable;
    }
    
    @Override
    public ScrollPane getPage() { return basePane; }
    
    @Override
    public void resetPage() {
        controller.recPageHandleClear();
    }
    
    @Override
    public void reloadPage(AppDataComponent data) {
        
    }
    
}
