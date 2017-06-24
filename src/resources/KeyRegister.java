package resources;

import javax.swing.JComponent;
import javax.swing.KeyStroke;

import com.sun.glass.events.KeyEvent;

import javax.swing.InputMap;
import javax.swing.ActionMap;
import javax.swing.AbstractAction;

public class KeyRegister {
	public KeyRegister(JComponent registerTo, String key, boolean pressed, AbstractAction a){
		InputMap keys = registerTo.getInputMap();
		ActionMap actions = registerTo.getActionMap();
		String text = key;
		
		if(pressed){
			text += " pressed";
		} else {
			text += " released";
		}
		
		keys.put(KeyStroke.getKeyStroke(findKey(key), 0, !pressed), text);
		actions.put(text, a);
	}
	public int findKey(String key){
		switch(key){
		case "q":
			return KeyEvent.VK_Q;
		case "w":
			return KeyEvent.VK_W;
		case "e":
			return KeyEvent.VK_E;
		case "r":
			return KeyEvent.VK_R;
		case "t":
			return KeyEvent.VK_T;
		case "y":
			return KeyEvent.VK_Y;
		case "u":
			return KeyEvent.VK_U;
		case "i":
			return KeyEvent.VK_I;
		case "o":
			return KeyEvent.VK_O;
		case "p":
			return KeyEvent.VK_P;
		case "a":
			return KeyEvent.VK_A;
		case "s":
			return KeyEvent.VK_S;
		case "d":
			return KeyEvent.VK_D;
		case "f":
			return KeyEvent.VK_F;
		case "g":
			return KeyEvent.VK_G;
		case "h":
			return KeyEvent.VK_H;
		case "j":
			return KeyEvent.VK_J;
		case "k":
			return KeyEvent.VK_K;
		case "l":
			return KeyEvent.VK_L;
		case "z":
			return KeyEvent.VK_Z;
		case "x":
			return KeyEvent.VK_X;
		case "c":
			return KeyEvent.VK_C;
		case "v":
			return KeyEvent.VK_V;
		case "b":
			return KeyEvent.VK_B;
		case "n":
			return KeyEvent.VK_N;
		case "m":
			return KeyEvent.VK_M;
		}
		Op.add("I don't have this key in my key bank");
		Op.dp();
		return 0;
	}
}
