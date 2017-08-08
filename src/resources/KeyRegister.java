package resources;

import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.AbstractAction;

public class KeyRegister {
	public KeyRegister(JComponent registerTo, int key, boolean pressed, AbstractAction a){
		InputMap keys = registerTo.getInputMap();
		ActionMap actions = registerTo.getActionMap();
		String text = key + " ";
		
		if(pressed){
			text += "pressed";
		} else {
			text += "released";
		}
		
		keys.put(KeyStroke.getKeyStroke(key, 0, !pressed), text);
		actions.put(text, a);
	}
}
