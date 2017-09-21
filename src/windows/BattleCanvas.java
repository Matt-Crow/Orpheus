package windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.sun.glass.events.KeyEvent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.Timer;

import battle.*;
import entities.Player;
import resources.EasyButton;
import resources.KeyRegister;
import resources.Direction;
import initializers.Master;
import initializers.Run;
import initializers.Controls;

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
			public static final long serialVersionUID = 12L;
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
		p = Run.player;
		Controls.registerPlayerControlsTo(this, p);
	}
	
	public void addKeyRegistration(){
		new KeyRegister(this, KeyEvent.VK_P, true, new pauseAction());
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
		
		Direction rotTo = new Direction(p.getDir().getDegrees());
		rotTo.turnClockwise(90);
		
        Graphics2D g2d = (Graphics2D)g;
        AffineTransform old = g2d.getTransform();
        g2d.translate(Master.CANVASWIDTH / 2, Master.CANVASHEIGHT / 2);
        
        g2d.rotate(rotTo.getRadians());
        g2d.translate(-(Master.CANVASWIDTH / 2), -(Master.CANVASHEIGHT / 2));
		int[] trans = retTranslate();
		g2d.translate(trans[0], trans[1]);
		battlefield.draw(g2d);
		
		g2d.translate(-trans[0], -trans[1]);
		
		g2d.setTransform(old);
		
		p.drawHUD(g2d);
		
		if(hostedBattle.shouldEnd()){
			drawMatchResolution(g2d);
      		return;
        }
		
		if(!paused){
			startTimer();
		} else {
			drawPause(g2d);
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
