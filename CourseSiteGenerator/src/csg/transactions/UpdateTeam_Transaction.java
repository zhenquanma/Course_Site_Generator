/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class UpdateTeam_Transaction implements jTPS_Transaction{
    
    CSGeneratorApp app;
    CSGController controller;
    String oldName;
    String oldColor;
    String oldTextColor;
    String oldLink;
    
    String newName;
    String newColor;
    String newTextColor;
    String newLink;

    public UpdateTeam_Transaction(CSGeneratorApp app, String oldName, String oldColor, String oldTextColor, String oldLink, String newName, String newColor, String newTextColor, String newLink) {
        this.app = app;
        this.oldName = oldName;
        this.oldColor = oldColor;
        this.oldTextColor = oldTextColor;
        this.oldLink = oldLink;
        this.newName = newName;
        this.newColor = newColor;
        this.newTextColor = newTextColor;
        this.newLink = newLink;
        controller = ((CSGWorkspace)app.getWorkspaceComponent()).getCSGController();
    }
    
    @Override
    public void doTransaction() {
        ProjectData projectData = ((CSGeneratorData) app.getDataComponent()).getProjectData();
        projectData.updateTeam(oldName, newName, newColor, newTextColor, newLink);
        controller.markWorkAsEdited();
    }
    
    @Override
    public void undoTransaction() {
        ProjectData projectData = ((CSGeneratorData) app.getDataComponent()).getProjectData();
        projectData.updateTeam(newName, oldName, oldColor, oldTextColor, oldLink);
        controller.markWorkAsEdited();
    }

}
