package resources;

public class Direction {
	public static Direction N = new Direction("North", 0, -1);
	public static Direction NE = new Direction("Northeast", 1, -1);
	public static Direction E = new Direction("East", 1, 0);
	public static Direction SE = new Direction("Southeast", 1, 1);
	public static Direction S = new Direction("South", 0, 1);
	public static Direction SW = new Direction("Southwest", -1, 1);
	public static Direction W = new Direction("West", -1, 0);
	public static Direction NW = new Direction("Northwest", -1, -1);
	public static Direction[] directions = {N, NE, E, SE, S, SW, W, NW};
	
	private String name;
	private int xMod;
	private int yMod;
	
	public Direction(String n, int x, int y){
		name = n;
		xMod = x;
		yMod = y;
	}
	public String getName(){
		return name;
	}
	public int getXMod(){
		return xMod;
	}
	public int getYMod(){
		return yMod;
	}
	public int[] getVector(){
		return new int[]{xMod, yMod};
	}
	public static int getIndexOf(String n){
		int i = 0;
		for(Direction dir : directions){
			if(dir.getName() == n){
				return i;
			}
			i++;
		}
		return 0;
	}
}
