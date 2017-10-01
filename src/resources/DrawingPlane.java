package resources;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.AbstractAction;
import java.awt.Graphics;
import java.awt.event.ActionEvent;


public class DrawingPlane extends JPanel{
	public static final long serialVersionUID = 1L;
	private int width;
	private int height;
	private int[] translatedTo; 
	
	public DrawingPlane(int w, int h){
		width = w;
		height = h;
		translatedTo = new int[]{0, 0};
		setSize(w, h);
		setLayout(null);
		setBackground(CustomColors.black);
		setFocusable(true);
	}
	
	public AbstractAction getRepaint(){
		return new AbstractAction(){
			public static final long serialVersionUID = 1L; 
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		};
	}
	
	// working on
	public void translate(int x, int y, Graphics g){
		// better way?
		translatedTo = new int[] {x, y};
		g.translate(x, y);
	}
	
	public void close(){
		SwingUtilities.getWindowAncestor(this).dispose();
	}
}