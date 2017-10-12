package resources;
import javax.swing.JButton;
import java.awt.Color;

@SuppressWarnings("serial")
public class EasyButton extends JButton{
	/**
	 * Creates a new JButton with preset values to make it quicker to create them. 
	 * Use addActionListener({new AbstractAction(){public void actionPerformed(ActionEvent e){...}}});
	 */
	public EasyButton(String text, int x, int y, int w, int h, Color c){
		super(text);
		setLayout(null);
		setOpaque(true);
		setBorderPainted(false);
		setBounds(x, y, w, h);
		setBackground(c);
	}	
}
