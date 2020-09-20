package serialization;

import util.Settings;
import world.customizables.Build;
import java.io.*;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Map.Entry;
import javax.json.*;
import world.customizables.BuildJsonUtil;

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
        try {
            BufferedReader buff = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
            StringBuilder sb = new StringBuilder();
            JsonReaderFactory jrf = Json.createReaderFactory(null);
            JsonObject obj = null;
            /*
            JsonParserFactory jpf = Json.createParserFactory(null);
            JsonParser parser = jpf.createParser(new FileInputStream(f));
            while(parser.hasNext()){
                //this can parse multiple it looks like
            }*/
            
            
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
    public static void writeToFile(JsonObject obj, File file){
        try {
            FileWriter write = new FileWriter(file);
            write.append(obj.toString());
            write.close();
        } catch (IOException ex) {
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
    
    public static void pprint(JsonObject obj, int indentLevel){
        String indent = "";
        for(int i = 0; i < indentLevel; i++){
            indent += " ";
        }
        out.println(indent + "{");
        for(Entry<String, JsonValue> val : obj.entrySet()){
            out.print("    " + indent + val.getKey() + ": ");
            if(val.getValue() instanceof JsonObject){
                pprint((JsonObject)val.getValue(), indentLevel + 4);
            } else if (val.getValue() instanceof JsonArray) {
                out.println("[");
                for(JsonValue j : (JsonArray)val.getValue()){
                    if(j.getValueType().equals(JsonValue.ValueType.OBJECT)){
                        pprint((JsonObject)j, indentLevel + 4);
                    } else {
                        out.println("        " + indent + j);
                    }
                }
                out.println("    " + indent + "]");
            } else {
                out.println(val.getValue().toString());
            }
        };
        out.println(indent + "}");
    }
    
    public static void main(String[] args) throws Exception{
        JsonObject obj = null;
        Build b = null;
        
        for(Build bu : Settings.getDataSet().getAllBuilds()){
            obj = BuildJsonUtil.serializeJson(bu);
            pprint(obj, 0);
            b = BuildJsonUtil.deserializeJson(obj);
            if(b != null){
                pprint(obj, 0);
                out.println(bu.getDescription());
                out.println(b.getDescription());
            }
        }
    }
}
