package initializers;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPanel;
import com.sun.glass.events.KeyEvent;
import entities.Player;
import resources.KeyRegister;

public class Controls {
	private static Player p;
	public static void registerControls(JPanel j){
		p = Master.TRUEPLAYER;
		new KeyRegister(j, KeyEvent.VK_Q, true, new meleeAction());
		new KeyRegister(j, KeyEvent.VK_1, true, new firstActive());
		new KeyRegister(j, KeyEvent.VK_2, true, new secondActive());
		new KeyRegister(j, KeyEvent.VK_3, true, new thirdActive());
	}
	
	public static class meleeAction extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.useMeleeAttack();
		}
	}
	public static class firstActive extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.useAttack(0);
		}
	}
	public static class secondActive extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.useAttack(1);
		}
	}
	public static class thirdActive extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.useAttack(2);
		}
	}
}
