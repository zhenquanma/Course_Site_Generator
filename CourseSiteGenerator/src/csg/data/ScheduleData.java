package csg.data;

import af.components.AppDataComponent;
import csg.CSGeneratorApp;
import static csg.CSGeneratorProp.TYPES;
import csg.utilities.ScheduleItem;
import java.time.LocalDate;
import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import properties_manager.PropertiesManager;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class ScheduleData implements AppDataComponent{
    
    CSGeneratorApp app;
    
    ObservableList<ScheduleItem> scheduleItems;
    
    String startDate;
    String endDate;
    
    ObservableList<String> typeList;
    
    public final static String DATE_SEPARATOR = "-";
    
    public ScheduleData(CSGeneratorApp initApp) {
        app = initApp;
        scheduleItems = FXCollections.observableArrayList();
        startDate = LocalDate.now().toString();
        endDate = LocalDate.now().toString();
        typeList = FXCollections.observableArrayList();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        typeList.addAll(props.getPropertyOptionsList(TYPES));
    }
    
    @Override
    public void resetData() {
        scheduleItems.clear();
        startDate = LocalDate.now().toString();
        endDate = LocalDate.now().toString();
    }
    
    public ObservableList getScheduleItems() {
        return scheduleItems;
    }
    
    public String getStartDate() {
        return startDate;
    }
    
    public String getEndDate() {
        return endDate;
    }
    
    public int getStartYear() {
        return Integer.parseInt(startDate.substring(0, startDate.indexOf(DATE_SEPARATOR)));
    }
    
    public int getStartMonth() {
        return Integer.parseInt(startDate.substring(startDate.indexOf(DATE_SEPARATOR)+1,
                startDate.lastIndexOf(DATE_SEPARATOR)));
    }
    
    public int getStartDay() {
        return Integer.parseInt(startDate.substring(startDate.lastIndexOf(DATE_SEPARATOR)+1));
    }
    
    public int getEndYear() {
        return Integer.parseInt(endDate.substring(0, endDate.indexOf(DATE_SEPARATOR)));
    }
    
    public int getEndMonth() {
        return Integer.parseInt(endDate.substring(endDate.indexOf(DATE_SEPARATOR)+1,
                endDate.lastIndexOf(DATE_SEPARATOR)));
    }
    
    public int getEndDay() {
        return Integer.parseInt(endDate.substring(endDate.lastIndexOf(DATE_SEPARATOR)+1));
    }
    
    public void setStartDate(String date) {
        startDate = date;
    }
    
    public void setEndDate(String date) {
        endDate = date;
    }
    
    public void addScheduleItem(String type, String date, String time,
            String title, String topic, String link, String criteria) {

        if (!containsSchedule(date, title)) {
            ScheduleItem item = new ScheduleItem(type, date, title, topic,
                    time, link, criteria);
            scheduleItems.add(item);
        }
        Collections.sort(scheduleItems);
    }
    
    public boolean containsSchedule(String date, String title) {
        for (ScheduleItem item : scheduleItems) {
            if (item.getDate().equals(date) && item.getTitle().equals(title)) {
                return true;
            }
        }
        return false;
    }
    
    public void removeSchedule(String date, String title) {
        for(ScheduleItem item : scheduleItems) {
            if  (item.getDate().equals(date) && item.getTitle().equals(title)) {
                scheduleItems.remove(item);
                return;
            }
        }
    }
    
    public ScheduleItem getScheduleItem(String date, String title) {
         for(ScheduleItem item : scheduleItems) {
            if  (item.getDate().equals(date) && item.getTitle().equals(title)) {
                return item;
            }
        }
         return null;
    }
    
    public void updateScheule(String oldDate, String oldTitle, String newType, String newDate, String newTime, String newTitle,
            String newTopic, String newLink, String newCriteria) {
        ScheduleItem scheduleItem = this.getScheduleItem(oldDate, oldTitle);
        scheduleItem.setType(newType);
        scheduleItem.setDate(newDate);
        scheduleItem.setTime(newTime);
        scheduleItem.setTitle(newTitle);
        scheduleItem.setTopic(newTopic);
        scheduleItem.setLink(newLink);
        scheduleItem.setCriteria(newCriteria);
        int index = scheduleItems.indexOf(scheduleItem);
        scheduleItems.set(index, scheduleItem);
        Collections.sort(scheduleItems);
    }
    
    public ObservableList getTypeList() {
        return typeList;
    }
}
