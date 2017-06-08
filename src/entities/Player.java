package entities;

public class Player {
	private int x;
	private int y;
	private String facing;
	private int momentum;
	private boolean moving;
	
	public Player(){
		facing = "N";
		momentum = 5;
		moving = false;
	}
	public void setCoords(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void setMoving(boolean m){
		moving = m;
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
	public void move(){
		if(!moving){
			return;
		}
		switch(facing){
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
