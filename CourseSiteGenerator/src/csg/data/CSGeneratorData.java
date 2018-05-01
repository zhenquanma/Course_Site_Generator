package csg.data;

import af.components.AppDataComponent;
import csg.CSGeneratorApp;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class CSGeneratorData implements AppDataComponent{
    
    CSGeneratorApp app;
    
    CourseData courseData;
    TAData taData;
    ScheduleData scheduleData;
    RecitationData recitationData;
    ProjectData projectData;
    
    public CSGeneratorData(CSGeneratorApp initApp) {
        app = initApp;
        courseData = new CourseData(initApp);
        taData = new TAData(initApp);
        scheduleData = new ScheduleData(initApp);
        recitationData = new RecitationData(initApp);
        projectData = new ProjectData(initApp);
    }

    public CourseData getCourseData() {
        return courseData;
    }

    public TAData getTAData() {
        return taData;
    }

    public ScheduleData getScheduleData() {
        return scheduleData;
    }

    public RecitationData getRecitationData() {
        return recitationData;
    }

    public ProjectData getProjectData() {
        return projectData;
    }   
    
    @Override
    public void resetData() {
        courseData.resetData();
        taData.resetData();
        scheduleData.resetData();
        recitationData.resetData();
        projectData.resetData();
    }
    
    
}
