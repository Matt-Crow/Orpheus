package battle;

import entities.Player;
import entities.Projectile;
import java.awt.Graphics;

public class Chunk {
	/**
	 * The Chunk class is used to group entities with similar coordinates,
	 * allowing the program to save time when checking for collisions
	 */
	private int x; //x coordinate of upper-left corner
	private int y;
	private int size; // width and height of the chunk
	
	private Player headPlayer; // linked list head
	private Projectile headProjectile;
	private boolean hasProjectile;
	
	private Chunk next;
	private boolean hasNext;
	
	public Chunk(int xCoord, int yCoord, int s){
		x = xCoord;
		y = yCoord;
		size = s;
		hasNext = false;
		hasProjectile = false;
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
	
	public void registerProjectile(Projectile p){
		if(!hasProjectile){
			headProjectile = p;
			hasProjectile = true;
		} else {
			headProjectile.insertChild(p);
		}
	}
	
	//not done
	public void unregisterProjectile(Projectile p){
		if(headProjectile == p){
			if(p.getHasChild()){
				headProjectile = (Projectile) p.getChild();
				p.disableChild();
				headProjectile.disableParent();
			} else {
				hasProjectile = false;
			}
			if(p.getHasParent()){
				p.getParent().disableChild();
				p.disableParent();
			}
		}
	}
	
	public boolean contains(int xCoord, int yCoord){
		return xCoord > x && xCoord < x + size && 
				yCoord > y && yCoord < y + size;
	}
	public void update(){
		if(hasProjectile){
			headProjectile.update();
			headProjectile.updateAllChildren();
		}
	}
	public void draw(Graphics g){
		if(hasProjectile){
			headProjectile.draw(g);
			Projectile current = headProjectile;
			while(current.getHasChild()){
				current = (Projectile) current.getChild();
				current.draw(g);
			}
		}
	}
}
