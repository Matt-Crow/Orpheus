package serialization;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.json.*;

/**
 *
 * @author Matt
 */
public class JsonUtil {
    
    public static JsonObject fromString(String s){
        return Json.createReader(new StringReader(s)).readObject();
    }
    
    public static JsonObject[] readFromFile(File f){
        ArrayList<JsonObject> objs = new ArrayList<>();
        try (
            BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        ){
            StringBuilder sb = new StringBuilder();
            JsonReaderFactory jrf = Json.createReaderFactory(null);
            JsonObject obj = null;
            
            while(buff.ready()){
                sb.append(buff.readLine().trim());
                if(sb.charAt(sb.length() - 1) == '}'){
                    //at the end of the object. Maybe make this smarter
                    obj = jrf.createReader(new StringReader(sb.toString())).readObject();
                    objs.add(obj);
                    sb.delete(0, sb.length());
                }
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return objs.toArray(new JsonObject[objs.size()]);
    }
    
    public static void writeToFile(JsonObject[] objs, File file){
        try{
            FileWriter write = new FileWriter(file);
            for(JsonObject obj : objs){
                write.append(obj.toString());
                write.append("\n");
            }
            write.close();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public static List<String> toList(JsonArray json) {
        // JsonString::getString is not the same as JsonValue::toString
        return json.getValuesAs(JsonString::getString);
    }

    public static JsonArray toJsonArray(List<String> strings) {
        var builder = Json.createArrayBuilder();
        strings.forEach(builder::add);
        return builder.build();
    }
}
