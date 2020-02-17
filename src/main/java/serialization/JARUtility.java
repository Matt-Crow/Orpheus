package serialization;

import gui.FileChooserUtil;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import static java.lang.System.out;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 *
 * @author Matt Crow
 */
public class JARUtility {
    public static void run(){
        Scanner in = new Scanner(System.in);
        int ip = 0;
        do {
            out.println("***JAR UTILITY***");
            out.println("1: Create a new data set of customizables");
            out.println("2: Create a JAR file for a data set");
            out.println("-1: Quit");
            out.println("Choose an option: ");
            ip = in.nextInt();
            switch(ip){
                case 1:
                    FileChooserUtil.chooseDir("Where do you want to create the new data set?", (f)->{
                        in.nextLine();
                        out.println("Enter a name for this data set: ");
                        String name = in.nextLine();
                        createNewDataSet(f, name);
                    });
                    break;
                case 2:
                    FileChooserUtil.chooseDir("Choose the main folder of your data set", (f)->{
                        try {
                            compile(f);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                    break;
            }
            in.nextLine();
        } while(ip != -1);
        in.close();
    }
    
    public static File createNewDataSet(File parentDir, String name){
        File newDir = new File(parentDir + File.separator + name);
        if(newDir.mkdir()){
            File mainFolder = new File(newDir.getAbsolutePath() + File.separator + "customizables");
            if(mainFolder.mkdir()){
                new File(mainFolder.getAbsolutePath() + File.separator + "actives").mkdir();
                new File(mainFolder.getAbsolutePath() + File.separator + "characterClass").mkdir();
                new File(mainFolder.getAbsolutePath() + File.separator + "passives").mkdir();
            } else {
                System.err.println("Failed to create " + mainFolder.getAbsolutePath());
            }
        } else {
            System.err.println("Failed to create " + newDir.getAbsolutePath());
        }
        return newDir;
    }
    
    public static void compile(File root) throws IOException{
        Path customizablesSrc = Paths.get(root.getAbsolutePath(), "customizables");
        if(Files.exists(customizablesSrc)){
            Path bin = Files.createDirectory(Paths.get(root.getAbsolutePath(), "bin"));
            Path java = Files.createDirectory(Paths.get(bin.toString(), "java"));
            Path main = Files.createDirectory(Paths.get(java.toString(), "main"));
            Path custom = Files.createDirectory(Paths.get(main.toString(), "customizables"));
            
            JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
            
            //character classes
            Path characters = Files.createDirectory(Paths.get(custom.toString(), "characterClass"));
            for(File file : Paths.get(customizablesSrc.toString(), "characterClass").toFile().listFiles()){
                //javac.run
            }
        } else {
            System.err.println("Folder does not contain subfolder 'customizables'");
        }
    }
    
    public static void main(String[] args){
        run();
    }
}
