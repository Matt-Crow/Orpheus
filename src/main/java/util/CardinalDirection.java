/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author Matt
 */
public enum CardinalDirection {
    LEFT("left", -1, 0),
    RIGHT("right", 1, 0),
    UP("up", 0, -1),
    DOWN("down", 0, 1);
    
    private final String strValue;
    private final int xMod;
    private final int yMod;
    
    private CardinalDirection(String strValue, int xMod, int yMod){
        this.strValue = strValue.toLowerCase();
        this.xMod = xMod;
        this.yMod = yMod;
    }
    
    public static CardinalDirection fromString(String s){
        CardinalDirection ret = null;
        CardinalDirection[] vals = CardinalDirection.values();
        s = s.toLowerCase();
        for(int i = 0; i < vals.length && ret == null; i++){
            if(vals[i].toString().equals(s)){
                ret = vals[i];
            }
        }
        
        if(ret == null){
            throw new IllegalArgumentException(String.format("There is no cardinal direction with name \"%s\"", s));
        }
        
        return ret;
    }
    
    public final int getXMod(){
        return xMod;
    }
    
    public final int getYMod(){
        return yMod;
    }
    
    @Override
    public String toString(){
        return strValue;
    }
}
