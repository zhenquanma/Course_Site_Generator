package csg;

import af.AppTemplate;
import csg.data.CSGeneratorData;
import csg.file.CSGFile;
import csg.style.CSGStyle;
import csg.workspace.CSGWorkspace;
import java.util.Locale;

/**
 *
 * @author Zhenquan Ma
 */
public class CSGeneratorApp extends AppTemplate{

    @Override
    public void buildAppComponentsHook() {
        dataComponent = new CSGeneratorData(this);
        workspaceComponent = new CSGWorkspace(this);
        fileComponent = new CSGFile(this);
        styleComponent = new CSGStyle(this);
    }
    
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        launch(args);
    }
    
}
