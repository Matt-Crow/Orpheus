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
		p = Master.thePlayer;
		new KeyRegister(j, KeyEvent.VK_UP, true, new moveAction());
		new KeyRegister(j, KeyEvent.VK_UP, false, new stopAction());
		new KeyRegister(j, KeyEvent.VK_DOWN, true, new reverseAction());
		new KeyRegister(j, KeyEvent.VK_DOWN, false, new forwardAction());
		new KeyRegister(j, KeyEvent.VK_LEFT, true, new turnLeftAction());
		new KeyRegister(j, KeyEvent.VK_RIGHT, true, new turnRightAction());
		new KeyRegister(j, KeyEvent.VK_Q, true, new meleeAction());
		new KeyRegister(j, KeyEvent.VK_E, true, new useAction());
		new KeyRegister(j, KeyEvent.VK_1, true, new firstActive());
		new KeyRegister(j, KeyEvent.VK_2, true, new secondActive());
		new KeyRegister(j, KeyEvent.VK_3, true, new thirdActive());
	}
	public static class moveAction extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.setMoving(true);
		}
	}
	public static class stopAction extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.setMoving(false);
		}
	}
	public static class reverseAction extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.setBackwards(true);
		}
	}
	public static class forwardAction extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.setBackwards(false);
		}
	}
	public static class turnLeftAction extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.turn("left");
		}
	}
	public static class turnRightAction extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.turn("right");
		}
	}
	public static class meleeAction extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.useMeleeAttack();
		}
	}
	public static class useAction extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.useSelectedAttack();
		}
	}
	public static class firstActive extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.changeSelectedAttack(0);
		}
	}
	public static class secondActive extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.changeSelectedAttack(1);
		}
	}
	public static class thirdActive extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.changeSelectedAttack(2);
		}
	}
}
