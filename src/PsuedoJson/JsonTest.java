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
        if(!objClass.isAnnotationPresent(JsonableClass.class)){
            out.println("Object needs to have the @JsonableClass annotation");
            return null;
        }
        
        JsonObjectBuilder json = Json.createObjectBuilder();
        while(objClass != null){
            for (Field field : objClass.getDeclaredFields()) {
                out.println(field);
                field.setAccessible(true);
                if (field.isAnnotationPresent(JsonableAttribute.class)) {
                    json.add(field.getName(), field.get(obj).toString());
                }
            }
            objClass = objClass.getSuperclass();
        }
        
        return json.build().toString();
    }
    
    public static void deserialize(String s){
        JsonReader r = Json.createReader(new StringReader(s));
        JsonObject obj = r.readObject();
        out.println(obj);
    }
    
    public static void main(String[] args) throws Exception{
        LoadActives.load();
        AbstractActive a = AbstractActive.getActiveByName("Slash");
        String s = JsonTest.serialize(a);
        out.println(s);
        deserialize(s);
    }
}
