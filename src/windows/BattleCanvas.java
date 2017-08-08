package windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.Timer;

import battle.*;
import entities.Player;
import resources.EasyButton;
import resources.KeyRegister;
import initializers.Master;

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
	private boolean paused;
	
	public BattleCanvas(int windowWidth, int windowHeight){
		JPanel panel = this;
		w = windowWidth;
		h = windowHeight;
		setLayout(null);
		setBackground(Color.black);
		setFocusable(true);
		FPS = 20;
		paused = true;
		
		EasyButton b = new EasyButton("Exit", 0, 0, Master.CANVASWIDTH / 10, Master.CANVASHEIGHT / 10, Color.red);
		b.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				new MainWindow();
				JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel);
				frame.dispose();
			}
		});
		b.addTo(this);
		
		update = new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		          hostedBattle.update();
		          repaint();
		          //Op.add("End of frame in battlecanvas");
		          //Op.dp();
		      }
		};
		addKeyRegistration();
	}
	public void setBattle(Team team1, Team team2){
		hostedBattle = new Battle(team1, team2);
		battlefield = new Battlefield();
		
		hostedBattle.setHost(battlefield);
		hostedBattle.init();
		Master.setCurrentBattle(hostedBattle);
		p = hostedBattle.getPlayer();
	}
	
	public void addKeyRegistration(){
		new KeyRegister(this, "w", true, new moveAction());
		new KeyRegister(this, "w", false, new stopAction());
		new KeyRegister(this, "s", true, new reverseAction());
		new KeyRegister(this, "a", true, new turnLeftAction());
		new KeyRegister(this, "d", true, new turnRightAction());
		new KeyRegister(this, "q", true, new meleeAction());
		new KeyRegister(this, "e", true, new useAction());
		new KeyRegister(this, "z", true, new firstActive());
		new KeyRegister(this, "x", true, new secondActive());
		new KeyRegister(this, "c", true, new thirdActive());
		new KeyRegister(this, "p", true, new pauseAction());
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
	public class reverseAction extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			p.switchMoving();
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
	public class pauseAction extends AbstractAction{
		static final long serialVersionUID = 1L;
		public void actionPerformed(ActionEvent e){
			paused = !paused;
			if(!paused){
				startTimer();
			}
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
	
	public void startTimer(){
		timer = new Timer(1000 / FPS, update);
		timer.setRepeats(false);
		timer.start();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		int[] trans = retTranslate();
		g.translate(trans[0], trans[1]);
		battlefield.draw(g);
		
		g.translate(-trans[0], -trans[1]);
		p.drawHUD(g);
		
		if(hostedBattle.shouldEnd()){
			drawMatchResolution(g);
      		return;
        }
		
		if(!paused){
			startTimer();
		} else {
			drawPause(g);
		}
	}
	public void drawPause(Graphics g){
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Master.CANVASWIDTH, Master.CANVASHEIGHT);
		g.setColor(Color.red);
		g.drawString("The game is paused", (int) (Master.CANVASWIDTH * 0.3), (int) (Master.CANVASHEIGHT * 0.3));
		g.drawString("Press 'p' to continue", (int) (Master.CANVASWIDTH * 0.4), (int) (Master.CANVASHEIGHT * 0.5));
	}
	public void drawMatchResolution(Graphics g){
		paused = true;
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, Master.CANVASWIDTH, Master.CANVASHEIGHT);
		g.setColor(Color.yellow);
		g.drawString("The match is ended,", (int) (Master.CANVASWIDTH * 0.3), (int) (Master.CANVASHEIGHT * 0.3));
		g.drawString(hostedBattle.getWinner().getName(), (int) (Master.CANVASWIDTH * 0.5), (int) (Master.CANVASHEIGHT * 0.5));
		g.drawString("is victorious!", (int) (Master.CANVASWIDTH * 0.7), (int) (Master.CANVASHEIGHT * 0.7));
	}
}
