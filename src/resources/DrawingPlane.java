package resources;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import initializers.Master;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
	
	public DrawingPlane(int w, int h){
		tx = 0;
		ty = 0;
		rotated = 0;
		setSize(w, h);
		setLayout(null);
		setBackground(CustomColors.black);
		setFocusable(true);
		components = new ArrayList<>();
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
		Op.add(components.size());
		Op.dp();
	}
	public void resizeComponents(int rows, int cols){
		int cWidth = Master.CANVASWIDTH;
		int cHeight = Master.CANVASHEIGHT;
		int horizSpacing = cWidth / cols;
		int vertSpacing = cHeight / rows;
		
		int rowNum = 0;
		int colNum = 0;
		
		for(JComponent j : components){
			if(colNum > cols){
				colNum = 0;
				rowNum++;
			}
			//check if out of space
			if(rowNum < rows){
				Op.add(colNum * horizSpacing);
				Op.add(rowNum * vertSpacing);
				j.setBounds(colNum * horizSpacing, rowNum * vertSpacing, horizSpacing, vertSpacing);
			} else {
				Op.add("No room");
			}
			colNum++;
			Op.dp();
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