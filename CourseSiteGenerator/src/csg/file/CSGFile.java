/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.file;

import af.components.AppDataComponent;
import af.components.AppFileComponent;
import af.settings.AppPropertyType;
import static af.settings.AppPropertyType.EXPORT_COMPLETED_MESSAGE;
import static af.settings.AppPropertyType.EXPORT_COMPLETED_TITLE;
import af.ui.AppMessageDialogSingleton;
import af.utilities.FileCopier;
import csg.CSGeneratorApp;
import static csg.CSGeneratorProp.*;
import csg.data.CSGeneratorData;
import csg.data.CourseData;
import csg.data.ProjectData;
import csg.data.RecitationData;
import csg.data.ScheduleData;
import static csg.data.ScheduleData.DATE_SEPARATOR;
import csg.data.TAData;
import csg.utilities.Course;
import csg.utilities.Recitation;
import csg.utilities.ScheduleItem;
import csg.utilities.SitePage;
import csg.utilities.Student;
import csg.utilities.TeachingAssistant;
import csg.utilities.Team;
import csg.utilities.TimeSlot;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import properties_manager.PropertiesManager;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class CSGFile implements AppFileComponent {

    CSGeneratorApp app;

    // THESE ARE USED FOR IDENTIFYING JSON TYPES
    static final String JSON_TA_DATA_PAGE = "ta_data";
    static final String JSON_START_HOUR = "startHour";
    static final String JSON_END_HOUR = "endHour";
    static final String JSON_OFFICE_HOURS = "officeHours";
    static final String JSON_DAY = "day";
    static final String JSON_TIME = "time";
    static final String JSON_NAME = "name";
    static final String JSON_EMAIL = "email";
    static final String JSON_TAS = "tas";
    static final String JSON_UNDERGRAD_TAS = "undergrad_tas";
    static final String JSON_GRADUATE_TAS = "grad_tas";
    static final String JSON_UNDERGRAD = "is_undergrad";

    static final String JSON_COURSE_PAGE = "course_details";
    static final String JSON_COURSE_INFO = "course_info";
    static final String JSON_SITE_TEMP = "course_template";
    static final String JSON_SITE_PAGES = "site_pages";
    static final String JSON_SUBJECT = "subject";
    static final String JSON_NUMBER = "number";
    static final String JSON_SEMESTER = "semester";
    static final String JSON_YEAR = "year";
    static final String JSON_TITLE = "title";
    static final String JSON_INSTRUCTOR_NAME = "instructor_name";
    static final String JSON_INSTRUCTOR_HOME = "instructor_home";
    static final String JSON_EXPORT_DIR = "export_dir";
    static final String JSON_TEMPLATE_DIR = "template_dir";
    static final String JSON_USE = "use";
    static final String JSON_NAVBAR_TITLE = "navbar_title";
    static final String JSON_FILE_NAME = "file_name";
    static final String JSON_SCRIPT = "script";
    static final String JSON_BANNER_DIR = "banner_dir";
    static final String JSON_LEFT_FOORTER_IMAGE_DIR = "left_footer_image_dir";
    static final String JSON_RIGHT_FOOTER_IMAGE_DIR = "right_footer_image_dir";
    static final String JSON_STYLESHEET = "stylesheet";

    static final String JSON_RECITATION_PAGE = "recitation_data";
    static final String JSON_RECITATIONS = "recitations";
    static final String JSON_SECTION = "section";
    static final String JSON_INSTRUCTOR = "instructor";
    static final String JSON_DAYTIME = "day_time";
    static final String JSON_LOCATION = "location";
    static final String JSON_TA1 = "ta_1";
    static final String JSON_TA2 = "ta_2";

    static final String JSON_SCHEDULE_PAGE = "schedule_data";
    static final String JSON_SCHEDULE_ITEMS = "schedule_items";
    static final String JSON_STARTING_MON = "starting_monday";
    static final String JSON_ENDING_FRI = "ending_friday";
    static final String JSON_TYPE = "type";
    static final String JSON_DATE = "date";
    static final String JSON_TOPIC = "topic";
    static final String JSON_LINK = "link";
    static final String JSON_CRITERIA = "criteria";    

    static final String JSON_PROJECT_PAGE = "project_data";
    static final String JSON_PROJECTS = "projects";
    static final String JSON_COLOR = "color";
    static final String JSON_RED = "red";
    static final String JSON_GREEN = "green";
    static final String JSON_BLUE = "blue";
    static final String JSON_TEXT_COLOR = "text_color";
    static final String JSON_TEXT_RED = "text_red";
    static final String JSON_TEXT_GREEN = "text_green";
    static final String JSON_TEXT_BLUE = "text_blue";
    static final String JSON_FIRST_NAME = "first_name";
    static final String JSON_LAST_NAME = "last_name";
    static final String JSON_TEAM = "team";
    static final String JSON_ROLE = "role";
    static final String JSON_TEAMS = "teams";
    static final String JSON_STUDENTS = "students";
    static final String JSON_WORK = "work";
    
    static final String JSON_FILE_EXPORT_DATA = "SiteData.json";
    static final String JSON_FILE_OFFICE_HOURS_DATA = "OfficeHoursGridData.json";
    static final String JSON_FILE_RECITATIONS_DATA = "RecitationsData.json";
    static final String JSON_FILE_SCHEDULE_DATA = "ScheduleData.json";
    static final String JSON_FILE_PROJECT_DATA = "ProjectsData.json";
    static final String JSON_FILE_COURSE_INFO = "CourseInfo.json";
    static final String JSON_FILE_TEAMS_AND_STUDENTS_DATA = "TeamsAndStudents.json";
    
    static final String JSON_COURSE_PAGE_TITLE = "course_page_title";
    static final String JSON_EXPORT_STARTING_MON_MONTH = "startingMondayMonth";
    static final String JSON_EXPORT_STARTING_MON_DAY = "startingMondayDay";
    static final String JSON_EXPORT_ENDING_FRI_MONTH = "endingFridayMonth";
    static final String JSON_EXPORT_ENDING_FIR_DAY = "endingFridayDay";
    static final String JSON_EXPORT_HOLIDAY = "holidays";
    static final String JSON_EXPORT_LECTURE = "lectures";
    static final String JSON_EXPORT_REFERENCES = "references";
    static final String JSON_EXPORT_RECITATIONS = "recitations";
    static final String JSON_EXPORT_HWS = "hws";
    static final String JSON_MONTH = "month";

    
    
    
    public CSGFile(CSGeneratorApp initApp) {
        app = initApp;
    }

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
        CSGeneratorData csgData = (CSGeneratorData) data;
        CourseData courseData = csgData.getCourseData();
        TAData taData = csgData.getTAData();
        RecitationData recData = csgData.getRecitationData();
        ScheduleData scheData = csgData.getScheduleData();
        ProjectData projData = csgData.getProjectData();

        //BUILD THE COURSE JSON OBJECT
        Course course = courseData.getCourse();
        JsonObject courseJson = Json.createObjectBuilder()
                .add(JSON_SUBJECT, course.getSubject())
                .add(JSON_NUMBER, course.getNumber())
                .add(JSON_SEMESTER, course.getSemester())
                .add(JSON_YEAR, course.getYear())
                .add(JSON_TITLE, course.getTitle())
                .add(JSON_INSTRUCTOR_NAME, course.getInstructorName())
                .add(JSON_INSTRUCTOR_HOME, course.getInstructorHome())
                .add(JSON_EXPORT_DIR, courseData.getExportDir()).build();

        JsonArrayBuilder sitePageArrayBuilder = Json.createArrayBuilder();
        ObservableList<SitePage> sitePages = courseData.getSitePages();
        for (SitePage sitePage : sitePages) {
            JsonObject sitePageJson = Json.createObjectBuilder()
                    .add(JSON_USE, sitePage.getUseString())
                    .add(JSON_NAVBAR_TITLE, sitePage.getNavbarTitle())
                    .add(JSON_FILE_NAME, sitePage.getFileName())
                    .add(JSON_SCRIPT, sitePage.getScript()).build();
            sitePageArrayBuilder.add(sitePageJson);
        }
        JsonArray sitePagesArray = sitePageArrayBuilder.build();

        JsonObject courseDetailsPageJSO = Json.createObjectBuilder()
                .add(JSON_COURSE_INFO, courseJson)
                .add(JSON_TEMPLATE_DIR, courseData.getSiteTempDir())
                .add(JSON_SITE_PAGES, sitePagesArray)
                .add(JSON_BANNER_DIR, courseData.getBannerImagePath())
                .add(JSON_LEFT_FOORTER_IMAGE_DIR, courseData.getLeftFooterImagePath())
                .add(JSON_RIGHT_FOOTER_IMAGE_DIR, courseData.getRightFooterImagePath())
                .add(JSON_STYLESHEET, courseData.getStylesheet())
                .build();

        //BUILD THE TA JSON OBJECTS TO SAVE
        JsonArrayBuilder taArrayBuilder = Json.createArrayBuilder();
        ObservableList<TeachingAssistant> tas = taData.getTeachingAssistants();
        for (TeachingAssistant ta : tas) {
            JsonObject taJson = Json.createObjectBuilder()
                    .add(JSON_UNDERGRAD, ta.checkUndergrad())
                    .add(JSON_NAME, ta.getName())
                    .add(JSON_EMAIL, ta.getEmail()).build();
            taArrayBuilder.add(taJson);
        }
        JsonArray undergradTAsArray = taArrayBuilder.build();

        //BUILD THE TIME SLOT JSON OBJECTS TO SAVE
        JsonArrayBuilder timeSlotArrayBuilder = Json.createArrayBuilder();
        ArrayList<TimeSlot> officeHours = TimeSlot.buildOfficeHoursList(taData);
        for (TimeSlot ts : officeHours) {
            JsonObject tsJson = Json.createObjectBuilder()
                    .add(JSON_DAY, ts.getDay())
                    .add(JSON_TIME, ts.getTime())
                    .add(JSON_NAME, ts.getName()).build();
            timeSlotArrayBuilder.add(tsJson);
        }
        JsonArray timeSlotsArray = timeSlotArrayBuilder.build();

        //PUT IT ALL TOGETHER IN A JsonObject
        JsonObject taDataPageJSO = Json.createObjectBuilder()
                .add(JSON_START_HOUR, "" + taData.getStartHour())
                .add(JSON_END_HOUR, "" + taData.getEndHour())
                .add(JSON_TAS, undergradTAsArray)
                .add(JSON_OFFICE_HOURS, timeSlotsArray)
                .build();

        //BUILD RECITATION JSON OBJECT
        JsonArrayBuilder recitationArrayBuilder = Json.createArrayBuilder();
        ObservableList<Recitation> recitations = recData.getRecitations();
        for (Recitation rec : recitations) {
            JsonObject recJson = Json.createObjectBuilder()
                    .add(JSON_SECTION, rec.getSection())
                    .add(JSON_INSTRUCTOR, rec.getInstructor())
                    .add(JSON_DAYTIME, rec.getDayTime())
                    .add(JSON_LOCATION, rec.getLocation())
                    .add(JSON_TA1, rec.getTa1())
                    .add(JSON_TA2, rec.getTa2()).build();
            recitationArrayBuilder.add(recJson);
        }
        JsonArray recArray = recitationArrayBuilder.build();
        JsonObject recitationPageJSO = Json.createObjectBuilder()
                .add(JSON_RECITATIONS, recArray).build();

        //BUILD SCHEDULE DATA PAGE JSON OBJECT
        JsonArrayBuilder scheduleItemsArrayBuilder = Json.createArrayBuilder();
        ObservableList<ScheduleItem> scheduleItems = scheData.getScheduleItems();
        for (ScheduleItem item : scheduleItems) {
            JsonObject itemJson = Json.createObjectBuilder()
                    .add(JSON_TYPE, item.getType())
                    .add(JSON_DATE, item.getDate())
                    .add(JSON_TIME, item.getTime())
                    .add(JSON_TITLE, item.getTitle())
                    .add(JSON_TOPIC, item.getTopic())
                    .add(JSON_LINK, item.getLink())
                    .add(JSON_CRITERIA, item.getCriteria())
                    .build();
            scheduleItemsArrayBuilder.add(itemJson);
        }
        JsonArray scheduleItemsArray = scheduleItemsArrayBuilder.build();

        JsonObject schedulesPageJSO = Json.createObjectBuilder()
                .add(JSON_STARTING_MON, scheData.getStartDate())
                .add(JSON_ENDING_FRI, scheData.getEndDate())
                .add(JSON_SCHEDULE_ITEMS, scheduleItemsArray)
                .build();

        //BUILD PROJECT DATA PAGE OBJECT
        JsonArrayBuilder teamsArrayBuilder = Json.createArrayBuilder();
        ObservableList<Team> teams = projData.getTeams();
        for (Team team : teams) {
            JsonObject teamJson = Json.createObjectBuilder()
                    .add(JSON_NAME, team.getName())
                    .add(JSON_COLOR, team.getColor())
                    .add(JSON_RED, getRedValue(team.getColor()))
                    .add(JSON_GREEN, getGreenValue(team.getColor()))
                    .add(JSON_BLUE, getBlueValue(team.getColor()))
                    .add(JSON_TEXT_COLOR, team.getTextColor())
                    .add(JSON_TEXT_RED, getRedValue(team.getTextColor()))
                    .add(JSON_TEXT_GREEN, getGreenValue(team.getTextColor()))
                    .add(JSON_TEXT_BLUE, getBlueValue(team.getTextColor()))
                    .add(JSON_LINK, team.getLink())
                    .build();
            teamsArrayBuilder.add(teamJson);
        }
        JsonArray teamsArray = teamsArrayBuilder.build();

        JsonArrayBuilder studentsArrayBuilder = Json.createArrayBuilder();
        ObservableList<Student> students = projData.getStudents();
        for (Student student : students) {
            JsonObject studentJson = Json.createObjectBuilder()
                    .add(JSON_FIRST_NAME, student.getFirstName())
                    .add(JSON_LAST_NAME, student.getLastName())
                    .add(JSON_TEAM, student.getTeamName())
                    .add(JSON_ROLE, student.getRole())
                    .build();
            studentsArrayBuilder.add(studentJson);
        }
        JsonArray studentsArray = studentsArrayBuilder.build();

        JsonObject projectsPageJSO = Json.createObjectBuilder()
                .add(JSON_TEAMS, teamsArray)
                .add(JSON_STUDENTS, studentsArray)
                .build();

        //PUT ALL FIVE PAGES OBJECTS INTO ONE OBJECT
        JsonObject csgDataJSO = Json.createObjectBuilder()
                .add(JSON_COURSE_PAGE, courseDetailsPageJSO)
                .add(JSON_TA_DATA_PAGE, taDataPageJSO)
                .add(JSON_RECITATION_PAGE, recitationPageJSO)
                .add(JSON_SCHEDULE_PAGE, schedulesPageJSO)
                .add(JSON_PROJECT_PAGE, projectsPageJSO)
                .build();

        //NOW OUPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(csgDataJSO);
        jsonWriter.close();

        // INIT THE WRITER
        OutputStream os = new FileOutputStream(filePath);
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(csgDataJSO);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(filePath);
        pw.write(prettyPrinted);
        pw.close();
    }
    
    private String getRedValue(String hexColor) {
        Color color = Color.web(hexColor);
        int red = (int) (color.getRed() * 255);
        return String.valueOf(red);
    }
    
    private String getBlueValue(String hexColor) {
        Color color = Color.web(hexColor);
        int blue = (int) (color.getBlue() * 255);
        return String.valueOf(blue);
    }
    
    private String getGreenValue(String hexColor) {
        Color color = Color.web(hexColor);
        int green = (int) (color.getGreen() * 255);
        return String.valueOf(green);
    }

    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        CSGeneratorData csgData = (CSGeneratorData) data;
        CourseData courseData = csgData.getCourseData();
        TAData taData = csgData.getTAData();
        RecitationData recData = csgData.getRecitationData();
        ScheduleData scheData = csgData.getScheduleData();
        ProjectData projData = csgData.getProjectData();

        //LOAD THE JSON FILE WITH ALL THE DATA
        JsonObject json = loadJSONFile(filePath);

        //LOAD THE COURSE DETAILS PAGE
        JsonObject coursePageJSO = json.getJsonObject(JSON_COURSE_PAGE);
        JsonObject courseInfoJSO = coursePageJSO.getJsonObject(JSON_COURSE_INFO);
        String courseSub = courseInfoJSO.getString(JSON_SUBJECT);
        String courseNum = courseInfoJSO.getString(JSON_NUMBER);
        String semester = courseInfoJSO.getString(JSON_SEMESTER);
        String year = courseInfoJSO.getString(JSON_YEAR);
        String courseTitle = courseInfoJSO.getString(JSON_TITLE);
        String instructorName = courseInfoJSO.getString(JSON_INSTRUCTOR_NAME);
        String instructorHome = courseInfoJSO.getString(JSON_INSTRUCTOR_HOME);
        String exportDir = courseInfoJSO.getString(JSON_EXPORT_DIR);

        Course course = new Course.CourseBuilder()
                .subject(courseSub)
                .number(courseNum)
                .semester(semester)
                .year(year)
                .title(courseTitle)
                .instructorName(instructorName)
                .instructorHome(instructorHome).buildCourse();
        courseData.setCourse(course);
        courseData.setExportDir(exportDir);

        String tempDir = coursePageJSO.getString(JSON_TEMPLATE_DIR);
        courseData.setSiteTempDir(tempDir);

        JsonArray sitePagesJsonArray = coursePageJSO.getJsonArray(JSON_SITE_PAGES);
        for (int i = 0; i < sitePagesJsonArray.size(); i++) {
            JsonObject jsonSitePage = sitePagesJsonArray.getJsonObject(i);
            String useStr = jsonSitePage.getString(JSON_USE);
            String navbarTitle = jsonSitePage.getString(JSON_NAVBAR_TITLE);
            String fileName = jsonSitePage.getString(JSON_FILE_NAME);
            String script = jsonSitePage.getString(JSON_SCRIPT);
            if (useStr.equals("true")) {
                courseData.addSitePage(true, navbarTitle, fileName, script);
            } else if (useStr.equals("false")) {
                courseData.addSitePage(false, navbarTitle, fileName, script);
            }
        }
        String bannerImageDir = coursePageJSO.getString(JSON_BANNER_DIR);
        String leftFooterImageDir = coursePageJSO.getString(JSON_LEFT_FOORTER_IMAGE_DIR);
        String rightFooterImageDir = coursePageJSO.getString(JSON_RIGHT_FOOTER_IMAGE_DIR);
        String stylesheet = coursePageJSO.getString(JSON_STYLESHEET);
        courseData.setBannerImagePath(bannerImageDir);
        courseData.setLeftFooterImagePath(leftFooterImageDir);
        courseData.setRightFooterImagePath(rightFooterImageDir);
        courseData.setStylesheet(stylesheet);

        //LOAD THE START AND END HOURS FOR TA DATA PAGE
        JsonObject taDataPageJSO = json.getJsonObject(JSON_TA_DATA_PAGE);
        String startHour = taDataPageJSO.getString(JSON_START_HOUR);
        String endHour = taDataPageJSO.getString(JSON_END_HOUR);
        taData.initHours(startHour, endHour);

        //LOAD ALL THE UNDERGRAD TAs
        JsonArray jsonTAArray = taDataPageJSO.getJsonArray(JSON_TAS);
        for (int i = 0; i < jsonTAArray.size(); i++) {
            JsonObject jsonTA = jsonTAArray.getJsonObject(i);
            String undergrad = jsonTA.getString(JSON_UNDERGRAD);
            String name = jsonTA.getString(JSON_NAME);
            String email = jsonTA.getString(JSON_EMAIL);
            if (undergrad.equals("true")) {
                taData.addTA(true, name, email);
            } else {
                taData.addTA(name, email);
            }
        }

        //LOAD THE RECITATION DATA PAGE
        JsonObject recPageJSO = json.getJsonObject(JSON_RECITATION_PAGE);
        JsonArray jsonRecArray = recPageJSO.getJsonArray(JSON_RECITATIONS);
        for (int i = 0; i < jsonRecArray.size(); i++) {
            JsonObject jsonRec = jsonRecArray.getJsonObject(i);
            String recSection = jsonRec.getString(JSON_SECTION);
            String recInstructor = jsonRec.getString(JSON_INSTRUCTOR);
            String recDayTime = jsonRec.getString(JSON_DAYTIME);
            String location = jsonRec.getString(JSON_LOCATION);
            String recTA1 = jsonRec.getString(JSON_TA1);
            String recTA2 = jsonRec.getString(JSON_TA2);
            recData.addRecitation(recSection, recInstructor, recDayTime, location, recTA1, recTA2);
        }

        //LOAD THE SCHEDULE DATA PAGE
        JsonObject schePageJSO = json.getJsonObject(JSON_SCHEDULE_PAGE);
        String startingMon = schePageJSO.getString(JSON_STARTING_MON);
        String endingFri = schePageJSO.getString(JSON_ENDING_FRI);
        scheData.setStartDate(startingMon);
        scheData.setEndDate(endingFri);

        JsonArray jsonScheItemArray = schePageJSO.getJsonArray(JSON_SCHEDULE_ITEMS);
        for (int i = 0; i < jsonScheItemArray.size(); i++) {
            JsonObject jsonItem = jsonScheItemArray.getJsonObject(i);
            String type = jsonItem.getString(JSON_TYPE);
            String date = jsonItem.getString(JSON_DATE);
            String time = jsonItem.getString(JSON_TIME);
            String title = jsonItem.getString(JSON_TITLE);
            String topic = jsonItem.getString(JSON_TOPIC);
            String link = jsonItem.getString(JSON_LINK);
            String criteria = jsonItem.getString(JSON_CRITERIA);
            scheData.addScheduleItem(type, date, time, title, topic, link, criteria);
        }

        //LOAD THE PROJECT DATA PAGE
        JsonObject projPageJSO = json.getJsonObject(JSON_PROJECT_PAGE);
        JsonArray jsonTeamArray = projPageJSO.getJsonArray(JSON_TEAMS);
        for (int i = 0; i < jsonTeamArray.size(); i++) {
            JsonObject jsonTeam = jsonTeamArray.getJsonObject(i);
            String name = jsonTeam.getString(JSON_NAME);
            String color = jsonTeam.getString(JSON_COLOR);
            String textColor = jsonTeam.getString(JSON_TEXT_COLOR);
            String link = jsonTeam.getString(JSON_LINK);
            projData.addTeam(name, color, textColor, link);
        }
        JsonArray jsonStudentArray = projPageJSO.getJsonArray(JSON_STUDENTS);
        for (int i = 0; i < jsonStudentArray.size(); i++) {
            JsonObject jsonStudent = jsonStudentArray.getJsonObject(i);
            String firstName = jsonStudent.getString(JSON_FIRST_NAME);
            String lastName = jsonStudent.getString(JSON_LAST_NAME);
            String team = jsonStudent.getString(JSON_TEAM);
            String role = jsonStudent.getString(JSON_ROLE);
            projData.addStudent(firstName, lastName, team, role);
        }

        //RELOAD TAPAGE WORKSPACE WITH THE LOADED DATA
        app.getWorkspaceComponent().reloadWorkspace(app.getDataComponent());

        //AND ALL THE OFFICE HOURS
        JsonArray jsonOfficeHoursArray = taDataPageJSO.getJsonArray(JSON_OFFICE_HOURS);
        for (int i = 0; i < jsonOfficeHoursArray.size(); i++) {
            JsonObject jsonOfficeHours = jsonOfficeHoursArray.getJsonObject(i);
            String day = jsonOfficeHours.getString(JSON_DAY);
            String time = jsonOfficeHours.getString(JSON_TIME);
            String name = jsonOfficeHours.getString(JSON_NAME);
            taData.addOfficeHoursReservation(day, time, name);
        }

    }

    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
        InputStream is = new FileInputStream(jsonFilePath);
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;
    }
    
    public String hexColor(String red, String green, String blue) {
        int r = Integer.parseInt(red);
        int g = Integer.parseInt(green);
        int b = Integer.parseInt(blue);
        Color color = Color.rgb(r, g, b);
        String hexColor = color.toString();
        return hexColor.substring(2);   
    }

    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
 
        CSGeneratorData csgData = (CSGeneratorData) data;
        CourseData courseData = csgData.getCourseData();
        String jsFilePath = courseData.getExportDir() + File.separator + "js" + File.separator + JSON_FILE_EXPORT_DATA;
        FileCopier fileCopy = new FileCopier(courseData.getSiteTempDir(), courseData.getExportDir());
        fileCopy.start();
        
        File jsonFile = new File(jsFilePath);
        saveData(csgData, jsonFile.getPath());
        
        AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
        dialog.show(props.getProperty(EXPORT_COMPLETED_TITLE), props.getProperty(EXPORT_COMPLETED_MESSAGE));
    }
}
