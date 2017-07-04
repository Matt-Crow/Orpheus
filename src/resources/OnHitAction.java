package resources;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import entities.Player;
import resources.Op;

public class OnHitAction extends AbstractAction{
	public static final long serialVersionUID = 1L;
	Player wasHit;
	AbstractAction action;
	public OnHitAction(){
		
	}
	public void setAction(AbstractAction a){
		action = a;
	}
	public void setTarget(Player p){
		wasHit = p;
	}
	public Player getTarget(){
		return wasHit;
	}
	public void actionPerformed(ActionEvent e){
		trip(wasHit);
	}
	public void trip(Player p){
		Op.add("BOOM! POW!");
		Op.add(p.getName());
		Op.dp();
		action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null){});
	}
}
