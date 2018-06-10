package gui;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class Pane extends JComponent{
	/**
	 * Pane is used only for aligning components
	 */
	public Pane(){
		super();
		Style.applyStyling(this);
	}
}
