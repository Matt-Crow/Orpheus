package PsuedoJson;

import actives.*;
import customizables.Build;
import customizables.CharacterClass;
import customizables.LoadCharacterClasses;
import java.io.StringReader;
import static java.lang.System.out;
import java.util.Map.Entry;
import javax.json.*;
import passives.*;
import upgradables.AbstractUpgradable;

/**
 *
 * @author Matt
 */
public class JsonTest {
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
        LoadActives.load();
        //LoadPassives.load();
        //LoadCharacterClasses.load();
        
        for(AbstractActive aa : AbstractActive.getAll()){
            if(
                aa instanceof MeleeActive
                || aa instanceof BoostActive
            ){
                break;
            }
            obj = aa.serializeJson();
            //pprint(obj, 0);
            u = AbstractActive.deserializeJson(obj);
            if(u != null){
                pprint(obj, 0);
                out.println(aa.getDescription());
                out.println(u.getDescription());
            }
        }
        
        /*
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
        }*/
        
        /*
        for(CharacterClass cc : CharacterClass.getAll()){
            pprint(cc.serializeJson(), 0);
        }
        for(Build b : Build.getAllBuilds()){
            pprint(b.serializeJson(), 0);
        }*/
    }
}
