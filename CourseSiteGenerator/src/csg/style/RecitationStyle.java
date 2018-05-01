/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.style;

import csg.CSGeneratorApp;
import csg.workspace.CSGWorkspace;
import csg.workspace.RecitationDataPage;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class RecitationStyle {

    public static String CLASS_BASE_PANE = "background_pane";
    public static String CLASS_BLOCK_PANE = "block_pane";
    public static String CLASS_HEADER_BOX = "header_box";
    public static String CLASS_ADD_EDIT_PANE="add_edit_pane"; 

    private CSGeneratorApp app;

    public RecitationStyle(CSGeneratorApp app) {
        this.app = app;
        initRecitationStyle();
    }

    private void initRecitationStyle() {
        CSGWorkspace workspaceComponent = (CSGWorkspace) app.getWorkspaceComponent();
        RecitationDataPage recitationDataPage = workspaceComponent.getRecitationDataPage();
        recitationDataPage.getPage().getStyleClass().add(CLASS_BASE_PANE);
        recitationDataPage.getRecitaitonHeaderBox().getStyleClass().add(CLASS_HEADER_BOX);
        recitationDataPage.getAddEditGridPane().getStyleClass().add(CLASS_ADD_EDIT_PANE);

    }
}
