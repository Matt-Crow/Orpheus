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
		g.fillOval(0, guiY, sw, sh);
		g.setColor(Color.black);
		g.drawString(strHP, w/100, (int) (h * 0.9));
		
		// Energy bubble
		String strEn = getEnergyLog().getEnergy() + "";
		g.setColor(Color.yellow);
		g.fillOval((int)(w * 0.8), guiY, sw, sh);
		g.setColor(Color.black);
		g.drawString(strEn, (int)(w * 0.81), (int) (h * 0.9));
		
		// Actives
		int i = sw;
		for(Attack a : getActives()){
			if(a == getActives()[getSelectedAttack()]){
				g.setColor(Color.yellow);
				g.fillRect(i, guiY, sw, h/10);
			}
			a.drawStatusPane(g, i, (int)(h * 0.9));
			i += sw;
		}
	}
}
