package windows;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import graphics.CustomColors;
import gui.Style;
import controllers.Master;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;

/**
 * In prior versions of Orpheus, this class
 * was used to serve as the base for every page
 * (BuildCanvas, BattleCanvas, etc), but will be
 * replaced by Page and Subpage for the purpose of
 * constructing GUIs.
 * 
 * However, functionality will be moved from
 * this to WorldCanvas, effectively functioning as
 * an HTML canvas: no elements, just drawing.
 * Might want to keep this class separate from
 * WorldCanvas just so I don't have tons of
 * translation and transformation functions in
 * the WorldCanvas.
 * 
 * @author Matt Crow.
 */
public class OldContentPage extends JPanel{
	private final JMenuBar menuBar;
	private final JPanel content;
	
	public OldContentPage(){
		super();
		setLayout(new GridBagLayout());
		GridBagConstraints c1 = new GridBagConstraints();
		menuBar = new JMenuBar();
        menuBar.setMinimumSize(new Dimension(Integer.MAX_VALUE, 50));
		
        c1.weightx = 1;
		c1.weighty = 1;
		c1.fill = GridBagConstraints.BOTH;
		super.add(menuBar, c1);
		GridBagConstraints c2 = new GridBagConstraints();
		c2.gridy = 1;
		c2.weighty = 9;
		c2.weightx = 1;
		c2.fill = GridBagConstraints.BOTH;
		content = new JPanel();
        content.setVisible(true);
		super.add(content, c2);
		setBackground(CustomColors.black);
		setSize(Master.CANVASWIDTH, Master.CANVASHEIGHT);
		setFocusable(true);
	}
    
    @Override
	public Component add(Component j){
		content.add(j);
        Style.applyStyling(j);
		return j;
	}
	public Component addMenuItem(Component j){
		menuBar.add(j);
        Style.applyStyling(j);
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
        j.requestFocus(); 
        //otherwise key controls don't work until the user selects the program in their task bar
    }
	public void close(){
		SwingUtilities.getWindowAncestor(this).dispose();
	}
}