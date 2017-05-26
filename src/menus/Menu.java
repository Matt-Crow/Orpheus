package menus;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JPanel;

import java.util.ArrayList;

public class Menu {
	private String text;
	private int width;
	private int height;
	private boolean clicked;
	private ArrayList<Object> contents;
	
	public Menu(String text, int w, int h){
		this.text = text;
		width = w;
		height = h;
		clicked = false;
		contents = new ArrayList<>();
	}
	public void open(){
		clicked = true;
	}
	public void close(){
		clicked = false;
	}
	public void set(JPanel j){
		JButton button = new JButton(text);
		j.add(button);
	}
	public void draw(Graphics g){
		if(!clicked){
			return;
		}
		g.setColor(Color.green);
		g.fillRect(0, height / 10, width, height);
		int x = 0;
		int y = height / 10;
		for(Object item : contents){
			if (x > width){
				x = 0;
				y += 100;
			}
			g.setColor(Color.white);
			g.fillRect(x + 10, y + 10, width / 5, height / 5);
			x += 100;
		}
	}
}
