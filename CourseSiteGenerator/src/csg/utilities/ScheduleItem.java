package csg.utilities;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class ScheduleItem<E extends Comparable<E>> implements Comparable<E> {
    
    private StringProperty type;
    private StringProperty date;
    private StringProperty title;
    private StringProperty topic;
    private String time;
    private String link;
    private String criteria;

    public ScheduleItem(String type, String date, String title, String topic,
            String time, String link, String citeria) {
        this.type = new SimpleStringProperty(type);
        this.date = new SimpleStringProperty(date);
        this.title = new SimpleStringProperty(title);
        this.topic = new SimpleStringProperty(topic);
        this.time = time;
        this.link = link;
        this.criteria = citeria;
    }
    
    
    /**
     * @return the type
     */
    public String getType() {
        return type.get();
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date.get();
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title.get();
    }

    /**
     * @return the topic
     */
    public String getTopic() {
        return topic.get();
    }

    /**
     * @return the time
     */
    public String getTime() {
        return time;
    }

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @return the criteria
     */
    public String getCriteria() {
        return criteria;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type.set(type);
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date.set(date);
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title.set(title);
    }

    /**
     * @param topic the topic to set
     */
    public void setTopic(String topic) {
        this.topic.set(time);
    }

    /**
     * @param time the time to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @param criteria the criteria to set
     */
    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }
    
    @Override
    public int compareTo(E otherScheduleItem) {
        if (getDate().equals(((ScheduleItem) otherScheduleItem).getDate())) {
            return getTitle().compareTo(((ScheduleItem) otherScheduleItem).getTitle());
        }
        else
            return -1;
    }

}
