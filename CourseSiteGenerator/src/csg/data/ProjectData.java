package csg.data;

import af.components.AppDataComponent;
import csg.CSGeneratorApp;
import csg.utilities.Student;
import csg.utilities.Team;
import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class ProjectData implements AppDataComponent{
    
    CSGeneratorApp app;
    
    ObservableList<Team> teams;
    ObservableList<Student> students;
    
    
    public ProjectData(CSGeneratorApp initApp) {
        app = initApp;
        teams = FXCollections.observableArrayList();
        students = FXCollections.observableArrayList();
    }
    
    @Override
    public void resetData() {
        teams.clear();
        students.clear();
    }
    
    public ObservableList getTeams() {
        return teams;
    }
    
    public ObservableList getStudents() {
        return students;
    }
    
    public void addTeam(String name, String color, String textColor, String Link) {
        if (!containsTeam(name)) {
            Team team = new Team(name, color, textColor, Link);
            teams.add(team);
            Collections.sort(teams);
        }
        
    }
    
    public boolean containsTeam(String testName) {
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(testName)) {
                return true;
            }
        }
        return false;
    }
    
    public void addStudent(String firstName, String lastName, String team, String link) {
        if (!containsStudent(firstName, lastName)) {
            Student student = new Student(firstName, lastName, team, link);
            students.add(student);
            Collections.sort(students);
        }
    }
    
    public boolean containsStudent(String firstName, String lastName) {
        for (Student student : students) {
            if (student.getFirstName().equalsIgnoreCase(firstName)
                    && student.getLastName().equalsIgnoreCase(lastName)) {
                return true;
            }
        }
        return false;
    }
    
    public Team getTeam(String name) {
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(name)) {
                return team;
            }
        }
        return null;
    }
    
    public void updateTeam(String oldName, String newName, String newColor, String newTextColor, String newLink) {
        Team team = getTeam(oldName);
        team.setName(newName);
        team.setColor(newColor);
        team.setTextColor(newTextColor);
        team.setLink(newLink);
        int index = teams.indexOf(team);
        teams.set(index, team);
        Collections.sort(teams);
    }
    
    public void removeTeam(String name) {
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(name)) {
                teams.remove(team);
                return;
            }
        }
    }
    
    public Student getStudent(String firstName, String lastName){
        for (Student student : students) {
            if (student.getFirstName().equalsIgnoreCase(firstName)
                    && student.getLastName().equalsIgnoreCase(lastName)){
                return student;
            }
        }
        return null;
    }
    
    public void updateStudent(String oldFirstName, String oldLastName,
            String newFirstName, String newLastName, String newTeam, String newRole) {
        
        Student student = getStudent(oldFirstName, oldLastName);
        student.setFirstName(newFirstName);
        student.setLastName(newLastName);
        student.setTeamName(newTeam);
        student.setRole(newRole);
        int index = students.indexOf(student);
        students.set(index, student);
        Collections.sort(teams);
    }
    
    public void removeStudent(String firstName, String lastName) {
        Student student = getStudent(firstName, lastName);
        students.remove(student);
    }
}
