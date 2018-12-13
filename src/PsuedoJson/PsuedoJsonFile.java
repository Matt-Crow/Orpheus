package PsuedoJson;


import java.io.*;
import java.nio.file.Files;

public class PsuedoJsonFile extends File{
    
    public PsuedoJsonFile(String pathname) {
        super(pathname);
        System.out.println(this.getAbsolutePath());
    }
    public PsuedoJsonFile(String pathname, PsuedoJsonObject[] objs){
        this(pathname);
        writeObjects(objs);
    }
    
    public void writeObjects(PsuedoJsonObject[] objs){
        try {
            FileWriter write = new FileWriter(this);
            for(PsuedoJsonObject obj : objs){
                write.append(obj.toString());
            }
            write.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
