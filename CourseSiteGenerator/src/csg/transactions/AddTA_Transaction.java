/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import af.utilities.jTPS_Transaction;
import csg.CSGeneratorApp;
import csg.data.CSGeneratorData;
import csg.data.TAData;
import csg.workspace.CSGWorkspace;
import csg.workspace.TADataPage;
import javafx.scene.control.TextField;

/**
 *
 * @author Zhenquan Ma
 */
public class AddTA_Transaction implements jTPS_Transaction {

    CSGeneratorApp app;
    String taName;
    String email;

    public AddTA_Transaction(CSGeneratorApp initApp, String taName, String email) {
        app = initApp;
        this.taName = taName;
        this.email = email;
    }

    @Override
    public void doTransaction() {
        TADataPage taDataPage = ((CSGWorkspace) app.getWorkspaceComponent()).getTADataPage();
        TextField nameTextField = taDataPage.getNameTextField();
        TextField emailTextField = taDataPage.getEmailTextField();

        // WE'LL NEED TO ASK THE DATA SOME QUESTIONS TOO
        TAData data = ((CSGeneratorData) app.getDataComponent()).getTAData();

        // ADD THE NEW TA TO THE DATA
        data.addTA(taName, email);

        // CLEAR THE TEXT FIELDS
        nameTextField.setText("");
        emailTextField.setText("");

        // WE'VE CHANGED STUFF
        ((CSGWorkspace) app.getWorkspaceComponent()).
                getCSGController().markWorkAsEdited();
    }

    @Override
    public void undoTransaction() {
        TAData data = ((CSGeneratorData) app.getDataComponent()).getTAData();
        data.removeTA(taName);

        TADataPage taDataPage = ((CSGWorkspace) app.getWorkspaceComponent()).getTADataPage();
        TextField nameTextField = taDataPage.getNameTextField();
        TextField emailTextField = taDataPage.getEmailTextField();
        nameTextField.setText("");
        emailTextField.setText("");

        ((CSGWorkspace) app.getWorkspaceComponent()).
                getCSGController().markWorkAsEdited();
    }

}
