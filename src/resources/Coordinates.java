package resources;

import java.lang.Math;
import entities.Entity;

public class Coordinates {
	private int x;
	private int y;
	private Entity coordsOf;
	
	public Coordinates(int xCoord, int yCoord){
		x = xCoord;
		y = yCoord;
	}
	public Coordinates(int xCoord, int yCoord, Entity e){
		x = xCoord;
		y = yCoord;
		coordsOf = e;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public Entity getRegistered(){
		return coordsOf;
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
