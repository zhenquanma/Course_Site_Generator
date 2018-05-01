package csg.style;

import af.components.AppStyleComponent;
import csg.CSGeneratorApp;
import csg.workspace.CSGWorkspace;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class CSGStyle extends AppStyleComponent{
    CSGeneratorApp app;
    TAStyle taStyle;
    CourseDetailsStyle courseDetailsStyle;
    RecitationStyle recitationStyle;
    ScheduleStyle scheduleStyle;
    ProjectPageStysle projectPageStysle;
    
        

    public CSGStyle(CSGeneratorApp initApp) {
        app = initApp;
        taStyle = new TAStyle(initApp);
        courseDetailsStyle = new CourseDetailsStyle(initApp);
        recitationStyle = new RecitationStyle(initApp);
        scheduleStyle = new ScheduleStyle(initApp);
        projectPageStysle = new ProjectPageStysle(initApp);
        
        super.initStylesheet(app);
        
        app.getGUI().initFileToolbarStyle();
        
//        initCSGWorkspaceStyle();

    }
    
    private void initCSGWorkspaceStyle() {
//        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        
    }
    
    public TAStyle getTAStyle() {
        return taStyle;
    }
    
}
