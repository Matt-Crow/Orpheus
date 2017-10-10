package windows;

import java.awt.Color;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import java.util.ArrayList;
import initializers.Master;
import resources.DrawingPlane;
import resources.Menu;
import resources.EasyButton;

public class BuildCanvas extends DrawingPlane{
	public static final long serialVersionUID = 1L;
	private Menu chooseClass;
	private Menu chooseAct1;
	private Menu chooseAct2;
	private Menu chooseAct3;
	private Menu choosePas1;
	private Menu choosePas2;
	private Menu choosePas3;
	
	private EasyButton setClass;
	private EasyButton finish;
	
	public BuildCanvas(int windowWidth, int windowHeight){
		super(windowWidth, windowHeight);
		//customcolors
		EasyButton quit = new EasyButton("Quit without saving", 0, 0, Master.CANVASWIDTH / 10, Master.CANVASHEIGHT / 10, Color.red);
		quit.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				new MainWindow();
				close();
			}
		});
		quit.addTo(this);
		
		// automate?
		String[] classes = {"earth", "fire", "air", "water"};
		
		chooseClass = new Menu(classes, Master.CANVASWIDTH / 2, Master.CANVASHEIGHT / 2, 100, 100);
		chooseClass.addActionListener(getRepaint());
		chooseClass.addTo(this);
		
		setClass = new EasyButton("Choose selected class", Master.CANVASWIDTH / 2, Master.CANVASHEIGHT / 2, 300, 100, Color.white);
		setClass.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				chooseClass.setEnabled(false);
				setClass.setEnabled(false);
				setClass.setVisible(false);
				phase2();
			}
		});
		setClass.addTo(this);
	}
	public void phase2(){
		
	}
}
