package csg.data;

import af.components.AppDataComponent;
import csg.CSGeneratorApp;
import csg.utilities.Recitation;
import csg.utilities.TeachingAssistant;
import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class RecitationData implements AppDataComponent{
    CSGeneratorApp app;
    
    ObservableList<Recitation> recitations;
    
    public RecitationData(CSGeneratorApp initApp) {
        app = initApp;
        recitations = FXCollections.observableArrayList();
    }
    
    
    @Override
    public void resetData() {
        recitations.clear();
    }
    
    public ObservableList getRecitations() {
        return recitations;
    }
    
    public void addRecitation(String section, String instructor, String dayTime,
            String location, String ta1, String ta2) {
        if (!containsRec(section)) {
            Recitation recitation = new Recitation(section, instructor, dayTime, location, ta1, ta2);
            recitations.add(recitation);
        }
        Collections.sort(recitations);
    }
    
    private boolean containsRec(String section) {
        for (Recitation rec : recitations) {
            if(section.equalsIgnoreCase(rec.getSection())) {
                return true;
            }
        }
        return false;
    }
    
    
    public void updateRecitation(String section, String newSection, String newInstructor, String newDayTime,
            String newLocation, String newTA1, String newTA2) {
        Recitation rec = this.getRec(section);
        rec.setSection(newSection);
        rec.setInstructor(newInstructor);
        rec.setDayTime(newDayTime);
        rec.setLocation(newLocation);
        rec.setTa1(newTA1);
        rec.setTa2(newTA2);
        
        int index = recitations.indexOf(rec);
        recitations.set(index, rec);
        Collections.sort(recitations);
    }
    
    public void removeRecitation(String section) {
        for (Recitation rec : recitations) {
            if (rec.getSection().equals(section)) {
                recitations.remove(rec);
                return;
            }
        }
    }
    
    /**
     * 
     * @return the recitation with the test section
     */
    public Recitation getRec(String testSection) {
        for (Recitation rec : recitations) {
            if (rec.getSection().equals(testSection)) {
                return rec;
            }
        }
        return null;
    }
    
    public boolean containsRecitation(String section) {
        for (Recitation rec : recitations) {
            if (rec.getSection().equals(section)) {
                return true;
            }
        }
        return false;
    }
    
    public boolean containsRecitationExclude(Recitation targetRec, String section) {
        for (Recitation rec : recitations) {
            if (!rec.equals(targetRec)) {
                if (rec.getSection().equals(section)) {
                    return true;
                }
            }
        }
        return false;
    }
}
