package serialization;

import gui.FileChooserUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
                    try {
                        compile();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
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
    
    public static void compile() throws IOException{
        FileChooserUtil.chooseDir("Select the directory to compile", (File compileMe)->{
            FileChooserUtil.chooseDir("Choose directory to send compiled files to", (File outputDir)->{
                FileChooserUtil.chooseJarFile("Select the Orpheus.jar file you downloaded", (File orpheus)->{
                    try {
                        compile(ToolProvider.getSystemJavaCompiler(), compileMe, outputDir, orpheus.getAbsolutePath());
                    } catch (FileNotFoundException ex) {
                        ex.printStackTrace();
                    }
                });
            });
        });
    }
    
    public static void compile(JavaCompiler javac, File compileMe, File outputDir, String orpheusPath) throws FileNotFoundException{
        if(compileMe.exists()){
            if(compileMe.isDirectory()){
                for(File child : compileMe.listFiles()){
                    compile(javac, child, outputDir, orpheusPath);
                }
            } else {
                String className = compileMe.getName().replace(".java", ".class");
                Path newPath = Paths.get(outputDir.getAbsolutePath(), className);
                //javac.run(new FileInputStream(compileMe), new FileOutputStream(newPath.toFile()), System.err, "classpath=" + orpheusPath);
                javac.run(
                    System.in, 
                    System.out, 
                    System.err, 
                    new String[]{String.format("\"%s\"", compileMe.getAbsolutePath())}
                    //String.format(" -d \"%s\" -classpath \"%s\" \"%s\"", outputDir.getAbsolutePath(), orpheusPath, compileMe.getAbsolutePath())
                );
            }
        }
    }
    
    public static void main(String[] args){
        run();
    }
}
