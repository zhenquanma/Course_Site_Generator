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
import csg.data.CourseData;
import csg.utilities.Course;
import csg.utilities.SitePage;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import properties_manager.PropertiesManager;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class CourseDetailPage implements Page{
    CSGeneratorApp app;
    CSGController controller;
    ScrollPane basePane;
    VBox backgroundBox;
    
    //COURSE INFO BLOCK
    GridPane courseInfoPane;
    Label courseInfoHeaderLabel;
    Label subjectLabel;
    Label numberLabel;
    Label semesterLabel;
    Label yearLabel;
    Label titleLabel;
    Label instructorNameLabel;
    Label instructorsHomeLabel;
    Label exportDirLabel;
    Label dirLabel;
    ComboBox<String> subjectComboBox;
    ComboBox<String> numberComboBox;
    ComboBox<String> semesterComboBox;
    ComboBox<String> yearComboBox;
    TextField titleTextField;
    TextField instructorNameTextField;
    TextField instructorsHomeTextField;
    Button exportDirChangeButton;

    //SITE TEMPLATE BLOCK
    VBox siteTemplatePane;
    Label siteTemplateHeaderLabel;
    Label instructionLabel;
    Label templateDirLabel;
    Label sitePagesLabel;
    Button selectTempDirButton;
    TableView<SitePage> sitePagesTable;
    TableColumn<SitePage, Boolean> useCheckColumn;
    TableColumn<SitePage, String> navbarTitleColumn;
    TableColumn<SitePage, String> fileNameColumn;
    TableColumn<SitePage, String> scriptColumn;
    
    
    
    
    //PAGE STYLE BLOCK
    GridPane pageStylePane;
    Label pageStyleHeaderLabel;
    Label bannerImageLabel;
    Label leftFooterImageLabel;
    Label rightFoorterImageLabel;
    Label stylesheetLabel;
    Label noteLabel;
    Button changeBannerButton;
    Button changeLeftFooterButton;
    Button changeRightFooterButton;
    ComboBox<String> stylesheeComboBox;
    Image bannerImage;
    Image leftFooterImage;
    Image rightFooterImage;
    HBox bannerImageBox;
    HBox leftFooterImageBox;
    HBox rightFooterImageBox;
    
    


    public CourseDetailPage(CSGeneratorApp initApp) {
        app = initApp;
        controller = new CSGController(initApp);
        CourseData courseData = ((CSGeneratorData)app.getDataComponent()).getCourseData();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        basePane = new ScrollPane();
        
        courseInfoHeaderLabel = new Label(props.getProperty(COURSE_INFO_TEXT));
        subjectLabel = new Label(props.getProperty(SUBJECT_TEXT));
        numberLabel = new Label(props.getProperty(NUMBER_TEXT));
        semesterLabel = new Label(props.getProperty(SEMESTER_TEXT));
        yearLabel = new Label(props.getProperty(YEAR_TEXT));
        titleLabel = new Label(props.getProperty(TITLE_TEXT));
        instructorNameLabel = new Label(props.getProperty(INSTRUCTOR_NAME_TEXT));
        instructorsHomeLabel = new Label(props.getProperty(INSTRUCTORS_HOME_TEXT));
        exportDirLabel = new Label(props.getProperty(EXPORT_DIR_TEXT));
        dirLabel = new Label();
        siteTemplateHeaderLabel = new Label(props.getProperty(SITE_TEMPLATE_TEXT));
        instructionLabel = new Label(props.getProperty(INSTRUCTION_TEXT));
        templateDirLabel = new Label();
        sitePagesLabel = new Label(props.getProperty(SITE_PAGES_TEXT));
        pageStyleHeaderLabel = new Label(props.getProperty(PAGE_STYLE_TEXT));
        bannerImageLabel = new Label(props.getProperty(BANNER_SCHOOL_IMAGE_TEXT));
        leftFooterImageLabel = new Label(props.getProperty(LEFT_FOOTER_IMAGE_TEXT));
        rightFoorterImageLabel = new Label(props.getProperty(RIGHT_FOOTER_IMAGE_TEXT));
        stylesheetLabel = new Label(props.getProperty(STYLESHEET_TEXT));
        noteLabel = new Label(props.getProperty(NOTE_MESSAGE));
        
        subjectComboBox = new ComboBox<>();
        numberComboBox = new ComboBox<>();
        semesterComboBox = new ComboBox<>();
        yearComboBox = new ComboBox<>();
        stylesheeComboBox = new ComboBox<>();
        subjectComboBox.setItems(courseData.getSubjectList());
        numberComboBox.setItems(courseData.getNumberList());
        semesterComboBox.setItems(courseData.getSemesterList());
        yearComboBox.setItems(courseData.getYearList());
        stylesheeComboBox.setItems(courseData.getStylesheetList());
        
        titleTextField = new TextField();
        instructorNameTextField = new TextField();
        instructorsHomeTextField = new TextField();
        
        sitePagesTable = new TableView<>();
        sitePagesTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        ObservableList<SitePage> sitePages = courseData.getSitePages();
        sitePagesTable.setItems(sitePages);
        useCheckColumn = new TableColumn<>(props.getProperty(USE_COLUMN_TEXT));
        navbarTitleColumn = new TableColumn<>(props.getProperty(NAVBAR_COLUMN_TEXT));
        fileNameColumn = new TableColumn<>(props.getProperty(FILENAME_COLUMN_TEXT));
        scriptColumn = new TableColumn<>(props.getProperty(SCRIPT_COLUMN_TEXT));
        
        useCheckColumn.setCellValueFactory(
                    new Callback<TableColumn.CellDataFeatures<SitePage, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<SitePage, Boolean> param) {
                return param.getValue().getUseProperty();
            }
        });
        
        useCheckColumn.setCellFactory(
                CheckBoxTableCell.forTableColumn(useCheckColumn) 
        );
        useCheckColumn.setEditable(true);
        sitePagesTable.setEditable(true);
        navbarTitleColumn.setCellValueFactory(
                new PropertyValueFactory<SitePage, String>("navbarTitle")
        );
        
        fileNameColumn.setCellValueFactory(
                new PropertyValueFactory<SitePage, String>("fileName")
        );
        
        scriptColumn.setCellValueFactory(
                new PropertyValueFactory<SitePage, String>("script")
        );
        
        sitePagesTable.getColumns().addAll(useCheckColumn, navbarTitleColumn, fileNameColumn, scriptColumn);


        exportDirChangeButton = new Button(props.getProperty(CHANGE_BUTTON_TEXT));
        selectTempDirButton = new Button(props.getProperty(SELECT_TEMPLATE_DIR_BUTTON_TEXT));
        changeBannerButton = new Button(props.getProperty(CHANGE_BUTTON_TEXT));
        changeLeftFooterButton = new Button(props.getProperty(CHANGE_BUTTON_TEXT));
        changeRightFooterButton = new Button(props.getProperty(CHANGE_BUTTON_TEXT));
        
        
        //COURSE INFO
        courseInfoPane = new GridPane();
        courseInfoPane.add(courseInfoHeaderLabel, 0, 0);
        courseInfoPane.addRow(1, subjectLabel, subjectComboBox, numberLabel, numberComboBox);
        courseInfoPane.addRow(2, semesterLabel, semesterComboBox, yearLabel, yearComboBox);
        courseInfoPane.addRow(3, titleLabel,titleTextField);
        courseInfoPane.addRow(4, instructorNameLabel, instructorNameTextField);
        courseInfoPane.addRow(5, instructorsHomeLabel, instructorsHomeTextField);
        courseInfoPane.addRow(6, exportDirLabel, dirLabel, exportDirChangeButton);
        
        //SITE TEMPLATE
        siteTemplatePane = new VBox();
        siteTemplatePane.getChildren().addAll(siteTemplateHeaderLabel, instructionLabel,
                templateDirLabel, selectTempDirButton, sitePagesLabel, sitePagesTable);
        
        //PAGE STYLE
        pageStylePane = new GridPane();
        bannerImageBox = new HBox();
        leftFooterImageBox = new HBox();
        rightFooterImageBox = new HBox();
        pageStylePane.add(pageStyleHeaderLabel, 0, 0);
        pageStylePane.addRow(1, bannerImageLabel, bannerImageBox, changeBannerButton);
        pageStylePane.addRow(2, leftFooterImageLabel, leftFooterImageBox, changeLeftFooterButton);
        pageStylePane.addRow(3, rightFoorterImageLabel, rightFooterImageBox, changeRightFooterButton);
        pageStylePane.addRow(4, stylesheetLabel, stylesheeComboBox);
        pageStylePane.addRow(5, noteLabel);
        
        backgroundBox = new VBox();
        backgroundBox.getChildren().addAll(courseInfoPane, siteTemplatePane, pageStylePane);
        basePane.setContent(backgroundBox);
        
        backgroundBox.prefWidthProperty().bind(basePane.widthProperty());
        
        exportDirChangeButton.setOnAction(e -> {
            controller.handleExportDirChange();
        });
        
        selectTempDirButton.setOnAction(e -> {
            controller.handleTemplateDirChange();
        });
        
        changeBannerButton.setOnAction(e -> {
            controller.handleBannerChange();
        });
        
        changeLeftFooterButton.setOnAction(e -> {
            controller.handleLeftFooterImageChange();
        });
        
        changeRightFooterButton.setOnAction(e -> {
            controller.handleRightFooterImageChange();
        });
        
        stylesheeComboBox.setOnAction(e ->{
            controller.handleStylesheetChange();
        });
        
        
    }
    
    public VBox getBackgroundBox() {
        return backgroundBox;
    }
    
    public GridPane getCourseInfoPane() {
        return courseInfoPane;
    }
    
    public VBox getSiteTemplatePane() {
        return siteTemplatePane;
    }
    
    public GridPane getPageStylePane() {
        return pageStylePane;
    }
    
    public HBox getBannerImageBox() {
        return bannerImageBox;
    }
    
    public HBox getLeftFooterImageBox() {
        return leftFooterImageBox;
    }
    
    public HBox getRightFooterImageBox() {
        return rightFooterImageBox;
    }
    
    public ComboBox getStylesheetComboBox() {
        return stylesheeComboBox;
    }
    
    @Override
    public void reloadPage(AppDataComponent dataComponent) {
        CourseData courseData = ((CSGeneratorData) dataComponent).getCourseData();
        Course currentCourse = courseData.getCourse();

        subjectComboBox.valueProperty().bindBidirectional(currentCourse.subjectProperty());
        numberComboBox.valueProperty().bindBidirectional(currentCourse.numberProperty());
        semesterComboBox.valueProperty().bindBidirectional(currentCourse.semesterProperty());
        yearComboBox.valueProperty().bindBidirectional(currentCourse.yearProperty());
        titleTextField.textProperty().bindBidirectional(currentCourse.getTitlePro());
        instructorNameTextField.textProperty().bindBidirectional(currentCourse.instructorNameProperty());
        instructorsHomeTextField.textProperty().bindBidirectional(currentCourse.instructorHomeProperty());
        dirLabel.textProperty().bind(courseData.exportDirProperty());
        templateDirLabel.textProperty().bind(courseData.getSiteTempDirPro());
        
        currentCourse.subjectProperty().addListener((observable, oldValue, newValue) -> {
            controller.markWorkAsEdited();
        });
        currentCourse.numberProperty().addListener((observable, oldValue, newValue) -> {
            controller.markWorkAsEdited();
        });
        currentCourse.yearProperty().addListener((observable, oldValue, newValue) -> {
            controller.markWorkAsEdited();
        });
        currentCourse.semesterProperty().addListener((observable, oldValue, newValue) -> {
            controller.markWorkAsEdited();
        });
        currentCourse.titleProperty().addListener((observable, oldValue, newValue) -> {
            controller.markWorkAsEdited();
        });
        currentCourse.instructorNameProperty().addListener((observable, oldValue, newValue) -> {
            controller.markWorkAsEdited();
        });
        currentCourse.instructorHomeProperty().addListener((observable, oldValue, newValue) -> {
            controller.markWorkAsEdited();
        });
        
        if (!courseData.getBannerImagePath().equals("")) {
            loadBannerImage();
        }
        if (!courseData.getLeftFooterImagePath().equals("")) {
            loadLeftFooterImage();
        }
        if(!courseData.getRightFooterImagePath().equals("")) {
            loadRightFooterImage();
        }
        stylesheeComboBox.setValue(courseData.getStylesheet());
    }
    
    public void loadBannerImage() {
        CourseData courseData = ((CSGeneratorData) app.getDataComponent()).getCourseData();
        bannerImage = new Image(courseData.getBannerImagePath());
        bannerImageBox.getChildren().add(new ImageView(bannerImage));
    }
    
    public void loadLeftFooterImage() {
        CourseData courseData = ((CSGeneratorData) app.getDataComponent()).getCourseData();
        leftFooterImage = new Image(courseData.getLeftFooterImagePath());
        leftFooterImageBox.getChildren().add(new ImageView(leftFooterImage));
    }
    
    public void loadRightFooterImage() {
        CourseData courseData = ((CSGeneratorData) app.getDataComponent()).getCourseData();
        rightFooterImage = new Image(courseData.getRightFooterImagePath());
        rightFooterImageBox.getChildren().add(new ImageView(rightFooterImage));
    }
    
    
    
    @Override
    public void resetPage() {
        bannerImageBox.getChildren().clear();
        leftFooterImageBox.getChildren().clear();
        rightFooterImageBox.getChildren().clear();
    }
    
    @Override
    public ScrollPane getPage() { return basePane; }
    
}
