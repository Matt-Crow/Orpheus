package gui.graphics;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

/**
 * The CustomColors class is used to store color patterns and create
 * variants of colors that I want.
 * 
 * I may change this later to act only as a static class to store color combinations
 * @author Matt Crow
 */
public class CustomColors extends Color{
	
	public static final CustomColors RED = new CustomColors(255, 55, 0);
	public static final CustomColors ORANGE = new CustomColors(255, 100, 0);
	public static final CustomColors YELLOW = new CustomColors(200, 200, 0);
	public static final CustomColors GREEN = new CustomColors(0, 200, 0);
	public static final CustomColors BLUE_GREEN = new CustomColors(0, 255, 200);
	public static final CustomColors BLUE = new CustomColors(0, 0, 200);
	public static final CustomColors PURPLE = new CustomColors(150, 0, 200);
	public static final CustomColors BLACK = new CustomColors(0, 0, 0);
	public static final CustomColors WHITE = new CustomColors(255, 255, 255);
	public static final CustomColors DARK_GREY = new CustomColors(200, 200, 200);
	public static final CustomColors BROWN = new CustomColors(255, 200, 200);
	public static final CustomColors GOLD = new CustomColors(155, 155, 0);
	public static final CustomColors SILVER = new CustomColors(200, 200, 200);
	
	public static final Collection<Color> FIRE = List.of(RED, ORANGE, BLACK);
	public static final Collection<Color> METAL = List.of(darkGray, GOLD, SILVER);

	public static final CustomColors[] FIRE_COLORS = {RED, ORANGE, BLACK};
	public static final CustomColors[] EARTH_COLORS = {GREEN, DARK_GREY, BROWN};
	public static final CustomColors[] AIR_COLORS = {YELLOW, WHITE, GOLD};
	public static final CustomColors[] WATER_COLORS = {BLUE, BLUE_GREEN, WHITE};
	public static final CustomColors[] RAINBOW_COLORS = {RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE};
	

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
