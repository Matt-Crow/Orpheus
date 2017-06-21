package entities;

import resources.Direction;

public class Entity {
	private int x;
	private int y;
	private int dirNum;
	private int momentum;
	private boolean moving;
	
	public Entity(int xCoord, int yCoord, int directionNumber, int m){
		x = xCoord;
		y = yCoord;
		dirNum = directionNumber;
		momentum = m;
		moving = false;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public void setCoords(int xCoord, int yCoord){
		x = xCoord;
		y = yCoord;
	}
	public int[] getVector(){
		return Direction.directions[dirNum];
	}
	public void turn(String dir){
		if(dir == "left"){
			dirNum -= 1;
		} else {
			dirNum += 1;
		}
		if(dirNum < 0){
			dirNum = 7;
		} else if(dirNum > 7){
			dirNum = 0;
		}
	}
	public void setDirNum(int dir){
		dirNum = dir;
	}
	public int getDirNum(){
		return dirNum;
	}
	public void setMoving(boolean m){
		moving = m;
	}
	// add collisions
	public void move(){
		x += getVector()[0] * momentum;
		y += getVector()[1] * momentum;
	}
	public void update(){
		if(moving){
			move();
		}
	}
}
