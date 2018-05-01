package csg.file;

import af.components.AppDataComponent;
import static af.settings.AppStartupConstants.APP_PROPERTIES_FILE_ENGLISH_NAME;
import static af.settings.AppStartupConstants.PATH_WORK;
import csg.CSGeneratorApp;
import csg.data.CSGeneratorData;
import csg.data.CourseData;
import csg.data.ProjectData;
import csg.data.RecitationData;
import csg.data.ScheduleData;
import csg.data.TAData;
import csg.utilities.Course;
import java.io.IOException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import static test_bed.TestSave.COURSE_SUBJECT;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class CSGFileTest {
    
    public CSGFileTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of saveData method, of class CSGFile.
     */
    @Ignore
    @Test
    public void testSaveData() throws Exception {
        System.out.println("saveData");
        AppDataComponent data = null;
        String filePath = "";
        CSGFile instance = null;
        instance.saveData(data, filePath);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadData method, of class CSGFile.
     */
    @Test
    public void testLoadData() throws Exception {
        CSGeneratorApp app = new CSGeneratorApp();
        app.loadProperties(APP_PROPERTIES_FILE_ENGLISH_NAME);
        System.out.println("Testing loadData():");
        CSGeneratorData data = new CSGeneratorData(app);
        String filePath = PATH_WORK + "SiteSaveTest.json";
        CSGFile csgFile = new CSGFile(app);
        csgFile.loadData(data, filePath);
        CourseData courseData = data.getCourseData();
        TAData taData = data.getTAData();
        RecitationData recData = data.getRecitationData();
        ScheduleData scheduleData = data.getScheduleData();
        ProjectData projectData = data.getProjectData();
        
        Course course = courseData.getCourse();
        assertEquals(COURSE_SUBJECT, course.getSubject());
        
        
        
    }

    /**
     * Test of exportData method, of class CSGFile.
     */
    @Ignore
    @Test
    public void testExportData() throws IOException {
        System.out.println("exportData");
        AppDataComponent data = null;
        String filePath = "";
        CSGFile instance = null;
        instance.exportData(data, filePath);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
