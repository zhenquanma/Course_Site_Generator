package test_bed;

import static af.settings.AppStartupConstants.FILE_PROTOCOL;
import static af.settings.AppStartupConstants.PATH_IMAGES;
import csg.CSGeneratorApp;
import csg.data.CSGeneratorData;
import csg.data.CourseData;
import csg.data.ProjectData;
import csg.data.RecitationData;
import csg.data.ScheduleData;
import csg.data.TAData;
import csg.file.CSGFile;
import csg.utilities.Course;
import csg.utilities.TeachingAssistant;
import java.io.IOException;


/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class TestSave {
    
    public static final String COURSE_SUBJECT = "CSE";
    public static final String COURSE_NUM = "219";
    public static final String COURSE_SEM = "Spring";
    public static final String COURSE_YEAR = "2017";
    public static final String COURSE_TITLE = "Computer Science III";
    public static final String INSTRUCTOR_NAME = "Ritwik Banerjee";
    public static final String INSTUCTOR_HOME = "http://www3.cs.stonybrook.edu/~rbanerjee/";
    public static final String EXPORT_DIR = "../Courses/CSE219/Spring2017/public";
    public static final String SiteTempDir = "./templates/CSE219";
    public static final String BANNER_IMAGE_NAME = "SBUDarkRedShieldLogo.png";
    public static final String LEFT_FOOTER_IMAGE_NAME = "SBUWhiteShieldLogo.jpg";
    public static final String RIGHT_FOOTER_IMAGE_NAME = "CSLogo.png";
    public static final String STYLESHEET_NAME = "sea_wolf.css";

    
    CSGeneratorApp app;

    public TestSave(CSGeneratorApp app) throws IOException {
        this.app =  app;
    }
    
    
    
    public void init() throws IOException {

        CSGeneratorData csgData = (CSGeneratorData)app.getDataComponent();
        CourseData courseData = csgData.getCourseData();
        Course course = courseData.getCourse();
        course.setSubject(COURSE_SUBJECT);
        course.setNumber(COURSE_NUM);
        course.setSemester(COURSE_SEM);
        course.setYear(COURSE_YEAR);
        course.setTitle(COURSE_TITLE);
        course.setInstructorName(INSTRUCTOR_NAME);
        course.setInstructorHome(INSTUCTOR_HOME);
        courseData.setExportDir(EXPORT_DIR);
        courseData.setSiteTempDir(SiteTempDir);
        courseData.addSitePage(true, "Home", "index.html", "HomeBuilder.js");
        courseData.addSitePage(true, "Syllabus", "syllabus.html", "SyllubusBuilder.js");
        courseData.addSitePage(true, "Schedule", "schedule.html", "ScheduleBuilder.js");
        courseData.addSitePage(true, "HWs", "hws.html", "HWsBuilder.js");
        courseData.addSitePage(true, "Projects", "projects.html", "ProjectsBuilder.js");
        courseData.setBannerImagePath(FILE_PROTOCOL + PATH_IMAGES + BANNER_IMAGE_NAME);
        courseData.setLeftFooterImagePath(FILE_PROTOCOL + PATH_IMAGES + LEFT_FOOTER_IMAGE_NAME);
        courseData.setRightFooterImagePath(FILE_PROTOCOL + PATH_IMAGES + RIGHT_FOOTER_IMAGE_NAME);
        courseData.setStylesheet(STYLESHEET_NAME);
        
        
        TAData taData = csgData.getTAData();
        taData.addTA(true, "Jane Doe", "jane.doe@yahoo.com");
        taData.addTA(false, "Joe Shmo", "joe.shmo@yale.edu");
        taData.addOfficeHoursReservation("MONDAY", "2_00am", "Jane Dow");
        taData.addOfficeHoursReservation("WEDNESDAY", "7_00am", "Jane Dow");
        taData.addOfficeHoursReservation("TUESDAY", "12_00pm", "Joe Shmo");
        taData.addOfficeHoursReservation("MONDAY", "8_00am", "Joe Shmo");
        
        RecitationData recData = csgData.getRecitationData();
        String ta1 = "Jane Doe";
        String ta2 = "Joe Shmo";
        recData.addRecitation("R02", "McKenna", "Wed 3:30pm-4:23pm", "Old CS 2114", ta1, ta2);
        recData.addRecitation("R05", "Banerjee", "Tues 5:30pm-6:23pm", "Old CS 2114", ta1, ta2);
        
        ScheduleData scheduleData = csgData.getScheduleData();
        scheduleData.setStartDate("2017-04-03");
        scheduleData.setEndDate("2017-05-05");
        scheduleData.addScheduleItem("Holiday", "2017-04-08", "whole day", "SNOW DAY", "..", "..", "..");
        
        ProjectData projectData = csgData.getProjectData();
        projectData.addTeam("Atomic Comics", "552211", "ffffff", "http://atomiccomic.com");
        projectData.addTeam("C4 Comics", "235399", "ffffff", "http://c4-comics.com");
        projectData.addStudent("Beau", "Brummell", "Atomic Comics", "Lead Designer");
        projectData.addStudent("Jane", "Doe", "C4 Comics", "Lead Programmer");
       
    }
}
