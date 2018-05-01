package csg.workspace;

import af.components.AppDataComponent;
import csg.CSGeneratorApp;
import static csg.CSGeneratorProp.*;
import csg.controller.CSGController;
import csg.data.CSGeneratorData;
import csg.data.TAData;
import csg.style.CSGStyle;
import csg.style.TAStyle;
import csg.utilities.TeachingAssistant;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import properties_manager.PropertiesManager;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>, Richard McKenna
 */
public class TADataPage implements Page {

    CSGeneratorApp app;
    CSGController controller;

    SplitPane basePane;

    // FOR THE HEADER ON THE LEFT
    HBox tasHeaderBox;
    Label tasHeaderLabel;
    Button deleteButton;

    // FOR THE TA TABLE
    TableView<TeachingAssistant> taTable;
    TableColumn<TeachingAssistant, String> nameColumn;
    TableColumn<TeachingAssistant, String> emailColumn;
    TableColumn<TeachingAssistant, Boolean> undergradColumn;

    //Storing time slots for combobox
    ObservableList<String> startTimeSlots;
    ObservableList<String> endTimeSlots;

    // THE TA INPUT
    HBox addBox;
    TextField nameTextField;
    TextField emailTextField;
    Button addButton;
    Button clearButton;

    // THE HEADER ON THE RIGHT
    HBox officeHoursHeaderBox;
    Label officeHoursHeaderLabel;
    HBox startEndTimeBoxPane;
    HBox startTimeBoxPane;
    HBox endTimeBoxPane;
    Label startTimeLabel;
    Label endTimeLabel;
    final ComboBox startTimeBox;
    final ComboBox endTimeBox;

    // THE OFFICE HOURS GRID
    GridPane officeHoursGridPane;
    HashMap<String, Pane> officeHoursGridTimeHeaderPanes;
    HashMap<String, Label> officeHoursGridTimeHeaderLabels;
    HashMap<String, Pane> officeHoursGridDayHeaderPanes;
    HashMap<String, Label> officeHoursGridDayHeaderLabels;
    HashMap<String, Pane> officeHoursGridTimeCellPanes;
    HashMap<String, Label> officeHoursGridTimeCellLabels;
    HashMap<String, Pane> officeHoursGridTACellPanes;
    HashMap<String, Label> officeHoursGridTACellLabels;

