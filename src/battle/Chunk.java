package battle;

import entities.Entity;
import java.awt.Graphics;

public class Chunk {
	/**
	 * The Chunk class is used to group entities with similar coordinates,
	 * allowing the program to save time when checking for collisions
	 */
	private int x; //x coordinate of upper-left corner
	private int y;
	private int size; // width and height of the chunk
	
	//linked list stuff
	private Entity head;
	
	private Chunk next;
	private boolean hasNext;
	
	public Chunk(int xCoord, int yCoord, int s){
		x = xCoord;
		y = yCoord;
		size = s;
		hasNext = false;
		head = new Entity();
	}
	
	public Chunk spawn(int xCoord, int yCoord){
		Chunk ret = new Chunk(xCoord, yCoord, size);
		next = ret;
		hasNext = true;
		return ret;
	}
	
	public boolean getHasNext(){
		return hasNext;
	}
	public Chunk getNext(){
		return next;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public Entity getHead(){
		return head;
	}
	
	public void register(Entity e){
		head.insertChild(e);
	}
	
	public boolean contains(int xCoord, int yCoord){
		return xCoord > x && xCoord < x + size && 
				yCoord > y && yCoord < y + size;
	}
	public void update(){
		Entity current = head;
		while(current.getHasChild()){
			current = current.getChild();
			current.update();
		}
		// check for collisions
	}
	public void draw(Graphics g){
		Entity current = head;
		while(current.getHasChild()){
			current = current.getChild();
			current.draw(g);
			
		}
	}
}
