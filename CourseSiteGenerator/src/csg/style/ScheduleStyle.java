/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.style;

import csg.CSGeneratorApp;
import csg.workspace.CSGWorkspace;
import csg.workspace.ScheduleDataPage;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class ScheduleStyle {

    public static String CLASS_BASE_PANE = "background_pane";
    public static String CLASS_BLOCK_PANE = "block_pane";
    public static String CLASS_HEADER_BOX = "header_box";
    public static String CLASS_ADD_EDIT_PANE="add_edit_pane"; 

    private CSGeneratorApp app;

    public ScheduleStyle(CSGeneratorApp app) {
        this.app = app;
        initScheduleStyle();
    }

    private void initScheduleStyle() {
        CSGWorkspace workspaceComponent = (CSGWorkspace) app.getWorkspaceComponent();
        ScheduleDataPage scheduleDataPage = workspaceComponent.getScheduleDataPage();
        scheduleDataPage.getPage().getStyleClass().add(CLASS_BASE_PANE);
        scheduleDataPage.getScheduleHeaderBox().getStyleClass().add(CLASS_HEADER_BOX);
        scheduleDataPage.getCalendarBoundariesPane().getStyleClass().add(CLASS_BLOCK_PANE);
        scheduleDataPage.getScheduleItemsPane().getStyleClass().add(CLASS_BLOCK_PANE);
        scheduleDataPage.getAddEditPane().getStyleClass().add(CLASS_ADD_EDIT_PANE);

    }
}

