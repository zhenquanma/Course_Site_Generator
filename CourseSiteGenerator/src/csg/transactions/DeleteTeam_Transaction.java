
package csg.transactions;

import af.utilities.jTPS_Transaction;
import csg.CSGeneratorApp;
import csg.controller.CSGController;
import csg.data.CSGeneratorData;
import csg.data.ProjectData;
import csg.workspace.CSGWorkspace;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class DeleteTeam_Transaction implements jTPS_Transaction{
    
    CSGeneratorApp app;
    CSGController controller;
    String name;
    String color;
    String textColor;
    String link;

    public DeleteTeam_Transaction(CSGeneratorApp app, String name, String color, String textColor, String link) {
        this.app = app;
        this.name = name;
        this.color = color;
        this.textColor = textColor;
        this.link = link;
        controller = ((CSGWorkspace)app.getWorkspaceComponent()).getCSGController();
    }
    
    @Override
    public void doTransaction() {
        ProjectData projectData = ((CSGeneratorData) app.getDataComponent()).getProjectData();
        projectData.removeTeam(name);
        controller.markWorkAsEdited();
    }
    
    @Override
    public void undoTransaction() {
        ProjectData projectData = ((CSGeneratorData) app.getDataComponent()).getProjectData();
        projectData.addTeam(name, color, textColor, link);
        controller.markWorkAsEdited();
    }
    
}
