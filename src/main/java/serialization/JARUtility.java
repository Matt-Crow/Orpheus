package serialization;

import gui.FileChooserUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileObject.Kind;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

/**
 * I cannot get this to work.
 * Basically, this is supposed to do the following:
 * 1. take an input file or directory, IN,
 * 2. take an output directory, OUT,
 * 3. take the path to Orpheus.jar, ORPH,
 * 4. run javac IN_FILE -classpath ORPH -d OUT on each of the given files in IN
 * 5. run jar cvf JARNAME.jar OUT_CLASSES, where OUT_CLASSES is all .class files generated by step 4
 * @author Matt Crow
 */
public class JARUtility {    
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
                //https://stackoverflow.com/questions/53187175/compile-java-file-at-runtime
                StandardJavaFileManager fileManager = javac.getStandardFileManager(null, Locale.ENGLISH, Charset.defaultCharset());
                ArrayList<String> options = new ArrayList<>();
                ArrayList<JavaFileObject> files = new ArrayList<>();
                //files.add(new SimpleJavaFileObject(compileMe.toURI(), Kind.SOURCE));
                javac.getTask(null, fileManager, null, options, null, files);
                /*
                javac.run(
                    null,
                    null,
                    null,
                    new String[]{String.format("\"%s\"", compileMe.getAbsolutePath())}
                    //String.format(" -d \"%s\" -classpath \"%s\" \"%s\"", outputDir.getAbsolutePath(), orpheusPath, compileMe.getAbsolutePath())
                );*/
            }
        }
    }
    
    public static void main(String[] args) throws IOException{
        compile();
    }
}
