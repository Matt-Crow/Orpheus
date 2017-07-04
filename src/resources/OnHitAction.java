package resources;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

import entities.Player;
import resources.Op;

public class OnHitAction extends AbstractAction{
	public static final long serialVersionUID = 1L;
	Player wasHit;
	public OnHitAction(){
		
	}
	public void setTarget(Player p){
		wasHit = p;
	}
	public void actionPerformed(ActionEvent e){
		trip(wasHit);
	}
	public void trip(Player p){
		Op.add("BOOM! POW!");
		Op.add(p.getName());
		Op.dp();
	}
}
