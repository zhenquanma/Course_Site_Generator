/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.transactions;

import af.utilities.jTPS_Transaction;
import csg.CSGeneratorApp;
import csg.utilities.TeachingAssistant;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class ToggleTAUndergrad_Transaction implements jTPS_Transaction{
    CSGeneratorApp app;
    TeachingAssistant ta;

    public ToggleTAUndergrad_Transaction(CSGeneratorApp app, TeachingAssistant ta) {
        this.app = app;
        this.ta = ta;
    }
    
    @Override
    public void doTransaction() {
        
    }
    
    @Override
    public void undoTransaction() {
        
    }
    
    
}
