package gui;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import graphics.CustomColors;

@SuppressWarnings("serial")
public class Button extends JButton implements MouseListener{
	public Button(String s){
		super(s);
		Style.applyStyling(this);
		setVerticalTextPosition(SwingConstants.CENTER);
		setHorizontalTextPosition(SwingConstants.CENTER);
		addMouseListener(this);
	}
	public void mousePressed(MouseEvent e){
		
	}
	public void mouseReleased(MouseEvent e){
		
	}
	public void mouseClicked(MouseEvent e){
		
	}
	public void mouseEntered(MouseEvent e){
		setBackground(CustomColors.orange);
		repaint();
	}
	public void mouseExited(MouseEvent e){
		setBackground(CustomColors.gold);
		repaint();
	}
}
