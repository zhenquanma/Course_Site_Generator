package af.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Zhenquan
 */
public class FileCopier {
    final String source;
    final String target;
    
    public FileCopier(String source, String target) throws IOException {
        this.source = source;
        this.target = target;

    }
    
    public void start() throws IOException {
        copyDirectory(source, target);
    }
   
    //copy a files
    public static void copyFile(File sourceFile, File targetFile) throws IOException{
        FileInputStream input = new FileInputStream(sourceFile);
        BufferedInputStream inBuff = new BufferedInputStream(input);
        
        FileOutputStream outPut = new FileOutputStream(targetFile);
        BufferedOutputStream outBuff = new BufferedOutputStream(outPut);
        
        byte[] buffer = new byte[2048];
        int num;
        while ((num = inBuff.read(buffer)) != -1) {
            outBuff.write(buffer, 0, num);
        }
        outBuff.flush();
        
        inBuff.close();
        outBuff.close();
        outPut.close();
        input.close();        
    }
    
    public static void copyDirectory(String source, String target) throws IOException {
        (new File(target)).mkdirs();
        File[] files = (new File(source)).listFiles();
        for (int i=0; i<files.length; i++) {
            if (files[i].isFile()) {
                File sourceFile = files[i];
                File targetFile = new File(new File(target).getAbsolutePath()
                        + File.separator + files[i].getName());
                copyFile(sourceFile, targetFile);
            }
            if (files[i].isDirectory() && !files[i].getName().equals("public_html")) {
                String sourceDir = source + File.separator + files[i].getName();
                String targetDir = target + File.separator + files[i].getName();
                copyDirectory(sourceDir, targetDir);
            }
        }
    }
}
