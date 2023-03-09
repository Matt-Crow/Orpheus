package gui.graphics;

import java.awt.Color;

/**
 * The CustomColors class is used to store color patterns and create
 * variants of colors that I want.
 * 
 * I may change this later to act only as a static class to store color combinations
 * @author Matt Crow
 */
public class CustomColors extends Color{
	
	public static final CustomColors red = new CustomColors(255, 55, 0);
	public static final CustomColors orange = new CustomColors(255, 100, 0);
	public static final CustomColors yellow = new CustomColors(200, 200, 0);
	public static final CustomColors green = new CustomColors(0, 200, 0);
	//public static final Color darkGreen = new Color(0, 100, 0);
	public static final CustomColors bluegreen = new CustomColors(0, 255, 200);
	public static final CustomColors blue = new CustomColors(0, 0, 200);
	public static final CustomColors purple = new CustomColors(150, 0, 200);
	public static final CustomColors black = new CustomColors(0, 0, 0);
	public static final CustomColors white = new CustomColors(255, 255, 255);
	public static final CustomColors darkGrey = new CustomColors(200, 200, 200);
	public static final CustomColors brown = new CustomColors(255, 200, 200);
	public static final CustomColors gold = new CustomColors(155, 155, 0);
	public static final CustomColors silver = new CustomColors(200, 200, 200);
	//pink
	
	public static final CustomColors[] fireColors = {red, orange, black};
	public static final CustomColors[] earthColors = {green, darkGrey, brown};
	public static final CustomColors[] airColors = {yellow, white, gold};
	public static final CustomColors[] waterColors = {blue, bluegreen, white};
	public static final CustomColors[] rainbow = {red, orange, yellow, green, blue, purple};
	
	public static final CustomColors[][] all = {fireColors, earthColors, airColors, waterColors, rainbow};
	
	public CustomColors(int r, int g, int b){
		super(r, g, b);
	}
    
    @Override
	public String toString(){
		return getRed() + ", " + getGreen() + ", " + getBlue();
	}
    
    /**
     * Converts a string to a color. Used in deserialization
     * @param s a string, hopefully one with at least three integers, separated by commas
     * @return 
     */
    public static CustomColors fromString(String s){
        String[] split = s.split(", ");
        int r = Integer.parseInt(split[0].trim());
        int g = Integer.parseInt(split[1].trim());
        int b = Integer.parseInt(split[2].trim());
        return new CustomColors(r, g, b);
    }
}
