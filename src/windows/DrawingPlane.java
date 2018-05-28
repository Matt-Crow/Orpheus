package windows;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import graphics.CustomColors;
import initializers.Master;
import resources.Op;

import javax.swing.AbstractAction;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;

public class DrawingPlane extends JPanel{
	public static final long serialVersionUID = 1L;
	private Graphics2D g;
	private AffineTransform initialTransform;
	
	private int tx;
	private int ty;
	
	private int priorX; //the last translates used
	private int priorY;
	
	public DrawingPlane(){
		super();
		tx = 0;
		ty = 0;
		priorX = 0;
		priorY = 0;
		setLayout(null);
		setBackground(CustomColors.black);
		setSize(Master.CANVASWIDTH, Master.CANVASHEIGHT);
		setFocusable(true);
	}
	public int getW(){
		return Master.CANVASWIDTH;
	}
	public int getH(){
		return Master.CANVASHEIGHT;
	}
	
	public int getTX(){
		return tx;
	}
	public int getTY(){
		return ty;
	}
	public void displayTransform(){
		Op.add("X: " + tx);
		Op.add("Y: " + ty);
		Op.dp();
	}
	
	public int[] getLastTransform(){
		return new int[]{priorX, priorY};
	}
	
	public void setG(Graphics gr){
		g = (Graphics2D)gr;
		initialTransform = g.getTransform();
	}
	public Graphics2D getG(){
		return g;
	}
	public void resetToInit(){
		priorX = tx;
		priorY = ty;
		tx = 0;
		ty = 0;
		
		g.setTransform(initialTransform);
	}
	public void translate(int x, int y){
		tx += x;
		ty += y;
		g.translate(x, y);
	}
	public void trueTranslate(int x, int y){
		// "true" means ignoring other translates.
		g.translate(-tx, -ty);
		g.translate(x, y);
	}
	
	public void resizeComponents(int rows, int cols){
		setLayout(new GridLayout(rows, cols, 10, 10));
	}
	public AbstractAction getRepaint(){
		return new AbstractAction(){
			public static final long serialVersionUID = 1L; 
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		};
	}	
	public void close(){
		SwingUtilities.getWindowAncestor(this).dispose();
	}
}