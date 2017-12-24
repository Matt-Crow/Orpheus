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
		
		int guiY = (int)(h * 0.8);
		int sw = w / 5;
		int sh = h / 5;
		
		// HP bubble
		String strHP = getLog().getHP() + "";
		g.setColor(Color.red);
		g.fillOval(0, guiY, sw, sw);
		g.setColor(Color.black);
		g.drawString(strHP, (int)(w * 0.1), (int) (h * 0.9));
		
		// Energy bubble
		String strEn = getEnergyLog().getEnergy() + "";
		g.setColor(Color.yellow);
		g.fillOval((int)(w * 0.8), guiY, sw, sw);
		g.setColor(Color.black);
		g.drawString(strEn, (int)(w * 0.9), (int) (h * 0.9));
		
		// Actives
		int i = sw;
		for(Attack a : getActives()){
			a.drawStatusPane(g, i, (int)(h * 0.9), sw, sh);
			i += sw;
		}
	}
}
