package statuses;

import entities.Player;
import initializers.Master;
import resources.OnHitAction;
import resources.Random;
import resources.Direction;

public class Daze extends Status{
	public Daze(int lv, int dur){
		super("Daze", lv, dur);
	}
	public void inflictOn(Player p){
		OnHitAction a = new OnHitAction(){
			public void f(){
				p.setDir(new Direction(Random.choose(0, 360) / Master.TICKSTOROTATE));
				use();
			}
		};
		p.getActionRegister().addOnBeHit(a);
	}
}
