package csg.transactions;

import af.utilities.jTPS_Transaction;
import csg.CSGeneratorApp;
import csg.controller.CSGController;
import csg.data.CSGeneratorData;
import csg.data.RecitationData;
import csg.data.TAData;
import csg.utilities.Recitation;
import csg.workspace.CSGWorkspace;
import csg.workspace.RecitationDataPage;
import csg.workspace.TADataPage;
import java.util.HashMap;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 *
 * @author Zhenquan Ma
 */
public class UpdateTA_Transaction implements jTPS_Transaction {

    CSGeneratorApp app;
    String oldName;
    String oldEmail;
    String newName;
    String newEmail;
    HashMap<Label, String> backupLabels;

    public UpdateTA_Transaction(CSGeneratorApp initApp, String oldName, String oldEmail,
            String newName, String newEmail) {
        app = initApp;
        this.oldName = oldName;
        this.oldEmail = oldEmail;
        this.newName = newName;
        this.newEmail = newEmail;
        backupLabels = new HashMap<>();
    }

    @Override
    public void doTransaction() {
        TAData data = ((CSGeneratorData) app.getDataComponent()).getTAData();
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        TADataPage taDataPage = workspace.getTADataPage();
        CSGController controller = workspace.getCSGController();
        // UPADTE THE TA TO THE DATA
        data.updataTA(oldName, newName, newEmail);

        HashMap<String, Label> labels = taDataPage.getOfficeHoursGridTACellLabels();
        for (Label label : labels.values()) {
            if (label.getText().equals(oldName)
                    || label.getText().startsWith(oldName + "\n")
                    || (label.getText().contains("\n" + oldName + "\n"))
                    || (label.getText().endsWith("\n" + oldName))) {
                backupLabels.put(label, label.getText());
                data.updateTAInCell(label.textProperty(), oldName, newName);
            }
        }
        
        //CHECK TAS IN RECITATION TABLE
        RecitationDataPage recPage = workspace.getRecitationDataPage();
        RecitationData recData = ((CSGeneratorData) app.getDataComponent()).getRecitationData();
        ObservableList<Recitation> recTableData = recData.getRecitations();
        for (Recitation rec : recTableData) {
            if (rec.getTa1().equals(oldName)) {
                rec.setTa1(newName);
            }
            if (rec.getTa2().equals(oldName)) {
                rec.setTa2(newName);
            }
        }
        recPage.getRecTable().refresh();

        //SWITCH THE BUTTON BACK TO THE STATUS OF ADD TA
        controller.taPageAddUpdateSwitch(false);
        // CLEAR THE TEXT FIELDS
        TextField nameTextField = taDataPage.getNameTextField();
        TextField emailTextField = taDataPage.getEmailTextField();
        nameTextField.setText("");
        emailTextField.setText("");

        // WE'VE CHANGED STUFF
        controller.markWorkAsEdited();
    }

    @Override
    public void undoTransaction() {
        CSGWorkspace workspace = (CSGWorkspace) app.getWorkspaceComponent();
        TADataPage taDataPage = workspace.getTADataPage();
        TAData data = ((CSGeneratorData) app.getDataComponent()).getTAData();
        data.updataTA(newName, oldName, oldEmail);
        for (Label label : backupLabels.keySet()) {
            label.textProperty().setValue(backupLabels.get(label));
        }
        
        //RECOVER RECITATION TABLE
        RecitationDataPage recPage = workspace.getRecitationDataPage();
        RecitationData recData = ((CSGeneratorData) app.getDataComponent()).getRecitationData();
        ObservableList<Recitation> recTableData = recData.getRecitations();
         for (Recitation rec : recTableData) {
            if (rec.getTa1().equals(newName)) {
                rec.setTa1(oldName);
            }
            if (rec.getTa2().equals(newName)) {
                rec.setTa2(oldName);
            }
        }
        recPage.getRecTable().refresh();
        
        TextField nameTextField = taDataPage.getNameTextField();
        TextField emailTextField = taDataPage.getEmailTextField();
        // CLEAR THE TEXT FIELDS
        nameTextField.setText("");
        emailTextField.setText("");
    }

}
