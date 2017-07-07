package resources;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class OnUpdateAction extends AbstractAction{
	public static final long serialVersionUID = 1L;
	AbstractAction action;
	public OnUpdateAction(){
		
	}
	public void setAction(AbstractAction a){
		action = a;
	}
	public void actionPerformed(ActionEvent e){
		trip();
	}
	public void trip(){
		action.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null){});
	}
}
