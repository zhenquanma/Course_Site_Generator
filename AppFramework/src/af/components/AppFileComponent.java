/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package af.components;

import java.io.IOException;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public interface AppFileComponent {
    
    public void saveData(AppDataComponent data, String filePath) throws IOException;
    
    public void loadData(AppDataComponent data, String filePath) throws IOException;
    
    public void exportData(AppDataComponent data, String filePath) throws IOException;
    
}
