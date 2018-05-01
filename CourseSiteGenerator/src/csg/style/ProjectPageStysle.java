/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.style;

import csg.CSGeneratorApp;
import csg.workspace.CSGWorkspace;
import csg.workspace.ProjectDataPage;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class ProjectPageStysle {
    public static String CLASS_BASE_PANE = "background_pane";
    public static String CLASS_BLOCK_PANE = "block_pane";
    public static String CLASS_HEADER_BOX = "header_box";
        public static String CLASS_ADD_EDIT_PANE="add_edit_pane"; 

    private CSGeneratorApp app;

    public ProjectPageStysle(CSGeneratorApp app) {
        this.app = app;
        initProjectPageStysle();
    }

    private void initProjectPageStysle() {
        CSGWorkspace workspaceComponent = (CSGWorkspace) app.getWorkspaceComponent();
        ProjectDataPage projectDataPage = workspaceComponent.getProjectDataPage();
        projectDataPage.getPage().getStyleClass().add(CLASS_BASE_PANE);
        projectDataPage.getProjectHeaderBox().getStyleClass().add(CLASS_HEADER_BOX);
        projectDataPage.getTeamsPane().getStyleClass().add(CLASS_BLOCK_PANE);
        projectDataPage.getStudentsPane().getStyleClass().add(CLASS_BLOCK_PANE);
        projectDataPage.getTeamsAddEditPane().getStyleClass().add(CLASS_ADD_EDIT_PANE);
        projectDataPage.getStudentsAddEditPane().getStyleClass().add(CLASS_ADD_EDIT_PANE);
        

    }
}
