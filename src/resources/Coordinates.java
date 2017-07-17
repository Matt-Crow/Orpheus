package resources;

import java.lang.Math;

public class Coordinates {
	private int x;
	private int y;
	
	public Coordinates(int xCoord, int yCoord){
		x = xCoord;
		y = yCoord;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	// this is the distance formula
	public double distanceBetween(Coordinates c){
		double xPart = c.getX() - getX();
		xPart *= xPart;
		
		double yPart = c.getY() - getY();
		yPart *= yPart;
		
		double insideTheBox = xPart + yPart;
		
		return Math.sqrt(insideTheBox);
	}
}
