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
import csg.utilities.Student;
import csg.workspace.CSGWorkspace;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class UpdateStudent_Transaction implements jTPS_Transaction{
    
    CSGeneratorApp app;
    CSGController controller;
    String oldFirstName;
    String oldLastName;
    String oldTeamName;
    String oldRole;
    String newFirstName;
    String newLastName;
    String newTeamName;
    String newRole;

    public UpdateStudent_Transaction(CSGeneratorApp app, String oldFirstName, String oldLastName, String oldTeamName, String oldRole, String newFirstName, String newLastName, String newTeamName, String newRole) {
        this.app = app;
        this.oldFirstName = oldFirstName;
        this.oldLastName = oldLastName;
        this.oldTeamName = oldTeamName;
        this.oldRole = oldRole;
        this.newFirstName = newFirstName;
        this.newLastName = newLastName;
        this.newTeamName = newTeamName;
        this.newRole = newRole;
        controller = ((CSGWorkspace)app.getWorkspaceComponent()).getCSGController();
    }
    
    @Override
    public void doTransaction() {
        ProjectData projectData = ((CSGeneratorData)app.getDataComponent()).getProjectData();
        projectData.updateStudent(oldFirstName, oldLastName, newFirstName, newLastName, newTeamName, newRole);
        controller.markWorkAsEdited();
    }
    
    @Override
    public void undoTransaction() {
        ProjectData projectData = ((CSGeneratorData)app.getDataComponent()).getProjectData();
        projectData.updateStudent(newFirstName, newLastName, oldFirstName, oldLastName, oldTeamName, oldRole);
        controller.markWorkAsEdited();
    }
    
    
}
