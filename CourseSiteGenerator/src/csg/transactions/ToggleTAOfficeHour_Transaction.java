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
import csg.data.TAData;
import csg.workspace.CSGWorkspace;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class ToggleTAOfficeHour_Transaction implements jTPS_Transaction {

    CSGeneratorApp app;
    String cellKey;
    String taName;

    public ToggleTAOfficeHour_Transaction(CSGeneratorApp app, String cellKey, String taName) {
        this.app = app;
        this.cellKey = cellKey;
        this.taName = taName;
    }

    @Override
    public void doTransaction() {
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        TAData data = ((CSGeneratorData) app.getDataComponent()).getTAData();
        CSGController controller = workspace.getCSGController();
        data.toggleTAOfficeHours(cellKey, taName);
        controller.markWorkAsEdited();
    }
    
    @Override
    public void undoTransaction() {
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        TAData data = ((CSGeneratorData) app.getDataComponent()).getTAData();
        CSGController controller = workspace.getCSGController();
        data.toggleTAOfficeHours(cellKey, taName);
        controller.markWorkAsEdited();
    }

}
