package resources;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JPanel;

import java.util.ArrayList;

import customizables.Customizable;

public class Menu {
	private String text;
	private int width;
	private int height;
	private boolean clicked;
	private ArrayList<Customizable> contents;
	
	public Menu(String text, int w, int h){
		this.text = text;
		width = w;
		height = h;
		clicked = false;
		contents = new ArrayList<>();
	}
	public void addContent(Customizable c){
		contents.add(c);
	}
	public void toggle(){
		clicked = !clicked;
	}
	public void draw(Graphics g, JPanel j){
		if(!clicked){
			return;
		}
		g.setColor(Color.gray);
		g.fillRect(0, height / 10, width, height);
		int x = 0;
		int y = height / 10;
		for(Customizable item : contents){
			if (x > width){
				x = 0;
				y += 100;
			}
			g.setColor(Color.white);
			EasyButton e = new EasyButton(item.name, x + 10, y + 10, width / 5, height / 50, Color.yellow);
			e.addTo(j);
			x += 100;
		}
	}
}
