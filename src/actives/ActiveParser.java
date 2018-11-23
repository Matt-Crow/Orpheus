package actives;

import java.io.*;
import static java.lang.System.out;

public class ActiveParser {
    public void readFile(File f){
        try {
            BufferedReader ip = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            while(ip.ready()){
                out.println(ip.readLine());
            }
        } catch (FileNotFoundException e) {
            out.println("File not found: " + f.getName());
            e.printStackTrace();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }
}
