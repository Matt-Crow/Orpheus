package util;

import java.lang.Math;
import entities.Entity;

public class Coordinates {
	/**
	 * Used to calculate distance between 2 points,
	 * delete later?
	 */
	// this is the distance formula
	public static double distanceBetween(int x1, int y1, int x2, int y2){
		double xPart = x1 - x2;
		xPart *= xPart;
		
		double yPart = y1 - y2;
		yPart *= yPart;
		
		double insideTheBox = xPart + yPart;
		
		return Math.sqrt(insideTheBox);
	}
	
	public static double distanceBetween(Entity e1, Entity e2){
		return distanceBetween(e1.getX(), e1.getY(), e2.getX(), e2.getY());
	}
}