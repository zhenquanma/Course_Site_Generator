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
import csg.data.ScheduleData;
import static csg.data.ScheduleData.DATE_SEPARATOR;
import csg.utilities.ScheduleItem;
import java.time.DayOfWeek;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
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
import javafx.util.Callback;
import properties_manager.PropertiesManager;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class ScheduleDataPage implements Page{

    CSGeneratorApp app;
    CSGController controller;
    ScrollPane basePane;
    VBox backgroundBox;
    
    HBox scheduleHeaderBox;
    Label scheduleHeaderLabel;
    
    GridPane calendarBoundariesPane;
    Label calendarBoundariesHeaderLabel;
    Label startingLabel;
    Label endingLabel;
    DatePicker startDatePicker;
    DatePicker endDatePicker;
    
    VBox scheduleItemsPane;
    HBox scheduleItemsHeaderBox;
    Label scheduleItemsHeaderLabel;
    Button deleteButton;
    
    TableView<ScheduleItem> scheduleTable;
    TableColumn<ScheduleItem, String> typeColumn;
    TableColumn<ScheduleItem, String> dateColumn;
    TableColumn<ScheduleItem, String> titleColumn;
    TableColumn<ScheduleItem, String> topicColumn;
    
    GridPane addEditPane;
    Label addEditHeaderLabel;
    Label typeLabel;
    Label dateLabel;
    Label timeLabel;
    Label titleLabel;
    Label topicLabel;
    Label linkLabel;
    Label criteriaLabel;
    ComboBox<String> typeComboBox;
    DatePicker datePicker;
    TextField timeTextField;
    TextField titleTextField;
    TextField topicTextField;
    TextField linkTextField;
    TextField criteriaTextField;
    Button addUpdateButton;
    Button clearButton;
       
    

    public ScheduleDataPage(CSGeneratorApp initApp) {
        app = initApp;
        controller = new CSGController(app);
        ScheduleData scheduleData = ((CSGeneratorData)app.getDataComponent()).getScheduleData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        //UPPER BLOCK
        scheduleHeaderBox = new HBox();
        scheduleHeaderLabel = new Label(props.getProperty(SCHEDULE_HEADER_TEXT));
        scheduleHeaderBox.getChildren().add(scheduleHeaderLabel);
        
        calendarBoundariesPane = new GridPane();
        calendarBoundariesHeaderLabel = new Label(props.getProperty(CALENDAR_BOUNDARIES_HEADER_TEXT));
        startingLabel = new Label(props.getProperty(STARTING_MONDAY_TEXT));
        endingLabel = new Label(props.getProperty(ENDING_FRIDAY_TEXT));
        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();
        
        startDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.getDayOfWeek() != DayOfWeek.MONDAY){
                setDisable(true);
                setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });
        startDatePicker.setEditable(false);

        final Callback<DatePicker, DateCell> dayCellFactory
                = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isBefore(
                                startDatePicker.getValue().plusDays(1)) || item.getDayOfWeek() != DayOfWeek.FRIDAY) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        };
        endDatePicker.setDayCellFactory(dayCellFactory);
        endDatePicker.setEditable(false);
        
        calendarBoundariesPane.addRow(0, calendarBoundariesHeaderLabel);
        calendarBoundariesPane.addRow(1, startingLabel, startDatePicker, endingLabel, endDatePicker);
        
        //BUTTOM BLOCK
        scheduleItemsPane = new VBox();
        scheduleItemsHeaderBox = new HBox();
        scheduleItemsHeaderLabel = new Label(props.getProperty(SCHEDULE_ITEMS_HEADER_TEXT));
        deleteButton = new Button("-");
        scheduleItemsHeaderBox.getChildren().addAll(scheduleItemsHeaderLabel, deleteButton);
        
        scheduleTable = new TableView<>();
        scheduleTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<ScheduleItem> scheduleItemsTableData = scheduleData.getScheduleItems();
        scheduleTable.setItems(scheduleItemsTableData);
        typeColumn = new TableColumn<>(props.getProperty(TYPE_TEXT));
        dateColumn = new TableColumn<>(props.getProperty(DATE_TEXT));
        titleColumn = new TableColumn<>(props.getProperty(TITLE_TEXT));
        topicColumn = new TableColumn<>(props.getProperty(TOPIC_TEXT));
        
        typeColumn.setCellValueFactory(
                new PropertyValueFactory<ScheduleItem, String>("type")
        );
        dateColumn.setCellValueFactory(
                new PropertyValueFactory<ScheduleItem, String>("date")
        );
        titleColumn.setCellValueFactory(
                new PropertyValueFactory<ScheduleItem, String>("title")
        );
        topicColumn.setCellValueFactory(
                new PropertyValueFactory<ScheduleItem, String>("topic")
        );
        
        scheduleTable.getColumns().add(typeColumn);
        scheduleTable.getColumns().add(dateColumn);
        scheduleTable.getColumns().add(titleColumn);
        scheduleTable.getColumns().add(topicColumn);
        
        addEditPane = new GridPane();
        addEditHeaderLabel = new Label(props.getProperty(ADD_EDIT_HEADER_TEXT));
        typeLabel = new Label(props.getProperty(TYPE_TEXT));
        dateLabel = new Label(props.getProperty(DATE_TEXT));
        timeLabel = new Label(props.getProperty(TIME_TEXT));
        titleLabel = new Label(props.getProperty(TITLE_TEXT));
        topicLabel = new Label(props.getProperty(TOPIC_TEXT));
        linkLabel = new Label(props.getProperty(LINK_TEXT));
        criteriaLabel = new Label(props.getProperty(CRITERIA_TEXT));
        addUpdateButton = new Button(props.getProperty(ADD_UPDATE_BUTTON_TEXT));
        clearButton = new Button(props.getProperty(CLEAR_BUTTON_TEXT));
        clearButton.setDisable(true);
        typeComboBox = new ComboBox<>();
        typeComboBox.setItems(scheduleData.getTypeList());
        
        datePicker = new DatePicker();
        
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
             public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(startDatePicker.getValue()) 
                        || date.isAfter(endDatePicker.getValue())
                        || date.getDayOfWeek() == DayOfWeek.SATURDAY
                        || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                  setDisable(true);
                  setStyle("-fx-background-color: #ffc0cb;");
                }
            }
        });
        datePicker.setEditable(false);

        
        timeTextField = new TextField();
        titleTextField = new TextField();
        topicTextField = new TextField();
        linkTextField = new TextField();
        criteriaTextField = new TextField();
                
        addEditPane.addRow(0, addEditHeaderLabel);
        addEditPane.addRow(1, typeLabel, typeComboBox);
        addEditPane.addRow(2, dateLabel, datePicker);
        addEditPane.addRow(3, timeLabel, timeTextField);
        addEditPane.addRow(4, titleLabel, titleTextField);
        addEditPane.addRow(5, topicLabel, topicTextField);
        addEditPane.addRow(6, linkLabel, linkTextField);
        addEditPane.addRow(7, criteriaLabel, criteriaTextField);
        addEditPane.addRow(8, addUpdateButton, clearButton);
        
        scheduleItemsPane.getChildren().addAll(scheduleItemsHeaderBox, scheduleTable, addEditPane);        
        
        basePane = new ScrollPane();
        backgroundBox = new VBox();
        backgroundBox.getChildren().addAll(scheduleHeaderBox, calendarBoundariesPane, scheduleItemsPane);
        basePane.setContent(backgroundBox);
        backgroundBox.prefWidthProperty().bind(basePane.widthProperty());
        
        CSGController controller = new CSGController(initApp);
        addUpdateButton.setOnAction(e -> {
            controller.handleAddSchedule();
        });
        clearButton.setOnAction(e -> {
            controller.schedulePageHandleClear();
        });
        scheduleTable.setOnMouseClicked(e -> {
            controller.handleMouseClicked(this, e);
        });
        
        deleteButton.setOnAction(e -> {
            controller.handleDeleteSchedule();
        });
    }
    
    public HBox getScheduleHeaderBox() {
        return scheduleHeaderBox;
    }

    public GridPane getCalendarBoundariesPane() {
        return calendarBoundariesPane;
    }
    
    public VBox getScheduleItemsPane() {
        return scheduleItemsPane;
    }
    
    public GridPane getAddEditPane() {
        return addEditPane;
    }
            
            
    @Override
    public ScrollPane getPage() {
        return basePane;
    }

    public TableView getScheduleTable() {
        return scheduleTable;
    }
    
    
    
    
    @Override
    public void resetPage() {
        controller.schedulePageHandleClear();
    }

    @Override
    public void reloadPage(AppDataComponent data) {
        ScheduleData scheduleData = ((CSGeneratorData) data).getScheduleData();
        getStartDatePicker().setValue(LocalDate.of(scheduleData.getStartYear(),
                scheduleData.getStartMonth(), scheduleData.getStartDay()));
        getEndDatePicker().setValue(LocalDate.of(scheduleData.getEndYear(),
                scheduleData.getEndMonth(), scheduleData.getEndDay()));
    }

    /**
     * @return the startDatePicker
     */
    public DatePicker getStartDatePicker() {
        return startDatePicker;
    }

    /**
     * @return the endDatePicker
     */
    public DatePicker getEndDatePicker() {
        return endDatePicker;
    }

    /**
     * @return the deleteButton
     */
    public Button getDeleteButton() {
        return deleteButton;
    }

    /**
     * @return the typeComboBox
     */
    public ComboBox<String> getTypeComboBox() {
        return typeComboBox;
    }

    /**
     * @return the datePicker
     */
    public DatePicker getDatePicker() {
        return datePicker;
    }

    /**
     * @return the timeTextField
     */
    public TextField getTimeTextField() {
        return timeTextField;
    }

    /**
     * @return the titleTextField
     */
    public TextField getTitleTextField() {
        return titleTextField;
    }

    /**
     * @return the topicTextField
     */
    public TextField getTopicTextField() {
        return topicTextField;
    }

    /**
     * @return the linkTextField
     */
    public TextField getLinkTextField() {
        return linkTextField;
    }

    /**
     * @return the criteriaTextField
     */
    public TextField getCriteriaTextField() {
        return criteriaTextField;
    }

    /**
     * @return the addUpdateButton
     */
    public Button getAddUpdateButton() {
        return addUpdateButton;
    }

    /**
     * @return the clearButton
     */
    public Button getClearButton() {
        return clearButton;
    }

    /**
     * @param typeComboBox the typeComboBox to set
     */
    public void setTypeComboBox(String type) {
        this.typeComboBox.getSelectionModel().select(type);
    }

    /**
     * @param datePicker the datePicker to set
     */
    public void setDatePicker(String date) {
        int year = Integer.parseInt(date.substring(0, date.indexOf(DATE_SEPARATOR)));
        int month = Integer.parseInt(date.substring(date.indexOf(DATE_SEPARATOR) + 1, date.lastIndexOf(DATE_SEPARATOR)));
        int day = Integer.parseInt(date.substring(date.lastIndexOf(DATE_SEPARATOR) + 1));
        
        this.datePicker.setValue(LocalDate.of(year, month, day));
    }

    /**
     * @param timeTextField the timeTextField to set
     */
    public void setTimeTextField(String time) {
        this.timeTextField.setText(time);
    }

    /**
     * @param titleTextField the titleTextField to set
     */
    public void setTitleTextField(String title) {
        this.titleTextField.setText(title);
    }

    /**
     * @param topicTextField the topicTextField to set
     */
    public void setTopicTextField(String topic) {
        this.topicTextField.setText(topic);
    }

    /**
     * @param linkTextField the linkTextField to set
     */
    public void setLinkTextField(String link) {
        this.linkTextField.setText(link);
    }

    /**
     * @param criteriaTextField the criteriaTextField to set
     */
    public void setCriteriaTextField(String criteria) {
        this.criteriaTextField.setText(criteria);
    }
}
