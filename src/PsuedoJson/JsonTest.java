package PsuedoJson;

import actives.AbstractActive;
import actives.LoadActives;
import customizables.Build;
import customizables.CharacterClass;
import customizables.LoadCharacterClasses;
import java.io.StringReader;
import static java.lang.System.out;
import java.util.Map.Entry;
import javax.json.*;
import passives.AbstractPassive;
import passives.LoadPassives;
import upgradables.AbstractUpgradable;

/**
 *
 * @author Matt
 */
public class JsonTest {
    public static void deserialize(String s){
        JsonReader r = Json.createReader(new StringReader(s));
        JsonObject obj = r.readObject();
        
        obj.forEach((String key, JsonValue value)->{
            out.println(key + " : " + value.toString());
            out.println(value.getValueType());
            switch(value.getValueType()){
                
            }
        });
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
                    out.println("        " + indent + j);
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
        //LoadActives.load();
        LoadPassives.load();
        //LoadCharacterClasses.load();
        /*
        for(AbstractActive aa : AbstractActive.getAll()){
            pprint(aa.serializeJson(), 0);
        }*/
        for(AbstractPassive ap : AbstractPassive.getAll()){
            out.println(ap.getDescription());
            obj = ap.serializeJson();
            pprint(obj, 0);
            u = AbstractPassive.deserializeJson(obj);
            if(u != null){
                out.println(u.getDescription());
            }
        }
        /*
        for(CharacterClass cc : CharacterClass.getAll()){
            pprint(cc.serializeJson(), 0);
        }
        for(Build b : Build.getAllBuilds()){
            pprint(b.serializeJson(), 0);
        }*/
    }
}
