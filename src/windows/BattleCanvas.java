package windows;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Timer;

import battle.*;
import gui.Button;
import resources.KeyRegister;
import initializers.Master;
import initializers.Controls;
import resources.DrawingPlane;
import resources.Chat;

public class BattleCanvas extends DrawingPlane implements MouseListener, KeyListener{
	public static final long serialVersionUID = 1L;
	private Battlefield battlefield;
	private Battle hostedBattle;
	private Timer timer;
	private ActionListener update;
	private boolean paused;
	
	public BattleCanvas(){
		super();
		paused = true;
		
		Button b = new Button("Exit");
		b.addActionListener(new AbstractAction(){
			public static final long serialVersionUID = 12L;
			public void actionPerformed(ActionEvent e){
				new MainWindow();
				close();
			}
		});
		add(b);
		
		Chat.addTo(this);
		
		update = new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		          hostedBattle.update();
		          repaint();
		          //Op.add("End of frame in battlecanvas");
		          //Op.dp();
		      }
		};
		addKeyRegistration();
		resizeComponents(10, 10);
		addMouseListener(this);
		addKeyListener(this);
	}
	
	public void setBattle(Team team1, Team team2){
		hostedBattle = new Battle(this, team1, team2);
		Master.setCurrentBattle(hostedBattle);
		battlefield = new Battlefield();
		
		hostedBattle.setHost(battlefield);
		hostedBattle.init();
		
		Controls.registerControls(this);
	}
	
	public void addKeyRegistration(){
		new KeyRegister(this, KeyEvent.VK_P, true, new pauseAction());
	}
	
	public void mousePressed(MouseEvent e){
		Master.TRUEPLAYER.setFollowingMouse(!Master.TRUEPLAYER.getFollowingMouse());
	}
	public void mouseReleased(MouseEvent e){}
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void keyPressed(KeyEvent e){
		Point mousePos = MouseInfo.getPointerInfo().getLocation();
		int[] t = getLastTransform();
		Master.TRUEPLAYER.turnTo(mousePos.x -t[0], mousePos.y - t[1]);
	}
	public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {}
	
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
		int x = -Master.TRUEPLAYER.getX() + getW() / 2;
		int y = -Master.TRUEPLAYER.getY() + getH() / 2;
		
		/*
		int minX = -(battlefield.getWidth() - getW());
		int minY = -(battlefield.getHeight() - getH());
		
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
		*/
		ret[0] = x;
		ret[1] = y;
		return ret;
	}
	
	public void startTimer(){
		timer = new Timer(1000 / Master.FPS, update);
		timer.setRepeats(false);
		timer.start();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		setG(g);
		int[] trans = retTranslate();
		translate(trans[0], trans[1]);
		battlefield.draw(getG());
		
		resetToInit();
		
		Master.TRUEPLAYER.drawHUD(getG());
		
		if(hostedBattle.shouldEnd()){
			drawMatchResolution(getG());
      		return;
        }
		
		if(!paused){
			startTimer();
		} else {
			drawPause(getG());
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