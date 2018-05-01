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
public class DeleteStudent_Transaction implements jTPS_Transaction{
    
    CSGeneratorApp app;
    CSGController controller;
    String firstName;
    String lastName;
    String teamName;
    String role;

    public DeleteStudent_Transaction(CSGeneratorApp app, String firstName, String lastName, String teamName, String role) {
        this.app = app;
        this.controller = ((CSGWorkspace)app.getWorkspaceComponent()).getCSGController();
        this.firstName = firstName;
        this.lastName = lastName;
        this.teamName = teamName;
        this.role = role;
    }
    
    @Override
    public void doTransaction() {
        ProjectData projectData = ((CSGeneratorData)app.getDataComponent()).getProjectData();
        projectData.removeStudent(firstName, lastName);
        controller.markWorkAsEdited();
    }
    
    @Override
    public void undoTransaction() {
        ProjectData projectData = ((CSGeneratorData) app.getDataComponent()).getProjectData();
        projectData.addStudent(firstName, lastName, teamName, role);
        controller.markWorkAsEdited();
    }
    
}
