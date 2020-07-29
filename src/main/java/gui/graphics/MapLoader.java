package gui.graphics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 *
 * @author Matt
 */
public class MapLoader {
    public static Map readCsv(InputStream s) throws IOException{
        BufferedReader r = new BufferedReader(new InputStreamReader(s));
        String data = "";
        String[] split;
        ArrayList<String[]> lines = new ArrayList<>();
        int width = 0;
        int height = 0;
        Map ret = null;
        //get the size of the map
        while(r.ready()){
            data = r.readLine();
            height++;
            split = data.split(",");
            if(split.length > width){
                width = split.length;
            }
            lines.add(split);
        }
        ret = new Map(width, height);
        r.close();
        //some way to reset so I don't have to create my own buffer?
        
        
        for(int y = 0; y < height; y++){
            split = lines.get(y);
            for(int x = 0; x < width && x < split.length; x++){
                //System.out.print(split[x] + " ");
                ret.setTile(x, y, Integer.parseInt(split[x].trim()));
            }
            //System.out.println();
        }
        
        return ret;
    }
    public static void saveCsv(OutputStream os, Map m){
        
        try {
            BufferedWriter buff = new BufferedWriter(new OutputStreamWriter(os));
            buff.write(m.getCsv());
            buff.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
}
