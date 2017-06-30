package windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.AbstractAction;
import javax.swing.Timer;

import battle.Battle;
import battle.Battlefield;
import entities.Player;
import resources.KeyRegister;
import resources.Op;

public class BattleCanvas extends JPanel{
	public static final long serialVersionUID = 1L;
	private Battlefield battlefield;
	private Battle hostedBattle;
	private int w;
	private int h;
	private Timer timer;
	private int FPS;
	private Player p;
	private ActionListener update;
	
	// it fails here
	public BattleCanvas(int windowWidth, int windowHeight){
		w = windowWidth;
		h = windowHeight;
		setLayout(null);
		setBackground(Color.black);
		setFocusable(true);
		FPS = 20;
		
		hostedBattle = new Battle();
		battlefield = new Battlefield();
		
		hostedBattle.setHost(battlefield);
		hostedBattle.init();
		p = hostedBattle.getPlayer();
		Op.add("In battlecanvas");
		p.getCharacterClass().listActiveNames();
		update = new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		          hostedBattle.update();
		          repaint();
		      }
		};
		addKeyRegistration();
	}
	
	public void addKeyRegistration(){
		new KeyRegister(this, "w", true, new moveAction());
		new KeyRegister(this, "w", false, new stopAction());
		new KeyRegister(this, "a", true, new turnLeftAction());
		new KeyRegister(this, "d", true, new turnRightAction());
		new KeyRegister(this, "q", true, new meleeAction());
		new KeyRegister(this, "e", true, new useAction());
		new KeyRegister(this, "z", true, new firstActive());
		new KeyRegister(this, "x", true, new secondActive());
		new KeyRegister(this, "c", true, new thirdActive());
	}
	
	public class moveAction extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.setMoving(true);
		}
	}
	public class stopAction extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.setMoving(false);
		}
	}
	public class turnLeftAction extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.turn("left");
		}
	}
	public class turnRightAction extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.turn("right");
		}
	}
	public class meleeAction extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.useMeleeAttack();
		}
	}
	public class useAction extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.useSelectedAttack();
		}
	}
	public class firstActive extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.changeSelectedAttack(0);
		}
	}
	public class secondActive extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.changeSelectedAttack(1);
		}
	}
	public class thirdActive extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.changeSelectedAttack(2);
		}
	}
	
	public int[] retTranslate(){
		int[] ret = new int[2];
		int x = -p.getX() + w / 2;
		int y = -p.getY() + h / 2;
		int minX = -(battlefield.getWidth() - w);
		int minY = -(battlefield.getHeight() - h);
		
		if(x < minX){
			x = minX;
		} else if (x > 0){
			x = 0;
		}
		
		if(y < minY){
			y = minY;
		} else if (y > 0){
			y = 0;
		}
		
		ret[0] = x;
		ret[1] = y;
		return ret;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		int[] trans = retTranslate();
		g.translate(trans[0], trans[1]);
		battlefield.draw(g);
		
		timer = new Timer(1000 / FPS, update);
		timer.setRepeats(false);
		timer.start();
	}
}
