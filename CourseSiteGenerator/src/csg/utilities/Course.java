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
public class Course {
    private StringProperty subject;
    private StringProperty number;
    private StringProperty semester;
    private StringProperty year;
    private StringProperty title;
    private StringProperty instructorName;
    private StringProperty instructorHome;
    
    public Course() {
        subject = new SimpleStringProperty("");
        number = new SimpleStringProperty("");
        semester = new SimpleStringProperty("");
        year = new SimpleStringProperty("");
        title = new SimpleStringProperty("");
        instructorName = new SimpleStringProperty("");
        instructorHome = new SimpleStringProperty("");
    }

    private Course(final String subject, final String number,
                 final String semester, final String year,
                 final String title, final String instructorName,
                 final String instructorHome) {
        this.subject = new SimpleStringProperty(subject);
        this.number = new SimpleStringProperty(number);
        this.semester = new SimpleStringProperty(semester);
        this.year = new SimpleStringProperty(year);
        this.title = new SimpleStringProperty(title);
        this.instructorName = new SimpleStringProperty(instructorName);
        this.instructorHome = new SimpleStringProperty(instructorHome);
    }
    
    //ACCESSORS AND MUTATORS
    public String getSubject() { return subject.get(); }
    
    public String getNumber() { return number.get(); }
    
    public String getSemester() { return semester.get(); }
    
    public String getYear() { return year.get(); }
    
    public StringProperty getTitlePro() { return title; }
    
    public String getTitle() { return title.get(); }
    
    public StringProperty subjectProperty() { return subject; }
    
    public StringProperty numberProperty() { return number; }
    
    public StringProperty semesterProperty() { return semester; }
    
    public StringProperty yearProperty() { return year; }
    
    public StringProperty titleProperty() { return title; }
    
    public StringProperty instructorNameProperty() { return instructorName; }
    
    public String getInstructorName() { return instructorName.get(); }
    
    public StringProperty instructorHomeProperty() { return instructorHome; }
    
    public String getInstructorHome() { return instructorHome.get(); }
    
    public void setSubject(String subject) { this.subject.set(subject); }
    
    public void setNumber(String number) { this.number.set(number); }
    
    public void setSemester(String semester) { this.semester.set(semester); }
    
    public void setYear(String year) { this.year.set(year); }
    
    public void setTitle(String title) { this.title.set(title); }
    
    public void setInstructorName(String instructorName) { this.instructorName.set(instructorName); }
    
    public void setInstructorHome(String instructorHome) { this.instructorHome.set(instructorHome); }
    
    public static class CourseBuilder {

        private String nestedSubject;
        private String nestedNumber;
        private String nestedSemester;
        private String nestedYear;
        private String nestedTitle;
        private String nestedInstructorName;
        private String nestedInstructorHome;
        
        public CourseBuilder(){}
        
        public CourseBuilder subject(String subject) {
            nestedSubject = subject;
            return this;
        }
        
        public CourseBuilder number(String number) {
            nestedNumber = number;
            return this;
        }
        
        public CourseBuilder semester(String semester) {
            nestedSemester = semester;
            return this;
        }
        
        public CourseBuilder year(String year) {
            nestedYear = year;
            return this;
        }
        
        public CourseBuilder title(String title) {
            nestedTitle = title;
            return this;
        }
        
        public CourseBuilder instructorName(String instructorName) {
            nestedInstructorName = instructorName;
            return this;
        }
        
        public CourseBuilder instructorHome(String instructorHome) {
            nestedInstructorHome = instructorHome;
            return this;
        }
        
        public Course buildCourse() {
            return new Course(nestedSubject, nestedNumber, nestedSemester, nestedYear,
                    nestedTitle, nestedInstructorName, nestedInstructorHome);
        }
    }
    
    @Override
    public String toString() {
        return subject + " " + number;
    }
 
}
