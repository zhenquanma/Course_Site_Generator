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
import csg.data.ScheduleData;
import csg.workspace.CSGWorkspace;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class UpdateSchedule_Transaction implements jTPS_Transaction {

    CSGeneratorApp app;
    CSGController controller;
    String oldType;
    String oldDate;
    String oldTime;
    String oldTitle;
    String oldTopic;
    String oldLink;
    String oldCriteria;
    String newType;
    String newDate;
    String newTime;
    String newTitle;
    String newTopic;
    String newLink;
    String newCriteria;

    public UpdateSchedule_Transaction(CSGeneratorApp app, String oldType, String oldDate, String oldTime, String oldTitle, String oldTopic, String oldLink, String oldCriteria, String newType, String newDate, String newTime, String newTitle, String newTopic, String newLink, String newCriteria) {
        this.app = app;
        this.oldType = oldType;
        this.oldDate = oldDate;
        this.oldTime = oldTime;
        this.oldTitle = oldTitle;
        this.oldTopic = oldTopic;
        this.oldLink = oldLink;
        this.oldCriteria = oldCriteria;
        this.newType = newType;
        this.newDate = newDate;
        this.newTime = newTime;
        this.newTitle = newTitle;
        this.newTopic = newTopic;
        this.newLink = newLink;
        this.newCriteria = newCriteria;
        controller = ((CSGWorkspace) app.getWorkspaceComponent()).getCSGController();
    }

    @Override
    public void doTransaction() {
        ScheduleData scheduleData = ((CSGeneratorData) app.getDataComponent()).getScheduleData();
        scheduleData.updateScheule(oldDate, oldTitle, newType, newDate, newTime, newTitle, newTopic, newLink, newCriteria);
        controller.markWorkAsEdited();
    }

    @Override
    public void undoTransaction() {
        ScheduleData scheduleData = ((CSGeneratorData) app.getDataComponent()).getScheduleData();
        scheduleData.updateScheule(newDate, newTitle, oldType, oldDate, oldTime, oldTitle, oldTopic, oldLink, oldCriteria);
        controller.markWorkAsEdited();
    }

}
