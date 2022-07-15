package serialization;

import java.io.*;
import java.util.ArrayList;

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
    
    /**
     * Since javax.json.JsonObject is immutable,
     * I need some way of creating an object builder from that object.
     * @param obj the object to undo the JsonObjectBuilder build method for
     * @return the JsonObjectBuilder which, when its build method is called, would return obj
     */
    public static JsonObjectBuilder deconstruct(JsonObject obj){
        JsonObjectBuilder b = Json.createObjectBuilder();
        obj.forEach((String key, JsonValue value)->{
            b.add(key, value);
        });
        return b;
    }
    
    /**
     * Throws an exception if the given object 
     * is missing the given key.
     * That's all this does.
     * @param obj the object to check
     * @param key the key to look for
     */
    public static void verify(JsonObject obj, String key){
        if(!obj.containsKey(key)){
            throw new JsonException("Json Object is missing key \'" + key + "\'");
        }
    }
}
