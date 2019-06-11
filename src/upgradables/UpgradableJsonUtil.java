package upgradables;

import actives.AbstractActive;
import customizables.CharacterClass;
import java.io.File;
import javax.json.JsonObject;
import passives.AbstractPassive;
import serialization.JsonTest;
import static upgradables.AbstractUpgradable.deserializeJson;

/**
 *
 * @author Matt Crow
 */
public class UpgradableJsonUtil {
    public static void loadFile(File f){
        AbstractUpgradable au = null;
        for(JsonObject obj : JsonTest.readFromFile(f)){
            au = deserializeJson(obj);
            if(au != null){
                if(au instanceof AbstractPassive){
                    AbstractPassive.addPassive((AbstractPassive)au);
                } else if(au instanceof AbstractActive){
                    AbstractActive.addActive((AbstractActive)au);
                } else if(au instanceof CharacterClass){
                    CharacterClass.addCharacterClass((CharacterClass)au);
                } else {
                    System.out.println("Couldn't deserialize " + au.getClass().getName());
                    JsonTest.pprint(obj, 0);
                }
            }
        }
    }
}
