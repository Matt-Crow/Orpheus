package battle;
import entities.Entity;

public class Hitbox {
	private int x;
	private int y;
	private int width;
	private int height;
	private Entity registeredTo;
	
	public Hitbox(Entity e, int w, int h){
		x = e.getX();
		y = e.getY();
		width = w;
		height = h;
		registeredTo = e;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public int getWidth(){
		return width;
	}
	public int getHeight(){
		return height;
	}
	
	public boolean checkForIntercept(Hitbox b){
		return x + width / 2 >= b.getX() - b.getWidth() / 2 
				&& x - width / 2 <= b.getX() + b.getWidth() / 2 
				&& y + width / 2 >= b.getY() - b.getHeight() / 2 
				&& y - width / 2 <= b.getY() + b.getHeight() / 2;
	}
	public void updatePosition(){
		x = registeredTo.getX();
		y = registeredTo.getY();
	}
}
