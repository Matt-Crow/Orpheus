package entities;

//import resources.Op;

public class Player {
	private int x;
	private int y;
	private int dirNum;
	private int momentum;
	private boolean moving;
	
	public Player(){
		momentum = 10;
		moving = false;
		dirNum = 0;
	}
	public void setCoords(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void setMoving(boolean m){
		moving = m;
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
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public String getFacing(){
		String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
		return directions[dirNum];
	}
	// add collisions
	public void move(){
		if(!moving){
			return;
		}
		switch(getFacing()){
			case "N":
				y -= momentum;
				break;
			case "W":
				x -= momentum;
				break;
			case "E":
				x += momentum;
				break;
			case "S":
				y += momentum;
				break;
			case "NW":
				x -= momentum;
				y -= momentum;
				break;
			case "NE":
				x += momentum;
				y -= momentum;
				break;
			case "SW":
				x -= momentum;
				y += momentum;
				break;
			case "SE":
				x += momentum;
				y += momentum;
				break;
		}
	}
}
