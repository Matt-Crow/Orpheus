package serialization;

import gui.FileChooserUtil;
import java.io.File;
import static java.lang.System.out;
import java.util.Scanner;

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
            out.println("1: Create a new set of customizables");
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
    
    public static void main(String[] args){
        run();
    }
}
