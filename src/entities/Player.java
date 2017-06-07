package entities;

import resources.Op;

public class Player {
	private int x;
	private int y;
	private String facing;
	private int[] momentum; 
	
	public Player(){
		facing = "N";
		momentum = new int[2];
		momentum[0] = 0;
		momentum[1] = 0;
	}
	public void setCoords(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void setMomentum(int x, int y){
		momentum[0] = x;
		momentum[1] = y;
	}
	public void setDirection(String dir){
		facing = dir;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	// add collisions
	public void move(int amount){
		switch(facing){
			case "N":
				y -= amount;
				break;
			case "W":
				x -= amount;
				break;
			case "E":
				x += amount;
				break;
			case "S":
				y += amount;
				break;
			case "NW":
				x -= amount;
				y -= amount;
				break;
			case "NE":
				x += amount;
				y -= amount;
				break;
			case "SW":
				x -= amount;
				y += amount;
				break;
			case "SE":
				x += amount;
				y += amount;
				break;
		}
		Op.add(facing);
		Op.dp();
	}
}
