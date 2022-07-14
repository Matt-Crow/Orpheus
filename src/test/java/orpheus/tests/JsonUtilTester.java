package orpheus.tests;

import javax.json.JsonObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import util.Settings;
import world.build.Build;
import world.build.BuildJsonUtil;

public class JsonUtilTester {
    
    @Test
    public void testJsonUtil(){
        JsonObject asJson;
        Build fromJson;

        for(Build original : Settings.getDataSet().getAllBuilds()){
            asJson = BuildJsonUtil.serializeJson(original);
            fromJson = BuildJsonUtil.deserializeJson(asJson);
            Assertions.assertEquals(
                fromJson.getDescription(), 
                original.getDescription()
            );
        }
    }
}
