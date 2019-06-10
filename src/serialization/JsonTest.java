package serialization;

import actives.*;
import customizables.Build;
import customizables.CharacterClass;
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
        Build b = null;
        AbstractUpgradable.loadAll();
        Build.loadAll();
        
        //done
        for(AbstractActive aa : AbstractActive.getAll()){
            out.println(aa.getName());
            obj = aa.serializeJson();
            //pprint(obj, 0);
            u = AbstractActive.deserializeJson(obj);
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
