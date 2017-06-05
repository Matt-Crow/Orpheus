package windows;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import battle.Battlefield;
import initializers.Run;

public class BattleCanvas extends JPanel{
	public static final long serialVersionUID = 1L;
	Battlefield b;
	
	public BattleCanvas(){
		setLayout(null);
		setBackground(Color.black);
		b = new Battlefield(10, 10, 100);
		Run.player.setCoords(600, 500);
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		b.draw(g);
	}
}
