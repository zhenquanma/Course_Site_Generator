package csg.utilities;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class SitePage {
    private BooleanProperty use;
    private StringProperty navbarTitle;
    private StringProperty fileName;
    private StringProperty script;

    public SitePage(boolean use, String navbarTitle, String fieldName, String script) {
        this.use = new SimpleBooleanProperty(use);
        this.navbarTitle = new SimpleStringProperty(navbarTitle);
        this.fileName = new SimpleStringProperty(fieldName);
        this.script = new SimpleStringProperty(script);
    }

    /**
     * @return the use
     */
    public BooleanProperty getUseProperty() {
        return use;
    }
    
    /**
     * 
     * @return if it is used
     */
    public boolean getUse() {
        return use.get();
    }
    
    public String getUseString() {
        if (getUse() == true) {
            return "true";
        } else {
            return "false";
        }
    }

    /**
     * @return the navbarTitle
     */
    public String getNavbarTitle() {
        return navbarTitle.get();
    }

    /**
     * @param navbarTitle the navbarTitle to set
     */
    public void setNavbarTitle(String navbarTitle) {
        this.navbarTitle.set(navbarTitle);
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName.get();
    }

    /**
     * @param fileName the fieldName to set
     */
    public void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    /**
     * @return the script
     */
    public String getScript() {
        return script.get();
    }

    /**
     * @param script the script to set
     */
    public void setScript(String script) {
        this.script.set(script);
    }
    
    public void toggleUse() {
        if (getUse() == false) {
            use.set(true);
        }
        else {
            use.set(false);
        }
    }
    
    public void setUse(boolean use) {
        this.use.set(use);
    }
}
