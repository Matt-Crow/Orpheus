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
	private int rotated;
	
	public DrawingPlane(int w, int h){
		width = w;
		height = h;
		tx = 0;
		ty = 0;
		rotated = 0;
		setSize(w, h);
		setLayout(null);
		setBackground(CustomColors.black);
		setFocusable(true);
	}
	public void pd(){
		Op.add(tx);
		Op.add(ty);
		Op.add(rotated);
		Op.dp();
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
	
	public void rotate(int x, int y, int degrees, Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		translate(x, y, g2d);
		g2d.rotate((double)(degrees * (Math.PI / 180)));
		translate(-x, -y, g2d);
		rotated += degrees;
	}
	public void untranslate(Graphics g){
		g.translate(-tx, -ty);
		tx = 0;
		ty = 0;
	}
	public void unrotate(Graphics2D g){
		g.rotate((double)(-rotated * (Math.PI / 180)));
		rotated = 0;
	}
	
	public void close(){
		SwingUtilities.getWindowAncestor(this).dispose();
	}
}