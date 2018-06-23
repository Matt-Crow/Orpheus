package graphics;

import java.awt.Color;

public class CustomColors extends Color{
	private static final long serialVersionUID = 1L;
	
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
	public String toString(){
		return getRed() + ", " + getGreen() + ", " + getBlue();
	}
}
