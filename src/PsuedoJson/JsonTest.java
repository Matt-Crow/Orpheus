package PsuedoJson;

import actives.AbstractActive;
import actives.LoadActives;
import java.util.Objects;
import org.json.JSONObject;
import static java.lang.System.out;
import java.lang.reflect.Field;

/**
 *
 * @author Matt
 */
public class JsonTest {
    public static void serialize(Object obj) throws Exception{
        if(Objects.isNull(obj)){
            out.println("No object to serialize");
            return;
        }
        Class<?> objClass = obj.getClass();
        if(!objClass.isAnnotationPresent(JsonableClass.class)){
            out.println("Object needs to have the @JsonableClass annotation");
            return;
        }
        
        JSONObject json = new JSONObject();
        while(objClass != null){
            for (Field field : objClass.getDeclaredFields()) {
                out.println(field);
                field.setAccessible(true);
                if (field.isAnnotationPresent(JsonableAttribute.class)) {
                    json.append(field.getName(), field.get(obj).toString());
                }
            }
            objClass = objClass.getSuperclass();
        }
        
        out.println(json.toString(4));
    }
    
    public static void main(String[] args) throws Exception{
        LoadActives.load();
        AbstractActive a = AbstractActive.getActiveByName("Slash");
        JsonTest.serialize(a);
    }
}
