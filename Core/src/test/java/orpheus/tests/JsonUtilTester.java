package orpheus.tests;

import javax.json.JsonObject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import orpheus.core.AppContext;
import util.Settings;
import world.builds.Build;
import world.builds.BuildJsonUtil;

public class JsonUtilTester {
    
    @Test
    public void testJsonUtil(){
        AppContext ctx = new AppContext(new Settings());
        JsonObject asJson;
        Build fromJson;

        for(Build original : ctx.getDataSet().getAllBuilds()){
            asJson = BuildJsonUtil.serializeJson(original);
            fromJson = BuildJsonUtil.deserializeJson(asJson);
            Assertions.assertEquals(
                fromJson.getDescription(), 
                original.getDescription()
            );
        }
    }
}
