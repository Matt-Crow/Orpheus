package gui.graphics;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

/**
 * Commonly used colors
 * @author Matt Crow
 */
public class CustomColors {
	
	public static final Color RED = new Color(255, 55, 0);
	public static final Color ORANGE = new Color(255, 100, 0);
	public static final Color YELLOW = new Color(200, 200, 0);
	public static final Color GREEN = new Color(0, 200, 0);
	public static final Color BLUE_GREEN = new Color(0, 255, 200);
	public static final Color BLUE = new Color(0, 0, 200);
	public static final Color PURPLE = new Color(150, 0, 200);
	public static final Color BLACK = new Color(0, 0, 0);
	public static final Color WHITE = new Color(255, 255, 255);
	public static final Color DARK_GREY = new Color(100, 100, 100);
	public static final Color BROWN = new Color(255, 200, 200);
	public static final Color GOLD = new Color(155, 155, 0);
	public static final Color SILVER = new Color(200, 200, 200);
	
	public static final Collection<Color> FIRE = List.of(RED, ORANGE, BLACK);
	public static final Collection<Color> EARTH = List.of(GREEN, DARK_GREY, BROWN);
	public static final Collection<Color> METAL = List.of(DARK_GREY, GOLD, SILVER);
	public static final Collection<Color> AIR = List.of(YELLOW, WHITE, GOLD);
	public static final Collection<Color> WATER = List.of(BLUE, BLUE_GREEN, WHITE);
	public static final Collection<Color> RAINBOW = List.of(RED, ORANGE, YELLOW, GREEN, BLUE, PURPLE);
}
