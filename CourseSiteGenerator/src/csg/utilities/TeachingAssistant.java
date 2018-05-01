package csg.utilities;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class represents a Teaching Assistant for the table of TAs.
 * 
 * @author Richard McKenna
 */
public class TeachingAssistant<E extends Comparable<E>> implements Comparable<E>  {
    // THE TABLE WILL STORE TA NAMES AND EMAILS
    private final StringProperty name;
    private final StringProperty email;
    private BooleanProperty isUndergrad;

    /**
     * Constructor initializes both the TA name and email.
     */
    public TeachingAssistant(String initName, String initEmail) {
        name = new SimpleStringProperty(initName);
        email = new SimpleStringProperty(initEmail);
        isUndergrad = new SimpleBooleanProperty(false);
    }

    // ACCESSORS AND MUTATORS FOR THE PROPERTIES
    

    public String getName() {
        return name.get();
    }
    
    
    
    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String initName) {
        name.set(initName);
    }

    public String getEmail() {
        return email.get();
    }
    
    public BooleanProperty undergradProperty() {
        return isUndergrad;
    }
    
    public boolean isUndergrad() {
        return isUndergrad.get();
    }
    
    public String checkUndergrad() {
        if (isUndergrad.get()) {
            return "true";
        }
        else
            return "false";
    }

    public void setEmail(String initEmail) {
        email.set(initEmail);
    }
    
    public void markAsUndergrad() {
        isUndergrad.set(true);
    }
    
    public void markAsGrad() {
        isUndergrad.set(false);
    }
    
    public void toggleUndergrad() {
        if (isUndergrad() == false) {
            isUndergrad.set(true);
        }
        else {
            isUndergrad.set(false);
        }
    }
    

    @Override
    public int compareTo(E otherTA) {
        return getName().compareTo(((TeachingAssistant)otherTA).getName());
    }
    
    @Override
    public String toString() {
        return name.getValue();
    }
}