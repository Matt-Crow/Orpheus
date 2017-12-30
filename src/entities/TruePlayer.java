package entities;

import java.awt.Color;
import java.awt.Graphics;

import attacks.Attack;
import initializers.Master;

public class TruePlayer extends Player{
	public TruePlayer(){
		super("Player");
		getPlayerAI().disable();
	}
	public void drawHUD(Graphics g){
		int w = Master.CANVASWIDTH;
		int h = Master.CANVASHEIGHT;
		
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
		for(Attack a : getActives()){
			a.drawStatusPane(g, i, (int)(h * 0.9), sw, sh);
			i += sw;
		}
	}
}
