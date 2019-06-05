/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PsuedoJson;

import javax.json.JsonObject;

/**
 *
 * @author Matt
 */
public interface JsonSerialable {
    public abstract JsonObject serializeJson();
    public static Object deserializeJson(JsonObject obj){
        throw new UnsupportedOperationException();
    }
}
