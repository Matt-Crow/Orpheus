package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

/**
 *
 * @author Matt
 */
public class SerialUtil {
    
    /**
     * Serializes an Object using ObjectOutputStream.writeObject,
     * then converts the result to a string so that it can
     * be used as the body of a ServerMessage
     * 
     * @param obj the object to serialize
     * @return the serialized version of the given Object,
     * converted to a string for convenience.
     */
    public static final String serializeToString(Object obj){
        String ret = "";
        try{
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(byteOut);
            objOut.writeObject(obj);
            objOut.close();
            ret = Base64.getEncoder().encodeToString(byteOut.toByteArray());
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return ret;
    }
    
    /**
     * Converts a String generated from SerialUtil.serializeToString,
     * and de-serializes it into a copy of that original Object, barring Transient fields.
     * 
     * @param s a string variant of an object stream serialization of an object
     * @return a copy of the object which was serialized into a string
     */
    public static final Object fromSerializedString(String s){
        Object ret = null;
        try{
            byte[] byteData = Base64.getDecoder().decode(s);
            ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(byteData));
            ret = objIn.readObject();
            objIn.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return ret;
    }
}
