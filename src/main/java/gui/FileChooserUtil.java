package gui;

import java.io.File;
import javax.swing.JFileChooser;

/**
 *
 * @author Matt
 */
public class FileChooserUtil {
    public static File[] chooseFiles(){
        File[] f = null;
        JFileChooser choose = new JFileChooser();
        choose.setFileSelectionMode(JFileChooser.FILES_ONLY);
        choose.setMultiSelectionEnabled(true);
        if(choose.showOpenDialog(choose) == JFileChooser.APPROVE_OPTION){
            f = choose.getSelectedFiles();
        }
        return f;
    }
    public static File chooseDir(){
        File f = null;
        JFileChooser choose = new JFileChooser();
            choose.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            
            if(choose.showOpenDialog(choose) == JFileChooser.APPROVE_OPTION){
                f = choose.getSelectedFile();
            }
        return f;
    }
}
