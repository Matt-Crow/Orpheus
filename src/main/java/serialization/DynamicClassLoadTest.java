package serialization;

import customizables.AbstractCustomizable;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/*
Creating JAR files for customizables:

Looks like giving them a package causes problems.

javac xxx.java -classpath path/to/Orpheus.jar
jar cvf Customizables.jar xxx.class xxxx.class etc
*/


/**
 *
 * @author Matt
 */
public class DynamicClassLoadTest {
    public static void main(String[] args) throws ClassNotFoundException, IOException{
        String jarPath = "C:\\Users\\Matt\\Documents\\GitHub\\Orpheus\\src\\main\\resources\\Revitalize.jar";
        JarFile jar = new JarFile(jarPath);
        URL[] urls = {
            new URL("jar:file:" + jarPath + "!/")
        };
        URLClassLoader loader = URLClassLoader.newInstance(urls);
        
        Enumeration<JarEntry> entries = jar.entries();
        JarEntry entry;
        String className;
        Class c;
        Object obj;
        while(entries.hasMoreElements()){
            entry = entries.nextElement();
            if(!entry.isDirectory() && entry.getName().endsWith(".class")){
                className = entry.getName().replace(".class", "").replace('/', '.');
                System.out.println(className);
                c = loader.loadClass(className);
                try {
                    obj = c.newInstance();
                    if(obj instanceof AbstractCustomizable){
                        System.out.println(((AbstractCustomizable)obj).getDescription());
                    }
                } catch (InstantiationException ex) {
                    ex.printStackTrace();
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
