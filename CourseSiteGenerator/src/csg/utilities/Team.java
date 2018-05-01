/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.utilities;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class Team<E extends Comparable<E>> implements Comparable<E> {
    
    private StringProperty name;
    private StringProperty color;
    private StringProperty textColor;
    private StringProperty link;

    public Team(String name, String color, String textColor, String link) {
        this.name = new SimpleStringProperty(name);
        this.color = new SimpleStringProperty(color);
        this.textColor = new SimpleStringProperty(textColor);
        this.link = new SimpleStringProperty(link);
    }

    /**
     * @return the name
     */
    public String getName() {
        return name.get();
    }

    /**
     * @return the color
     */
    public String getColor() {
        return color.get();
    }

    /**
     * @return the textColor
     */
    public String getTextColor() {
        return textColor.get();
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link.get();
    }
    

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name.set(name);
    }

    /**
     * @param color the color to set
     */
    public void setColor(String color) {
        this.color.set(color);
    }

    /**
     * @param textColor the textColor to set
     */
    public void setTextColor(String textColor) {
        this.textColor.set(textColor);
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link.set(link);
    }
    
    @Override
    public int compareTo(E otherTeam) {
        return getName().compareTo(((Team) otherTeam).getName());
    }
    
    @Override
    public String toString() {
        return name.get();
    }
    
}
