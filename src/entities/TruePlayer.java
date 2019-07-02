package entities;

import java.awt.Color;
import java.awt.Graphics;

import actives.AbstractActive;
import graphics.CustomColors;
import controllers.Master;
import windows.world.WorldCanvas;

//get rid of this thing
public class TruePlayer extends Player{
	
    public TruePlayer(String userName){
        super(userName);
    }
	public TruePlayer(){
		this("Player");
	}
	
	public void drawHUD(Graphics g, WorldCanvas wc){
		int w = wc.getWidth();
		int h = wc.getHeight();
		
		// compass
		int compassX = w / 10 * 9; // center points
		int compassY = h / 10 * 3;
		int compassDiameter = w / 10;
		
		g.setColor(CustomColors.darkGrey);
		g.fillOval(compassX - compassDiameter, compassY - compassDiameter, compassDiameter * 2, compassDiameter * 2); // draws from upper-left corner, not center
		g.setColor(CustomColors.red);
		g.drawLine(compassX, compassY, (int)(compassX + getDir().getXMod() * compassDiameter), (int)(compassY + getDir().getYMod() * compassDiameter));
		
		
		int guiY = (int)(h * 0.9);
		int sw = w / 5;
		int sh = h / 10;
		
		// HP
		String strHP = getLog().getHP() + "";
		g.setColor(Color.red);
		g.fillRect(0, guiY, sw, sh);
		g.setColor(Color.black);
		g.drawString("HP: " + strHP, (int)(w * 0.1), (int) (h * 0.93));
		
		// Energy
		String strEn = getEnergyLog().getEnergy() + "";
		g.setColor(Color.yellow);
		g.fillRect((int)(w * 0.8), guiY, sw, sw);
		g.setColor(Color.black);
		g.drawString("Energy: " + strEn, (int)(w * 0.9), (int) (h * 0.93));
		
		// Actives
		int i = sw;
		for(AbstractActive a : getActives()){
			a.drawStatusPane(g, i, (int)(h * 0.9), sw, sh);
			i += sw;
		}
	}
}
