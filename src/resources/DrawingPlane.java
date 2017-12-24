package resources;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import initializers.Master;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class DrawingPlane extends JPanel{
	public static final long serialVersionUID = 1L;
	private Graphics2D g;
	private AffineTransform initialTransform;
	private int tx;
	private int ty;
	private int rotated;
	private ArrayList<JComponent> components;
	
	public DrawingPlane(){
		super();
		tx = 0;
		ty = 0;
		rotated = 0;
		setLayout(null);
		setBackground(CustomColors.black);
		setSize(Master.CANVASWIDTH, Master.CANVASHEIGHT);
		setFocusable(true);
		components = new ArrayList<>();
	}
	public int getW(){
		return Master.CANVASWIDTH;
	}
	public int getH(){
		return Master.CANVASHEIGHT;
	}
	public void displayTransform(){
		Op.add("X: " + tx);
		Op.add("Y: " + ty);
		Op.add("R: " + rotated);
		Op.dp();
	}
	public void setG(Graphics gr){
		g = (Graphics2D)gr;
		initialTransform = g.getTransform();
	}
	public Graphics2D getG(){
		return g;
	}
	public void resetToInit(){
		tx = 0;
		ty = 0;
		rotated = 0;
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
	public void rotate(int x, int y, int degrees){
		translate(x, y);
		g.rotate((double)(degrees * (Math.PI / 180)));
		translate(-x, -y);
		rotated += degrees;
	}
	

	public void addComp(JComponent j){
		add(j);
		components.add(j);
	}
	public void removeComp(JComponent j){
		remove(j);
		components.remove(j);
	}
	public void resizeComponents(int rows, int cols){
		setLayout(new GridLayout(rows, cols, 10, 10));
		for(JComponent j : components){
			j.setOpaque(true);
			j.setVisible(true);
		}
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