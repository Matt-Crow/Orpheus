package PsuedoJson;

import actives.AbstractActive;
import actives.LoadActives;
import java.io.StringReader;
import java.util.Objects;
import static java.lang.System.out;
import java.lang.reflect.Field;
import javax.json.*;

/**
 *
 * @author Matt
 */
public class JsonTest {
    public static String serialize(Object obj) throws Exception{
        if(Objects.isNull(obj)){
            out.println("No object to serialize");
            return null;
        }
        Class<?> objClass = obj.getClass();
        
        
        JsonObjectBuilder json = Json.createObjectBuilder();
        while(objClass != null){
            out.println("Class:");
            out.println(objClass.toString());
            out.println("Interfaces:");
            for(Class<?> i : objClass.getInterfaces()){
                out.println(i);
            }
            out.println("Fields:");
            for (Field field : objClass.getDeclaredFields()) {
                out.println(field);
            }
            objClass = objClass.getSuperclass();
        }
        
        return json.build().toString();
    }
    
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
    
    public static void main(String[] args) throws Exception{
        LoadActives.load();
        AbstractActive a = AbstractActive.getActiveByName("Slash");
        String s = JsonTest.serialize(a);
        out.println(s);
        deserialize(s);
    }
}
