/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.workspace;

import af.components.AppDataComponent;
import af.components.AppWorkspaceComponent;
import csg.CSGeneratorApp;
import static csg.CSGeneratorProp.*;
import csg.controller.CSGController;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import properties_manager.PropertiesManager;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class CSGWorkspace extends AppWorkspaceComponent{
    CSGeneratorApp app;
    CSGController controller;
    
    TabPane tabPane;
    
    Tab courseDetailTab;
    Tab taDataTab;
    Tab recitationTab;
    Tab scheduleTab;
    Tab projecTab;
    
    CourseDetailPage courseDetailPage;
    TADataPage taDataPage;
    RecitationDataPage recitationDataPage;
    ScheduleDataPage scheduleDataPage;
    ProjectDataPage projectDataPage;
    
    
    
    public CSGWorkspace(CSGeneratorApp initApp) {
        app = initApp;
        controller = new CSGController(initApp);
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        tabPane = new TabPane();
        courseDetailTab = new Tab();
        taDataTab = new Tab();
        recitationTab = new Tab();
        scheduleTab = new Tab();
        projecTab = new Tab();
        
        //FIVE TAB PAGES
        courseDetailPage = new CourseDetailPage(initApp);
        taDataPage  = new TADataPage(initApp);
        recitationDataPage = new RecitationDataPage(initApp);
        scheduleDataPage = new ScheduleDataPage(initApp);
        projectDataPage = new ProjectDataPage(initApp);
        
        courseDetailTab.setText(props.getProperty(COURSE_DETAILS_TXT));
        courseDetailTab.setClosable(false);
        taDataTab.setText(props.getProperty(TA_DATA_TXT));
        taDataTab.setClosable(false);
        recitationTab.setText(props.getProperty(RECITATION_DATA_TXT));
        recitationTab.setClosable(false);
        scheduleTab.setText(props.getProperty(SCHEDULE_DATA_TXT));
        scheduleTab.setClosable(false);
        projecTab.setText(props.getProperty(PROJECT_DATA_TXT));
        projecTab.setClosable(false);
        tabPane.getTabs().addAll(courseDetailTab, taDataTab, recitationTab,
                scheduleTab, projecTab);
        
        courseDetailTab.setContent(courseDetailPage.getPage());
        taDataTab.setContent(taDataPage.getPage());
        recitationTab.setContent(recitationDataPage.getPage());
        scheduleTab.setContent(scheduleDataPage.getPage());
        projecTab.setContent(projectDataPage.getPage());        
        
        workspace = new BorderPane();
        workspace.setOnKeyPressed(e -> {
            if (e.isControlDown()) {
                if (e.getCode() == KeyCode.Z) {
                    app.getGUI().getAppFileController().handleUndoRequest();
                } else if (e.getCode() == KeyCode.Y) {
                    app.getGUI().getAppFileController().handleRedoRequest();
                }
            }
        });
        
        ((BorderPane) workspace).setCenter(tabPane);
        
        tabPane.prefHeightProperty().bind(workspace.heightProperty().multiply(1.9));
        
        
    }
    
    public TabPane getTabPane() {
        return tabPane;
    }
    
    public CourseDetailPage getCourseDetailPage() {
        return courseDetailPage;
    }
    
    public TADataPage getTADataPage() {
        return  taDataPage;
    }

    public RecitationDataPage getRecitationDataPage() {
        return recitationDataPage;
    }
    
    public ScheduleDataPage getScheduleDataPage() {
        return scheduleDataPage;
    }
    
    public ProjectDataPage getProjectDataPage() {
        return projectDataPage;
    }
     
    public CSGController getCSGController() {
        return controller;
    }
    
    
    @Override
    public void resetWorkspace() {
        courseDetailPage.resetPage();
        taDataPage.resetPage();
        recitationDataPage.resetPage();
        scheduleDataPage.resetPage();
        projectDataPage.resetPage();
    }
    
    @Override
    public void reloadWorkspace(AppDataComponent dataComponent) {
        courseDetailPage.reloadPage(dataComponent);
        taDataPage.reloadPage(dataComponent);
        recitationDataPage.reloadPage(dataComponent);
        scheduleDataPage.reloadPage(dataComponent);
        projectDataPage.reloadPage(dataComponent);
    }
    
    
     
}
