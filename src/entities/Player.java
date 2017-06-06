package entities;

public class Player {
	private int x;
	private int y;
	private String facing;
	
	public Player(){
		facing = "N";
	}
	public void setCoords(int x, int y){
		this.x = x;
		this.y = y;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	// add collisions
	public void move(int amount, String direction){
		switch(direction){
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
		facing = direction;
	}
}
