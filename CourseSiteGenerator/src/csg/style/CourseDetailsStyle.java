/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.style;

import csg.CSGeneratorApp;
import csg.workspace.CSGWorkspace;
import csg.workspace.CourseDetailPage;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class CourseDetailsStyle {

    public static String CLASS_BASE_PANE = "background_pane";
    public static String CLASS_BLOCK_PANE ="block_pane";

    private CSGeneratorApp app;

    public CourseDetailsStyle(CSGeneratorApp app) {
        this.app = app;
        initCouseDetailsPageStyle();
    }

    private void initCouseDetailsPageStyle() {
        CSGWorkspace workspaceComponent = (CSGWorkspace) app.getWorkspaceComponent();
        CourseDetailPage courseDetailPage = workspaceComponent.getCourseDetailPage();
        courseDetailPage.getPage().getStyleClass().add(CLASS_BASE_PANE);
        courseDetailPage.getCourseInfoPane().getStyleClass().add(CLASS_BLOCK_PANE);
        courseDetailPage.getSiteTemplatePane().getStyleClass().add(CLASS_BLOCK_PANE);
        courseDetailPage.getPageStylePane().getStyleClass().add(CLASS_BLOCK_PANE);
    }
}
