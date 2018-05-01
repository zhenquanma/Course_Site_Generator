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
import csg.data.TAData;
import csg.utilities.Recitation;
import csg.workspace.CSGWorkspace;
import csg.workspace.RecitationDataPage;
import csg.workspace.TADataPage;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

/**
 *
 * @author Zhenquan
 */
public class DeleteTA_Transaction implements jTPS_Transaction{
    CSGeneratorApp app;
    String taName;
    String email;
    HashMap<Label, String> backupLabels;
    ArrayList<Recitation> backupRecitations;
    
    public DeleteTA_Transaction(CSGeneratorApp initApp, String taName, String email) {
        app = initApp;
        this.taName = taName;
        this.email = email;
        backupLabels = new HashMap<>();
        backupRecitations = new ArrayList<>();
    }
    
    @Override
    public void doTransaction() {
        // GET THE TABLE
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();

        TADataPage taDataPage = workspace.getTADataPage();
        TAData data = ((CSGeneratorData) app.getDataComponent()).getTAData();
        data.removeTA(taName);

        // AND BE SURE TO REMOVE ALL THE TA'S OFFICE HOURS
        HashMap<String, Label> labels = taDataPage.getOfficeHoursGridTACellLabels();
        for (Label label : labels.values()) {
            if (label.getText().equals(taName)
                    || label.getText().startsWith(taName + "\n")
                    || (label.getText().contains("\n" + taName + "\n"))
                    || (label.getText().endsWith("\n" + taName))) {
                backupLabels.put(label, label.getText());
                data.removeTAFromCell(label.textProperty(), taName);
            }
        }
        
        //CHECK RECITATION TABLE
        RecitationDataPage recPage = workspace.getRecitationDataPage();
        RecitationData recData = ((CSGeneratorData) app.getDataComponent()).getRecitationData();
        ObservableList<Recitation> recTableData = recData.getRecitations();
        for (Recitation rec : recTableData) {
            Recitation copyRec = new Recitation();
            if (rec.getTa1().equals(taName)) {               
                copyRec.setSection(rec.getSection());
                copyRec.setTa1(rec.getTa1());
                rec.setTa1("");
            }
            if (rec.getTa2().equals(taName)) {
                copyRec.setSection(rec.getSection());
                copyRec.setTa2(rec.getTa2());
                rec.setTa2("");
            }
            if (copyRec.getSection() != null) {
                backupRecitations.add(copyRec);
            }
        }
        recPage.getRecTable().refresh();
        
        // CLEAR THE TEXT FIELDS
        taDataPage.getNameTextField().setText("");
        taDataPage.getEmailTextField().setText("");

        workspace.getCSGController().taPageAddUpdateSwitch(false);
        workspace.getCSGController().markWorkAsEdited();
    }
    
    @Override
    public void undoTransaction() {
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();   
        TAData data = ((CSGeneratorData) app.getDataComponent()).getTAData();
        data.addTA(taName, email);
        
        //RECOVER NAMES ON THOSE DELETED LABELS
        for (Label label : backupLabels.keySet()) {
            label.textProperty().setValue(backupLabels.get(label));
        }

         //CHECK RECITATION TABLE
        RecitationDataPage recPage = workspace.getRecitationDataPage();
        RecitationData recData = ((CSGeneratorData) app.getDataComponent()).getRecitationData();
        ObservableList<Recitation> recTableData = recData.getRecitations();
        for (Recitation copyRec : backupRecitations) {
            for (Recitation rec : recTableData) {
                if (rec.getSection().equals(copyRec.getSection())) {
                    if (copyRec.getTa1() != null) {
                        rec.setTa1(copyRec.getTa1());
                    }
                    if (copyRec.getTa2() != null) {
                        rec.setTa2(copyRec.getTa2());
                    }
                }
            }   
        }
        recPage.getRecTable().refresh();
        workspace.getCSGController().markWorkAsEdited();

    }
}
