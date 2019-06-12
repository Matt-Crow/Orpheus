package serialization;

import actives.*;
import customizables.Build;
import customizables.CharacterClass;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import static java.lang.System.out;
import java.util.ArrayList;
import java.util.Map.Entry;
import javax.json.*;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParserFactory;
import passives.*;
import upgradables.AbstractUpgradable;

/**
 *
 * @author Matt
 */
public class JsonTest {
    
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
        AbstractUpgradable u = null;
        Build b = null;
        AbstractUpgradable.loadAll();
        Build.loadAll();
        
        //done
        for(AbstractActive aa : AbstractActive.getAll()){
            out.println(aa.getName());
            obj = aa.serializeJson();
            //pprint(obj, 0);
            u = ActiveJsonUtil.deserializeJson(obj);
            if(u != null){
                pprint(obj, 0);
                out.println(aa.getDescription());
                out.println(u.getDescription());
            } else {
                out.println("NULL");
            }
        }
        
        //done
        for(AbstractPassive ap : AbstractPassive.getAll()){
            obj = ap.serializeJson();
            pprint(obj, 0);
            u = AbstractPassive.deserializeJson(obj);
            if(u != null){
                pprint(obj, 0);
                out.println(ap.getDescription());
                out.println(u.getDescription());
            }
        }
        
        //done
        for(CharacterClass cc : CharacterClass.getAll()){
            obj = cc.serializeJson();
            pprint(obj, 0);
            u = CharacterClass.deserializeJson(obj);
            if(u != null){
                pprint(obj, 0);
                out.println(cc.getDescription());
                out.println(u.getDescription());
            }
        }
        
        for(Build bu : Build.getAllBuilds()){
            obj = bu.serializeJson();
            pprint(obj, 0);
            b = Build.deserializeJson(obj);
            if(b != null){
                pprint(obj, 0);
                out.println(bu.getDescription());
                out.println(b.getDescription());
            }
        }
    }
}
