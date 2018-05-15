package battle;

import entities.Entity;
import resources.Op;

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
	
	private int id;
	private static int nextId = 0;
	
	public Chunk(int xCoord, int yCoord, int s){
		x = xCoord;
		y = yCoord;
		size = s;
		hasNext = false;
		head = new Entity();
		id = nextId;
		nextId++;
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
	public int getSize(){
		return size;
	}
	public Entity getHead(){
		return head;
	}
	
	public int getId(){
		return id;
	}
	
	public void register(Entity e){
		head.insertChild(e);
	}
	
	public boolean contains(int xCoord, int yCoord){
		return xCoord >= x && xCoord < x + size && 
				yCoord >= y && yCoord < y + size;
	}
	public void displayData(){
		Op.add("Chunk " + id);
		Op.add("X: " + x + "-" + (x + size));
		Op.add("Y: " + y + "-" + (y + size));
		Entity current = head;
		while(current.getHasChild()){
			current = current.getChild();
			Op.add("Entity id " + current.getId());
		}
		Op.dp();
	}
	public void update(){
		Entity current = head;
		while(current.getHasChild()){
			current = current.getChild();
			current.update();
			
			Entity checkAgainst = head;
			while(checkAgainst.getHasChild()){
				checkAgainst = checkAgainst.getChild();
				if(checkAgainst.getTeam().getId() != current.getTeam().getId()){
					//enemies
					current.checkForCollisions(checkAgainst);
				}
			}
		}
	}
	public void draw(Graphics g){
		Entity current = head;
		while(current.getHasChild()){
			current = current.getChild();
			current.draw(g);
			
		}
	}
}
