/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.utilities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class Student<E extends Comparable<E>> implements Comparable<E> {
    
    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty teamName;
    private StringProperty role;

    public Student(String firstName, String lastName, String teamName, String role) {
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.teamName = new SimpleStringProperty(teamName);
        this.role = new SimpleStringProperty(role);
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName.get();
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName.get();
    }

    /**
     * @return the teamName
     */
    public String getTeamName() {
        return teamName.get();
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role.get();
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    /**
     * @param teamName the teamName to set
     */
    public void setTeamName(String teamName) {
        this.teamName.set(teamName);
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role.set(role);
    }
    
    public String getFullName() {
        return firstName  + " " + lastName;
    }
    
    
    @Override
    public int compareTo(E otherStudent) {
        return this.getFullName().compareTo(((Student) otherStudent).getFullName());
    }
    
}