    public TADataPage(CSGeneratorApp initApp) {
        app = initApp;

        // WE'LL NEED THIS TO GET LANGUAGE PROPERTIES FOR OUR UI
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // INIT THE HEADER ON THE LEFT
        tasHeaderBox = new HBox();
        String tasHeaderText = props.getProperty(TAS_HEADER_TEXT);
        tasHeaderLabel = new Label(tasHeaderText);
        deleteButton = new Button("-");
        tasHeaderBox.getChildren().addAll(tasHeaderLabel, deleteButton);

        // MAKE THE TABLE AND SETUP THE DATA MODEL
        taTable = new TableView();
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TAData data = ((CSGeneratorData) app.getDataComponent()).getTAData();
        ObservableList<TeachingAssistant> tableData = data.getTeachingAssistants();
        taTable.setItems(tableData);
        
        String undergraText = props.getProperty(UNDERGRAD_COLUMN_TEXT);
        String nameColumnText = props.getProperty(NAME_COLUMN_TEXT);
        String emailColumnText = props.getProperty(EMAIL_COLUMN_TEXT);
        undergradColumn = new TableColumn(undergraText);
        nameColumn = new TableColumn(nameColumnText);
        emailColumn = new TableColumn(emailColumnText);
        
        
        
        undergradColumn.setCellValueFactory(
                new Callback<TableColumn.CellDataFeatures<TeachingAssistant, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<TeachingAssistant, Boolean> param) {
                return param.getValue().undergradProperty();
            }
        });
        undergradColumn.setCellFactory(
                CheckBoxTableCell.forTableColumn(undergradColumn) 
        );
        undergradColumn.setEditable(true);
        taTable.setEditable(true);
        
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<TeachingAssistant, String>("name")
        );
        emailColumn.setCellValueFactory(
                new PropertyValueFactory<TeachingAssistant, String>("email")
        );
        taTable.getColumns().add(undergradColumn);
        taTable.getColumns().add(nameColumn);
        taTable.getColumns().add(emailColumn);

        // ADD BOX FOR ADDING A TA
        String namePromptText = props.getProperty(NAME_PROMPT_TEXT);
        String emailPromptText = props.getProperty(EMAIL_PROMPT_TEXT);
        String addButtonText = props.getProperty(ADD_BUTTON_TEXT);
        String clearButtonText = props.getProperty(CLEAR_BUTTON_TEXT);
        nameTextField = new TextField();
        emailTextField = new TextField();
        nameTextField.setPromptText(namePromptText);
        emailTextField.setPromptText(emailPromptText);
        addButton = new Button(addButtonText);
        clearButton = new Button(clearButtonText);
        addBox = new HBox();
        nameTextField.prefWidthProperty().bind(addBox.widthProperty().multiply(.3));
        emailTextField.prefWidthProperty().bind(addBox.widthProperty().multiply(.4));
        addButton.prefWidthProperty().bind(addBox.widthProperty().multiply(.2));
        clearButton.prefWidthProperty().bind(addBox.widthProperty().multiply(.2));
        clearButton.setDisable(true);
        addBox.getChildren().add(nameTextField);
        addBox.getChildren().add(emailTextField);
        addBox.getChildren().add(addButton);
        addBox.getChildren().add(clearButton);

        // INIT THE HEADER ON THE RIGHT
        officeHoursHeaderBox = new HBox();
        startEndTimeBoxPane = new HBox();
        startTimeBoxPane = new HBox();
        endTimeBoxPane = new HBox();

        startTimeBox = new ComboBox();
        endTimeBox = new ComboBox();
        startTimeSlots = FXCollections.observableArrayList();
        endTimeSlots = FXCollections.observableArrayList();

        initComboBoxes();

        String officeHoursGridText = props.getProperty(OFFICE_HOURS_SUBHEADER);
        officeHoursHeaderLabel = new Label(officeHoursGridText);
        startTimeLabel = new Label(props.getProperty(START_TIME_TEXT));
        endTimeLabel = new Label(props.getProperty(END_TIME_TEXT));
        startTimeBoxPane.getChildren().addAll(startTimeLabel, startTimeBox);
        endTimeBoxPane.getChildren().addAll(endTimeLabel, endTimeBox);
        startEndTimeBoxPane.getChildren().addAll(startTimeBoxPane, endTimeBoxPane);
        HBox blankBox = new HBox();
        blankBox.setHgrow(blankBox, Priority.ALWAYS);
        officeHoursHeaderBox.getChildren().addAll(officeHoursHeaderLabel, blankBox, startEndTimeBoxPane);

        // THESE WILL STORE PANES AND LABELS FOR OUR OFFICE HOURS GRID
        officeHoursGridPane = new GridPane();
        officeHoursGridTimeHeaderPanes = new HashMap();
        officeHoursGridTimeHeaderLabels = new HashMap();
        officeHoursGridDayHeaderPanes = new HashMap();
        officeHoursGridDayHeaderLabels = new HashMap();
        officeHoursGridTimeCellPanes = new HashMap();
        officeHoursGridTimeCellLabels = new HashMap();
        officeHoursGridTACellPanes = new HashMap();
        officeHoursGridTACellLabels = new HashMap();

        // ORGANIZE THE LEFT AND RIGHT PANES
        VBox leftPane = new VBox();
        leftPane.getChildren().add(tasHeaderBox);
        leftPane.getChildren().add(taTable);
        leftPane.getChildren().add(addBox);
        VBox rightPane = new VBox();
        rightPane.getChildren().add(officeHoursHeaderBox);
        rightPane.getChildren().add(officeHoursGridPane);

        // BOTH PANES WILL NOW GO IN A SPLIT PANE
        basePane = new SplitPane(leftPane, new ScrollPane(rightPane));

        // MAKE SURE THE TABLE EXTENDS DOWN FAR ENOUGH
        taTable.prefHeightProperty().bind(basePane.heightProperty().multiply(1.9));
        
        // NOW LET'S SETUP THE EVENT HANDLING
        controller = new CSGController(app);

        // CONTROLS FOR ADDING TAs
        nameTextField.setOnAction(e -> {
            controller.handleAddTA();
        });
        emailTextField.setOnAction(e -> {
            controller.handleAddTA();
        });
        addButton.setOnAction(e -> {
            controller.handleAddTA();
        });

        clearButton.setOnAction(e -> {
            controller.taPageHandleClearButton();
        });

        taTable.setFocusTraversable(true);

        taTable.setOnMouseClicked(e -> {
            controller.handleMouseClicked(this, e);
        });

        taTable.setOnKeyPressed(e -> {
            controller.handleKeyPress(e.getCode());
        });
        
        deleteButton.setOnAction(e -> {
            controller.handleDeleteTA();
        });
    }
    
    public SplitPane getBasePane() {
        return basePane;
    }

    public HBox getTAsHeaderBox() {
        return tasHeaderBox;
    }

    public Label getTAsHeaderLabel() {
        return tasHeaderLabel;
    }

    public TableView getTATable() {
        return taTable;
    }

    public HBox getAddBox() {
        return addBox;
    }

    public TextField getNameTextField() {
        return nameTextField;
    }

    public TextField getEmailTextField() {
        return emailTextField;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getClearButton() {
        return clearButton;
    }

    public HBox getOfficeHoursSubheaderBox() {
        return officeHoursHeaderBox;
    }

    public Label getOfficeHoursSubheaderLabel() {
        return officeHoursHeaderLabel;
    }

    public GridPane getOfficeHoursGridPane() {
        return officeHoursGridPane;
    }

    public HashMap<String, Pane> getOfficeHoursGridTimeHeaderPanes() {
        return officeHoursGridTimeHeaderPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTimeHeaderLabels() {
        return officeHoursGridTimeHeaderLabels;
    }

    public HBox getStartEndTimeBoxPane() {
        return startEndTimeBoxPane;
    }

    public HBox getStartTimeBoxPane() {
        return startTimeBoxPane;
    }

    public HBox getEndTimeBoxPane() {
        return endTimeBoxPane;
    }

    public ComboBox getStartTimeBox() {
        return startTimeBox;
    }

    public ComboBox getEndTimeBox() {
        return endTimeBox;
    }

    public HashMap<String, Pane> getOfficeHoursGridDayHeaderPanes() {
        return officeHoursGridDayHeaderPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridDayHeaderLabels() {
        return officeHoursGridDayHeaderLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridTimeCellPanes() {
        return officeHoursGridTimeCellPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTimeCellLabels() {
        return officeHoursGridTimeCellLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridTACellPanes() {
        return officeHoursGridTACellPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTACellLabels() {
        return officeHoursGridTACellLabels;
    }

    public String getCellKey(Pane testPane) {
        for (String key : officeHoursGridTACellLabels.keySet()) {
            if (officeHoursGridTACellPanes.get(key) == testPane) {
                return key;
            }
        }
        return null;
    }

    public Label getTACellLabel(String cellKey) {
        return officeHoursGridTACellLabels.get(cellKey);
    }

    public Pane getTACellPane(String cellPane) {
        return officeHoursGridTACellPanes.get(cellPane);
    }

    public String buildCellKey(int col, int row) {
        return "" + col + "_" + row;
    }

    public String buildCellText(int militaryHour, String minutes) {
        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }
        if (hour == 0) {
            hour = 12;
        }
        String cellText = "" + hour + ":" + minutes;

        if (militaryHour < 12 || militaryHour == 24) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }
    
    @Override
    public SplitPane getPage() {
        return basePane;
    }

    @Override
    public void resetPage() {
        // CLEAR OUT THE GRID PANE
        officeHoursGridPane.getChildren().clear();

        // AND THEN ALL THE GRID PANES AND LABELS
        officeHoursGridTimeHeaderPanes.clear();
        officeHoursGridTimeHeaderLabels.clear();
        officeHoursGridDayHeaderPanes.clear();
        officeHoursGridDayHeaderLabels.clear();
        officeHoursGridTimeCellPanes.clear();
        officeHoursGridTimeCellLabels.clear();
        officeHoursGridTACellPanes.clear();
        officeHoursGridTACellLabels.clear();
        nameTextField.setText("");
        emailTextField.setText("");
    }

    @Override
    public void reloadPage(AppDataComponent dataComponent) {
        TAData taData = ((CSGeneratorData) dataComponent).getTAData();
        reloadOfficeHoursGrid(taData);
//        initComboBoxValue(taData, startTimeBox, endTimeBox);
    }

    public void reloadOfficeHoursGrid(TAData dataComponent) {
        ArrayList<String> gridHeaders = dataComponent.getGridHeaders();

        // ADD THE TIME HEADERS
        for (int i = 0; i < 2; i++) {
            addCellToGrid(dataComponent, officeHoursGridTimeHeaderPanes, officeHoursGridTimeHeaderLabels, i, 0);
            dataComponent.getCellTextProperty(i, 0).set(gridHeaders.get(i));
        }

        // THEN THE DAY OF WEEK HEADERS
        for (int i = 2; i < 7; i++) {
            addCellToGrid(dataComponent, officeHoursGridDayHeaderPanes, officeHoursGridDayHeaderLabels, i, 0);
            dataComponent.getCellTextProperty(i, 0).set(gridHeaders.get(i));
        }

        // THEN THE TIME AND TA CELLS
        int row = 1;
        for (int i = dataComponent.getStartHour(); i < dataComponent.getEndHour(); i++) {
            // START TIME COLUMN
            int col = 0;
            // START TIME COLUMN
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row);
            dataComponent.getCellTextProperty(col, row).set(buildCellText(i, "00"));
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row + 1);
            dataComponent.getCellTextProperty(col, row + 1).set(buildCellText(i, "30"));

            // END TIME COLUMN
            col++;
            int endHour = i;
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row);
            dataComponent.getCellTextProperty(col, row).set(buildCellText(endHour, "30"));
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes, officeHoursGridTimeCellLabels, col, row + 1);
            dataComponent.getCellTextProperty(col, row + 1).set(buildCellText(endHour + 1, "00"));
            col++;
            // AND NOW ALL THE TA TOGGLE CELLS
            while (col < 7) {
                addCellToGrid(dataComponent, officeHoursGridTACellPanes, officeHoursGridTACellLabels, col, row);
                addCellToGrid(dataComponent, officeHoursGridTACellPanes, officeHoursGridTACellLabels, col, row + 1);
                col++;
            }
            row += 2;

        }

        // CONTROLS FOR TOGGLING TA OFFICE HOURS
        for (Pane p : officeHoursGridTACellPanes.values()) {
            p.setFocusTraversable(true);
//            p.setOnKeyPressed(e -> {
//                controller.handleKeyPress(e.getCode());
//            });
            p.setOnMouseClicked(e -> {
                controller.handleCellToggle((Pane) e.getSource());
            });
            p.setOnMouseExited(e -> {
                controller.handleGridCellMouseExited((Pane) e.getSource());
            });
            p.setOnMouseEntered(e -> {
                controller.handleGridCellMouseEntered((Pane) e.getSource());
            });
        }

        // AND MAKE SURE ALL THE COMPONENTS HAVE THE PROPER STYLE
        TAStyle taStyle = ((CSGStyle)app.getStyleComponent()).getTAStyle();

        taStyle.initOfficeHoursGridStyle();
    }

    public void initComboBoxes() {
        startTimeSlots = FXCollections.observableArrayList();
        endTimeSlots = FXCollections.observableArrayList();
        TAData data = ((CSGeneratorData) app.getDataComponent()).getTAData();
        for (int i = data.getStartHour(); i < data.getEndHour(); i++) {
            startTimeSlots.addAll(buildCellText(i, "00"));
            endTimeSlots.addAll(buildCellText(i, "00"));
        }
        startTimeBox.setItems(startTimeSlots);
        endTimeBox.setItems(endTimeSlots);

        initComboBoxValue(data, startTimeBox, endTimeBox);

        startTimeBox.setOnAction(e -> {
            controller.handleStartTimeChange();
        });

        endTimeBox.setOnAction(e -> {
            controller.handleEndTimeChange();
        });
    }

    public void initComboBoxValue(TAData data, ComboBox<String> startTimeBox, ComboBox<String> endTimeBox) {
        String startTimePeriod;
        String endTimePeriod;
        if (data.getStartHour() < 12 || data.getStartHour() == 24) {
            startTimePeriod = "am";
        } else {
            startTimePeriod = "pm";
        }
        if (data.getEndHour() < 12 || data.getEndHour() == 24) {
            endTimePeriod = "am";
        } else {
            endTimePeriod = "pm";
        }
        startTimeBox.setValue(converHourTo12(data.getStartHour()) + ":00" + startTimePeriod);
        endTimeBox.setValue(converHourTo12(data.getEndHour()) + ":00" + endTimePeriod);
    }



    public void addCellToGrid(TAData dataComponent, HashMap<String, Pane> panes, HashMap<String, Label> labels, int col, int row) {
        // MAKE THE LABEL IN A PANE
        Label cellLabel = new Label("");
        HBox cellPane = new HBox();
        cellPane.setAlignment(Pos.CENTER);
        cellPane.getChildren().add(cellLabel);

        // BUILD A KEY TO EASILY UNIQUELY IDENTIFY THE CELL
        String cellKey = dataComponent.getCellKey(col, row);
        cellPane.setId(cellKey);
        cellLabel.setId(cellKey);

        // NOW PUT THE CELL IN THE WORKSPACE GRID
        officeHoursGridPane.add(cellPane, col, row);

        // AND ALSO KEEP IN IN CASE WE NEED TO STYLIZE IT
        panes.put(cellKey, cellPane);
        labels.put(cellKey, cellLabel);

        // AND FINALLY, GIVE THE TEXT PROPERTY TO THE DATA MANAGER
        // SO IT CAN MANAGE ALL CHANGES
        dataComponent.setCellProperty(col, row, cellLabel.textProperty());
    }

    private String converHourTo12(int hour) {
        if (hour > 12) {
            hour -= 12;
        }
        if (hour == 0) {
            hour = 12;
        }
        return String.valueOf(hour);
    }

}
