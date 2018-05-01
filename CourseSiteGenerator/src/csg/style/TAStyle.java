package csg.style;

import af.AppTemplate;
import csg.utilities.TeachingAssistant;
import csg.workspace.CSGWorkspace;
import csg.workspace.TADataPage;

import java.util.HashMap;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * This class manages all CSS style for this application.
 * 
 * @author Richard McKenna
 * @version 1.0
 */
public class TAStyle {
    
    public static String CLASS_BASE_PANE = "background_pane";
    
    // THESE ARE THE HEADERS FOR EACH SIDE
    public static String CLASS_HEADER_PANE = "header_pane";
    public static String CLASS_HEADER_LABEL = "header_label";
    
    public static String CLASS_START_END_TIME_PANE = "start_end_time_pane";
    public static String CLASS_COMBOBOX_PANE = "combo_box_pane";

    // ON THE LEFT WE HAVE THE TA ENTRY
    public static String CLASS_TA_TABLE = "ta_table";
    public static String CLASS_TA_TABLE_COLUMN_HEADER = "ta_table_column_header";
    public static String CLASS_ADD_TA_PANE = "add_ta_pane";
    public static String CLASS_ADD_TA_TEXT_FIELD = "add_ta_text_field";
    public static String CLASS_ADD_TA_BUTTON = "add_ta_button";
    public static String CLASS_CLEAR_BUTTON = "clear_button";

    // ON THE RIGHT WE HAVE THE OFFICE HOURS GRID
    public static String CLASS_OFFICE_HOURS_GRID = "office_hours_grid";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_PANE = "office_hours_grid_time_column_header_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_LABEL = "office_hours_grid_time_column_header_label";
    public static String CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_PANE = "office_hours_grid_day_column_header_pane";
    public static String CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_LABEL = "office_hours_grid_day_column_header_label";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_CELL_PANE = "office_hours_grid_time_cell_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TIME_CELL_LABEL = "office_hours_grid_time_cell_label";
    public static String CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE = "office_hours_grid_ta_cell_pane";
    public static String CLASS_OFFICE_HOURS_GRID_TA_CELL_LABEL = "office_hours_grid_ta_cell_label";

    // FOR HIGHLIGHTING CELLS, COLUMNS, AND ROWS
    public static String CLASS_HIGHLIGHTED_GRID_CELL = "highlighted_grid_cell";
    public static String CLASS_HIGHLIGHTED_GRID_ROW_OR_COLUMN = "highlighted_grid_row_or_column";
    
    // THIS PROVIDES ACCESS TO OTHER COMPONENTS
    private AppTemplate app;
    
    /**
     * This constructor initializes all style for the application.
     * 
     * @param initApp The application to be stylized.
     */
    public TAStyle(AppTemplate initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // AND NOW OUR WORKSPACE STYLE
        initTADataPageStyle();
    }

    /**
     * This function specifies all the style classes for
     * all user interface controls in the workspace.
     */
    private void initTADataPageStyle() {
        // LEFT SIDE - THE HEADER
        CSGWorkspace workspaceComponent = (CSGWorkspace)app.getWorkspaceComponent();
        TADataPage tADataPage = workspaceComponent.getTADataPage();
        tADataPage.getTAsHeaderBox().getStyleClass().add(CLASS_HEADER_PANE);
        tADataPage.getTAsHeaderLabel().getStyleClass().add(CLASS_HEADER_LABEL);

        // LEFT SIDE - THE TABLE
        TableView<TeachingAssistant> taTable = tADataPage.getTATable();
        taTable.getStyleClass().add(CLASS_TA_TABLE);
        for (TableColumn tableColumn : taTable.getColumns()) {
            tableColumn.getStyleClass().add(CLASS_TA_TABLE_COLUMN_HEADER);
        }

        // LEFT SIDE - THE TA DATA ENTRY
        tADataPage.getAddBox().getStyleClass().add(CLASS_ADD_TA_PANE);
        tADataPage.getNameTextField().getStyleClass().add(CLASS_ADD_TA_TEXT_FIELD);
        tADataPage.getEmailTextField().getStyleClass().add(CLASS_ADD_TA_TEXT_FIELD);
        tADataPage.getAddButton().getStyleClass().add(CLASS_ADD_TA_BUTTON);
        tADataPage.getClearButton().getStyleClass().add(CLASS_CLEAR_BUTTON);

        // RIGHT SIDE - THE HEADER
        tADataPage.getOfficeHoursSubheaderBox().getStyleClass().add(CLASS_HEADER_PANE);
        tADataPage.getOfficeHoursSubheaderLabel().getStyleClass().add(CLASS_HEADER_LABEL);
        tADataPage.getStartEndTimeBoxPane().getStyleClass().add(CLASS_START_END_TIME_PANE);
        tADataPage.getStartTimeBoxPane().getStyleClass().add(CLASS_COMBOBOX_PANE);
        tADataPage.getEndTimeBoxPane().getStyleClass().add(CLASS_COMBOBOX_PANE);
        
        tADataPage.getBasePane().getStyleClass().add(CLASS_BASE_PANE);
    }
    
    /**
     * This method initializes the style for all UI components in
     * the office hours grid. Note that this should be called every
     * time a new TA Office Hours Grid is created or loaded.
     */
    public void initOfficeHoursGridStyle() {
        // RIGHT SIDE - THE OFFICE HOURS GRID TIME HEADERS
        CSGWorkspace workspaceComponent = (CSGWorkspace)app.getWorkspaceComponent();
        TADataPage tADataPage = workspaceComponent.getTADataPage();
        tADataPage.getOfficeHoursGridPane().getStyleClass().add(CLASS_OFFICE_HOURS_GRID);
        setStyleClassOnAll(tADataPage.getOfficeHoursGridTimeHeaderPanes(), CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_PANE);
        setStyleClassOnAll(tADataPage.getOfficeHoursGridTimeHeaderLabels(), CLASS_OFFICE_HOURS_GRID_TIME_COLUMN_HEADER_LABEL);
        setStyleClassOnAll(tADataPage.getOfficeHoursGridDayHeaderPanes(), CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_PANE);
        setStyleClassOnAll(tADataPage.getOfficeHoursGridDayHeaderLabels(), CLASS_OFFICE_HOURS_GRID_DAY_COLUMN_HEADER_LABEL);
        setStyleClassOnAll(tADataPage.getOfficeHoursGridTimeCellPanes(), CLASS_OFFICE_HOURS_GRID_TIME_CELL_PANE);
        setStyleClassOnAll(tADataPage.getOfficeHoursGridTimeCellLabels(), CLASS_OFFICE_HOURS_GRID_TIME_CELL_LABEL);
        setStyleClassOnAll(tADataPage.getOfficeHoursGridTACellPanes(), CLASS_OFFICE_HOURS_GRID_TA_CELL_PANE);
        setStyleClassOnAll(tADataPage.getOfficeHoursGridTACellLabels(), CLASS_OFFICE_HOURS_GRID_TA_CELL_LABEL);
    }
    
    /**
     * This helper method initializes the style of all the nodes in the nodes
     * map to a common style, styleClass.
     */
    private void setStyleClassOnAll(HashMap nodes, String styleClass) {
        for (Object nodeObject : nodes.values()) {
            Node n = (Node)nodeObject;
            n.getStyleClass().add(styleClass);
        }
    }
}