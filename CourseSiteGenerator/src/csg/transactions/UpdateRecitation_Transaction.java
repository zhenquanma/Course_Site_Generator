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
import csg.data.RecitationData;
import csg.workspace.CSGWorkspace;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class UpdateRecitation_Transaction implements jTPS_Transaction {

    CSGeneratorApp app;
    CSGController controller;

    String oldSection;
    String oldInstructor;
    String oldDayTime;
    String oldLocation;
    String oldTA1;
    String oldTA2;

    String newSection;
    String newInstructor;
    String newDayTime;
    String newLocation;
    String newTA1;
    String newTA2;

    public UpdateRecitation_Transaction(CSGeneratorApp app, String oldSection,
            String oldInstructor, String oldDayTime, String oldLocation, String oldTA1, String oldTA2,
            String newSection, String newInstructor, String newDayTime,
            String newLocation, String newTA1, String newTA2) {
        this.app = app;
        this.oldSection = oldSection;
        this.oldInstructor = oldInstructor;
        this.oldDayTime = oldDayTime;
        this.oldLocation = oldLocation;
        this.oldTA1 = oldTA1;
        this.oldTA2 = oldTA2;
        this.newSection = newSection;
        this.newInstructor = newInstructor;
        this.newDayTime = newDayTime;
        this.newLocation = newLocation;
        this.newTA1 = newTA1;
        this.newTA2 = newTA2;
        controller = ((CSGWorkspace) app.getWorkspaceComponent()).getCSGController();
    }

    @Override
    public void doTransaction() {
        RecitationData recData = ((CSGeneratorData) app.getDataComponent()).getRecitationData();
        recData.updateRecitation(oldSection, newSection, newInstructor, newDayTime, newLocation, newTA1, newTA2);
        controller.markWorkAsEdited();
    }

    @Override
    public void undoTransaction() {
        RecitationData recData = ((CSGeneratorData) app.getDataComponent()).getRecitationData();
        recData.updateRecitation(newSection, oldSection, oldInstructor, oldDayTime, oldLocation, oldTA1, oldTA2);
        controller.markWorkAsEdited();
    }

}
