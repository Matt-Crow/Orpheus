package gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import customizables.actives.AbstractActive;
import customizables.actives.ActiveStatName;
import entities.ParticleType;

@SuppressWarnings("serial")
public class ActiveCustomizer extends Customizer<ActiveStatName>{
	public ActiveCustomizer(AbstractActive a){
		super(a);
		switch(a.getActiveType()){
		case MELEE:
			addBox(ActiveStatName.DAMAGE, 0, 5);
			//need to do more here
			break;
		case BOOST:
			addStatusBoxes();
			break;
		case ELEMENTAL:
            //change these to active.getmax / min
			addBox(ActiveStatName.ARC, 0, 5);
			addBox(ActiveStatName.RANGE, 0, 5);
			addBox(ActiveStatName.SPEED, 0, 5);
			addBox(ActiveStatName.AOE, 0, 5);
			addBox(ActiveStatName.DAMAGE, 0, 5);
			addParticleBox();
			break;
		}
	}
	private void addParticleBox(){
		ParticleType[] options = ParticleType.values();
		OptionBox<ParticleType> box = new OptionBox<>("Particles", options);
		box.setSelected(((AbstractActive)getCustomizing()).getParticleType());
		// don't need update desc
		box.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				((AbstractActive)getCustomizing()).setParticleType(box.getSelected());
			}
		});
		addBox(box);
	}
	public void updateField(String n, int val){
		AbstractActive a = (AbstractActive)getCustomizing();
		a.setStat(ActiveStatName.fromString(n.toUpperCase()), val);
		a.init();
		super.updateField(n, val);
	}
	public void save(){
		// this is invoked by Customizer
		super.save();
		AbstractActive.addActive((AbstractActive)getCustomizing());
	}
}
