package csg.utilities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class Recitation<E extends Comparable<E>> implements Comparable<E>{
    
    private StringProperty section;
    private StringProperty instructor;
    private StringProperty dayTime;
    private StringProperty location;
    private StringProperty ta1;
    private StringProperty ta2;

    public Recitation() {
        section = new SimpleStringProperty();
        instructor = new SimpleStringProperty();
        dayTime = new SimpleStringProperty();
        location = new SimpleStringProperty();
        ta1 = new SimpleStringProperty();
        ta2 = new SimpleStringProperty();
    }
    
    public Recitation(String section, String instructor, String dayTime, String location, String ta1, String ta2) {
        this.section = new SimpleStringProperty(section);
        this.instructor = new SimpleStringProperty(instructor);
        this.dayTime = new SimpleStringProperty(dayTime);
        this.location = new SimpleStringProperty(location);
        this.ta1 = new SimpleStringProperty(ta1);
        this.ta2 = new SimpleStringProperty(ta2);
    }

    /**
     * @return the section
     */
    public String getSection() {
        return section.get();
    }

    /**
     * @return the instructor
     */
    public String getInstructor() {
        return instructor.get();
    }

    /**
     * @return the dayTime
     */
    public String getDayTime() {
        return dayTime.get();
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location.get();
    }

    /**
     * @return the name of TA 1
     */
    public String getTa1() {
        return ta1.get();
    }

    /**
     * @return the name of TA 2
     */
    public String getTa2() {
        return ta2.get();
    }

    /**
     * @param section the section to set
     */
    public void setSection(String section) {
        this.section.set(section);
    }

    /**
     * @param instructor the instructor to set
     */
    public void setInstructor(String instructor) {
        this.instructor.set(instructor);
    }

    /**
     * @param dayTime the dayTime to set
     */
    public void setDayTime(String dayTime) {
        this.dayTime.set(dayTime);
    }

    /**
     * @param location the location to set
     */
    public void setLocation(String location) {
        this.location.set(location);
    }

    /**
     * @param ta1 the ta1 to set
     */
    public void setTa1(String ta1) {
        this.ta1.set(ta1);
    }

    /**
     * @param ta2 the ta2 to set
     */
    public void setTa2(String ta2) {
        this.ta2.set(ta2);
    }
    
    @Override
    public int compareTo(E otherRecitation) {
        return getSection().compareTo(((Recitation)otherRecitation).getSection());
    }

}
