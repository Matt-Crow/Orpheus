package resources;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.AbstractAction;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;


public class DrawingPlane extends JPanel{
	public static final long serialVersionUID = 1L;
	private int width;
	private int height;
	private int tx;
	private int ty;
	
	public DrawingPlane(int w, int h){
		width = w;
		height = h;
		tx = 0;
		ty = 0;
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
	
	public void translate(int x, int y, Graphics g){
		tx += x;
		ty += y;
		g.translate(x, y);
	}
	public void trueTranslate(int x, int y, Graphics g){
		// "true" means ignoring other translates.
		g.translate(-tx, -ty);
		g.translate(x, y);
	}
	public void untranslate(Graphics g){
		g.translate(-tx, -ty);
		translate(0, 0, g);
	}
	
	public void close(){
		SwingUtilities.getWindowAncestor(this).dispose();
	}
}