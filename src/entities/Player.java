package entities;

//import resources.Op;

public class Player {
	public String name;
	private int x;
	private int y;
	private int dirNum;
	private int turnCooldown;
	private int momentum;
	private boolean moving;
	
	public Player(String n){
		name = n;
	}
	public void setCoords(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void setMoving(boolean m){
		moving = m;
	}
	public void turn(String dir){
		if(turnCooldown > 0){
			return;
		}
		
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
		
		turnCooldown = 10;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public void init(){
		momentum = 10;
		moving = false;
		dirNum = 0;
		turnCooldown = 0;
	}
	public String getFacing(){
		String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW"};
		return directions[dirNum];
	}
	
	public void update(){
		turnCooldown -= 1;
		move();
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
