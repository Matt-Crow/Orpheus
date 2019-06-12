package gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import actives.AbstractActive;
import actives.ActiveStatName;
import entities.ParticleType;

@SuppressWarnings("serial")
public class ActiveCustomizer extends UpgradableCustomizer<ActiveStatName>{
	public ActiveCustomizer(AbstractActive a){
		super(a);
		switch(a.getActiveType()){
		case MELEE:
			addBox(ActiveStatName.DAMAGE);
			//need to do more here
			break;
		case BOOST:
			addStatusBoxes();
			break;
		case ELEMENTAL:
			addBox(ActiveStatName.ARC);
			addBox(ActiveStatName.RANGE);
			addBox(ActiveStatName.SPEED);
			addBox(ActiveStatName.AOE);
			addBox(ActiveStatName.DAMAGE);
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
		a.setStat(ActiveStatName.valueOf(n.toUpperCase()), val);
		a.init();
		super.updateField(n, val);
	}
	public void save(){
		// this is invoked by UpgradableCustomizer
		super.save();
		AbstractActive.addActive((AbstractActive)getCustomizing());
	}
}
