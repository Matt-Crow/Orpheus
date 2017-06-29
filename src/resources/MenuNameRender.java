package resources;

import javax.swing.JLabel;
import javax.swing.ListCellRenderer;
import java.awt.Component;
import javax.swing.JList;

public class MenuNameRender extends JLabel implements ListCellRenderer{
	public MenuNameRender(){
		setOpaque(true);
		setHorizontalAlignment(CENTER);
        setVerticalAlignment(CENTER);
	}
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus){
		
		//setText(value);
		return this;
	}
}
