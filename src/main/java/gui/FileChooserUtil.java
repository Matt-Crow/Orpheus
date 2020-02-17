package gui;

import java.io.File;
import java.util.function.Consumer;
import javax.swing.JFileChooser;

/**
 *
 * @author Matt
 */
public class FileChooserUtil {
    private final JFileChooser chooser;
    private final Consumer<File> action;
    
    public FileChooserUtil(String text, FileType fileType, Consumer<File> act){
        chooser = new JFileChooser();
        chooser.setDialogTitle(text);
        chooser.setFileSelectionMode((fileType == FileType.DIR) ? JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_ONLY);
        action = act;
        chooser.setOpaque(true);
        chooser.setVisible(true);
    }
    public void chooseFile(){
        chooser.requestFocus();
        if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            action.accept(chooser.getSelectedFile());
        }
    }
    
    public static void chooseDir(String text, Consumer<File> action){
        new FileChooserUtil(text, FileType.DIR, action).chooseFile();
    }
    
    public static void chooseJsonFile(String text, Consumer<File> action){
        new FileChooserUtil(text, FileType.JSON, action).chooseFile();
    }
}
