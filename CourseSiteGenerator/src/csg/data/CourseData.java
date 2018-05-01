package csg.data;

import af.components.AppDataComponent;
import csg.CSGeneratorApp;
import static csg.CSGeneratorProp.SEMESTERS;
import static csg.CSGeneratorProp.SUBJECTS;
import csg.controller.CSGController;
import csg.utilities.Course;
import csg.utilities.FileFinder;
import csg.utilities.SitePage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import properties_manager.PropertiesManager;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class CourseData implements AppDataComponent{
    
    CSGeneratorApp app;
    CSGController controller;
    
    Course course;
    StringProperty exportDir;
    
    StringProperty siteTempDir;
    ObservableList<SitePage> sitePages;
    
    String bannerImagePath;
    String leftFooterImagePath;
    String rightFooterImagePath;
    String stylesheet;
    
    ObservableList<String> subjectList;
    ObservableList<String> numberList;
    ObservableList<String> semesterList;
    ObservableList<String> yearList;
    
    ObservableList<String> stylesheetsList;
    
    SitePage home;
    SitePage syllabus;
    SitePage schedule;
    SitePage hws;
    SitePage projects;
        
    public static final int MIN_YEAR = 2017;
    public static final int MAX_YEAR = 2030;
    
    public static final String STYLESHEET_DIR = "./work/css/";
    public static final String EXPORT_INITIAL_DIR = "../Courses/";
    public static final String TEMPLATE_INITAL_DIR = "./templates/";
    public static final String HOME_NAVBAR_TITLE = "Home";
    public static final String SYLLABUS_NAVBAR_TITLE = "Syllabus";
    public static final String SCHEDULE_NAVBAR_TITLE = "Schedule";
    public static final String HWS_NAVBAR_TITLE = "HWs";
    public static final String PROJECTS_NAVBAR_TITLE = "Projects";
    public static final String INDEX_HTML = "index.html";
    public static final String SYLLABUS_HTML = "syllabus.html";
    public static final String SCHEDULE_HTML = "schedule.html";
    public static final String HWS_HTML = "hws.html";
    public static final String PROJECTS_HTML = "projects.html";
    public static final String HOME_BUILDER_JS = "HomeBuilder.js";
    public static final String SYLLABUS_BUILDER_JS = "SyllabusBuilder.js";
    public static final String SCHEDULE_BUILDER_JS = "ScheduleBuilder.js";
    public static final String HWS_BUILDER_JS = "HWsBuilder.js";
    public static final String PROJECTS_BUILDER_JS = "ProjectsBuilder.js";
        
    
    
    public CourseData(CSGeneratorApp initApp) {
        app = initApp;
        controller = new CSGController(initApp);
        course = new Course();
        exportDir = new SimpleStringProperty();
        
        siteTempDir = new SimpleStringProperty();
        sitePages = FXCollections.observableArrayList();
        home = new SitePage(true, HOME_NAVBAR_TITLE, INDEX_HTML, HOME_BUILDER_JS);
        syllabus = new SitePage(true, SYLLABUS_NAVBAR_TITLE, SYLLABUS_HTML, SYLLABUS_BUILDER_JS);
        schedule = new SitePage(true, SCHEDULE_NAVBAR_TITLE, SCHEDULE_HTML, SCHEDULE_BUILDER_JS);
        hws = new SitePage(true, HWS_NAVBAR_TITLE, HWS_HTML, HWS_BUILDER_JS);
        projects = new SitePage(true, PROJECTS_NAVBAR_TITLE, PROJECTS_HTML, PROJECTS_BUILDER_JS);
                
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        subjectList = FXCollections.observableArrayList();
        subjectList.addAll(props.getPropertyOptionsList(SUBJECTS));
        numberList = FXCollections.observableArrayList();
        semesterList = FXCollections.observableArrayList();
        semesterList.addAll(props.getPropertyOptionsList(SEMESTERS));
        yearList = FXCollections.observableArrayList();
        stylesheetsList = FXCollections.observableArrayList();
        initNumberList();
        initYearList();
        initStylesheetList();
    }
    
    
    
    @Override
    public void resetData(){
        course = new Course();
        exportDir.set("");
        siteTempDir.set("");
        sitePages.clear();
        bannerImagePath = "";
        leftFooterImagePath = "";
        rightFooterImagePath = "";
        stylesheet = "";
        home.setUse(true);
        syllabus.setUse(true);
        hws.setUse(true);
        projects.setUse(true);
    }
    
    private void initYearList() {
        for (int i = MIN_YEAR; i <= MAX_YEAR; i++) {
            yearList.add(String.valueOf(i));
        }
    }
    
    private void initNumberList() {
        for (int i = 100; i <= 400; i++) {
            numberList.add(String.valueOf(i));
        }
    }
    
    private void initStylesheetList() {
        File dir = new File(STYLESHEET_DIR);
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".css");
            }
        };
        List<File> fileList = new ArrayList<>();
        FileFinder.getFiles(dir, filter, fileList);
        for (File file : fileList) {
            stylesheetsList.add(file.getName());
        }
    }

    public Course getCourse() {
        return course;
    }
    
    public ObservableList getSubjectList() {
        return subjectList;
    }
    
    public ObservableList getNumberList() {
        return numberList;
    }
    
    public ObservableList getSemesterList() {
        return semesterList;
    }
    
    public ObservableList getYearList() {
        return yearList;
    }
    
    public SitePage getHomePage() {
        return home;
    }
    
    public SitePage getSyllabusPage() {
        return syllabus;
    }
    
    public SitePage getSchedulePage() {
        return schedule;
    }
    
    public SitePage getHWsPage() {
        return hws;
    }
    
    public SitePage getProjectPage() {
        return projects;
    }
    
    /**
     * @return the exportDir
     */
    public StringProperty exportDirProperty() {
        return exportDir;
    }
    
    public String getExportDir() { return exportDir.get(); }

    /**
     * @return the siteTempDir
     */
    public StringProperty getSiteTempDirPro() {
        return siteTempDir;
    }
    
    public String getSiteTempDir() { return siteTempDir.get(); }

    /**
     * @return the sitePages
     */
    public ObservableList getSitePages() {
        return sitePages;
    }

    /**
     * @return the bannerImagePath
     */
    public String getBannerImagePath() {
        return bannerImagePath;
    }

    /**
     * @return the leftFooterImagePath
     */
    public String getLeftFooterImagePath() {
        return leftFooterImagePath;
    }

    /**
     * @return the rightFooterImagePath
     */
    public String getRightFooterImagePath() {
        return rightFooterImagePath;
    }

    /**
     * @return the current selected stylesheet 
     */
    public String getStylesheet() {
        return stylesheet;
    }
    
    /**
     * @return the stylesheet list
     */
    public ObservableList getStylesheetList() {
        return stylesheetsList;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * @param exportDir the exportDir to set
     */
    public void setExportDir(String exportDir) {
        app.getGUI().getAppFileController().settExportFile(new File(exportDir));
        this.exportDir.set(exportDir);
    }

    /**
     * @param siteTempDir the siteTempDir to set
     */
    public void setSiteTempDir(String siteTempDir) {
        this.siteTempDir.set(siteTempDir);
    }

    /**
     * @param sitePages the sitePages to set
     */
    public void setSitePages(ObservableList<SitePage> sitePages) {
        this.sitePages = sitePages;
    }

    /**
     * @param bannerImagePath the bannerImagePath to set
     */
    public void setBannerImagePath(String bannerImagePath) {
        this.bannerImagePath = bannerImagePath;
    }

    /**
     * @param leftFooterImagePath the leftFooterImagePath to set
     */
    public void setLeftFooterImagePath(String leftFooterImagePath) {
        this.leftFooterImagePath = leftFooterImagePath;
    }

    /**
     * @param rightFooterImagePath the rightFooterImagePath to set
     */
    public void setRightFooterImagePath(String rightFooterImagePath) {
        this.rightFooterImagePath = rightFooterImagePath;
    }

    /**
     * @param stylesheet the stylesheet to set
     */
    public void setStylesheet(String stylesheet) {
        this.stylesheet = stylesheet;
    }
    
    public void addSitePage(boolean use, String navtitle,
            String fileName, String script) {
        SitePage newSitePage = new SitePage(use, navtitle, fileName, script);
        sitePages.add(newSitePage);
    }
    
    public void addHomePage(boolean use) {
        home.setUse(use);
        sitePages.add(home);
    }
    
    public void addSyllabusPage(boolean use) {
        syllabus.setUse(use);
        sitePages.add(syllabus);
    }
    
    public void addSchedulePage(boolean use) {
        schedule.setUse(use);
        sitePages.add(schedule);
    }
    
    public void addHWsPage(boolean use) {
        hws.setUse(use);
        sitePages.add(hws);
    }
    
    public void addProjectsPage(boolean use) {
        projects.setUse(use);
        sitePages.add(projects);
    }
    
    public void addStylesheet(String stylesheet) {
        stylesheetsList.add(stylesheet);
    }
}
