package attacks;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import entities.Player;

public class Flamethrower extends ElementalAttack{
	private ActionListener recur;
	private int recurCount;
	private Player recurUser;
	
	public Flamethrower(){
		super("Flamethrower", 3, 5, 2, 2, 0, 2);
		recurCount = 0;
		recur = new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(recurCount == 5){
					recurCount = 0;
					return;
				}
				use(recurUser);
				recurCount += 1;
			}
		};
	}
	public void use(Player user){
		super.use(user);
		recurUser = user;
		
		Timer t = new Timer(1000, recur);
		t.setRepeats(false);
		t.start();
	}
}
