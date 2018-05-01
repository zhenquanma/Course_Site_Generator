/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package csg.utilities;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public class FileFinder {   
   
    public static void getFiles(File dir, FilenameFilter filter, List<File> fileList) {
        
        File[] files = dir.listFiles();
        for (File file : files){
            if (file.isDirectory()) {
                getFiles(file, filter, fileList);
            }
            else if (filter.accept(dir, file.getName())){
                fileList.add(file);
            }           
        }
    }
}
