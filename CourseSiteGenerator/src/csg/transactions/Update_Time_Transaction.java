/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import af.utilities.jTPS_Transaction;
import csg.data.TAData;
import java.util.HashMap;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class Update_Time_Transaction implements jTPS_Transaction {
    
    private int start;
    private int end;
    private int orgStart;
    private int orgEnd;
    private TAData data;    
    private HashMap<String, String> stringMap;    
    
    private HashMap<String, StringProperty> officeHours;    
    
    public Update_Time_Transaction(int start, int end, TAData data) {
        
        stringMap = new HashMap();        
        this.start = start;
        this.end = end;        
        this.data = data;        
        orgStart = data.getStartHour();
        orgEnd = data.getEndHour();
        officeHours = data.getOfficeHours();
        for (HashMap.Entry<String, StringProperty> entry : officeHours.entrySet()) {
            String key = entry.getKey();
            StringProperty prop = officeHours.get(key);
            String cellText = prop.getValue();            
            stringMap.put(key, cellText);            
            
        }
        
    }
    
    @Override
    public void doTransaction() {
        data.updateTime(start, end);        
    }
    
    @Override
    public void undoTransaction() {
        data.updateTime(orgStart, orgEnd);
        for (HashMap.Entry<String, String> entry : stringMap.entrySet()) {
            String key = entry.getKey();            
            String cellText = stringMap.get(key);            
            StringProperty prop = officeHours.get(key);            
            prop.setValue(cellText);
        }
        
    }
    
}

