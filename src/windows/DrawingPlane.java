package windows;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import graphics.CustomColors;
import gui.Pane;
import initializers.Master;
import resources.Op;

import javax.swing.AbstractAction;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import javax.swing.JFrame;

public class DrawingPlane extends JPanel{
	public static final long serialVersionUID = 1L;
	
	private Pane menuBar;
	private Pane content;
	
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
		setLayout(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		menuBar = new Pane();
		c1.weightx = 1;
		c1.weighty = 1;
		c1.fill = GridBagConstraints.BOTH;
		super.add(menuBar, c1);
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridy = 1;
		c2.weighty = 9;
		c2.weightx = 1;
		c2.fill = GridBagConstraints.BOTH;
		content = new Pane();
		super.add(content, c2);
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
	
	public Component add(Component j){
		content.add(j);
		return j;
	}
	public Component addMenuItem(Component j){
		menuBar.add(j);
		return j;
	}
	public void remove(Component j){
		content.remove(j);
	}
	
	public void resizeComponents(int rows, int cols){
		content.setLayout(new GridLayout(rows, cols));
	}
	public void resizeMenu(int cols){
		menuBar.setLayout(new GridLayout(1, cols));
	}
	public AbstractAction getRepaint(){
		return new AbstractAction(){
			public static final long serialVersionUID = 1L; 
			public void actionPerformed(ActionEvent e){
				repaint();
			}
		};
	}	
    public void switchTo(JPanel j){
        JFrame parent = (JFrame)SwingUtilities.getWindowAncestor(this);
        parent.setContentPane(j);
        parent.revalidate();
    }
	public void close(){
		SwingUtilities.getWindowAncestor(this).dispose();
	}
}