/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import af.utilities.jTPS_Transaction;
import csg.CSGeneratorApp;
import csg.data.CSGeneratorData;
import csg.data.RecitationData;
import csg.workspace.CSGWorkspace;
import csg.workspace.RecitationDataPage;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class DeleteRecitation_Transaction implements jTPS_Transaction{
    CSGeneratorApp app;
    String section;
    String instructor;
    String dayTime;
    String location;
    String ta1;
    String ta2;

    public DeleteRecitation_Transaction(CSGeneratorApp app, String section, String instructor, String dayTime, String location, String ta1, String ta2) {
        this.app = app;
        this.section = section;
        this.instructor = instructor;
        this.dayTime = dayTime;
        this.location = location;
        this.ta1 = ta1;
        this.ta2 = ta2;
    }
    
    @Override
    public void doTransaction() {
        RecitationDataPage recPage = ((CSGWorkspace) app.getWorkspaceComponent()).getRecitationDataPage();
        RecitationData recData = ((CSGeneratorData)app.getDataComponent()).getRecitationData();
        recData.removeRecitation(section);
        ((CSGWorkspace) app.getWorkspaceComponent()).getCSGController().markWorkAsEdited();
    }
    
    @Override
    public void undoTransaction() {
        RecitationData recData = ((CSGeneratorData)app.getDataComponent()).getRecitationData();
        recData.addRecitation(section, instructor, dayTime, location, ta1, ta2);
        ((CSGWorkspace) app.getWorkspaceComponent()).getCSGController().markWorkAsEdited();
    }
    
}
