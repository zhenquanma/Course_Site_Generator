/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import af.utilities.jTPS_Transaction;
import csg.CSGeneratorApp;
import csg.data.CSGeneratorData;
import csg.data.ScheduleData;
import csg.utilities.ScheduleItem;
import csg.workspace.CSGWorkspace;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class AddSchedule_Transaction implements jTPS_Transaction {

    CSGeneratorApp app;
    String type;
    String date;
    String time;
    String title;
    String topic;
    String link;
    String criteria;

    public AddSchedule_Transaction(CSGeneratorApp app, String type, String date, String time, String title, String topic, String link, String criteria) {
        this.app = app;
        this.type = type;
        this.date = date;
        this.time = time;
        this.title = title;
        this.topic = topic;
        this.link = link;
        this.criteria = criteria;
    }

    @Override
    public void doTransaction() {
        ScheduleData scheduleData = ((CSGeneratorData) app.getDataComponent()).getScheduleData();
        scheduleData.addScheduleItem(type, date, time, title, topic, link, criteria);
        ((CSGWorkspace) app.getWorkspaceComponent()).getCSGController().markWorkAsEdited();
    }

    @Override
    public void undoTransaction() {
        ScheduleData scheduleData = ((CSGeneratorData) app.getDataComponent()).getScheduleData();
        scheduleData.removeSchedule(date, title);
        ((CSGWorkspace) app.getWorkspaceComponent()).getCSGController().markWorkAsEdited();
    }

}
