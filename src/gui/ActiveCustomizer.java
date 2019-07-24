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
            updateField(ActiveStatName.DAMAGE.toString(), a.getBase(ActiveStatName.DAMAGE));
			//need to do more here
			break;
		case BOOST:
			addStatusBoxes();
			break;
		case ELEMENTAL:
            //change these to active.getmax / min
			addBox(ActiveStatName.ARC, 0, 5);
            updateField(ActiveStatName.ARC.toString(), a.getBase(ActiveStatName.ARC));
			addBox(ActiveStatName.RANGE, 0, 5);
            updateField(ActiveStatName.RANGE.toString(), a.getBase(ActiveStatName.RANGE));
			addBox(ActiveStatName.SPEED, 0, 5);
            updateField(ActiveStatName.SPEED.toString(), a.getBase(ActiveStatName.SPEED));
			addBox(ActiveStatName.AOE, 0, 5);
            updateField(ActiveStatName.AOE.toString(), a.getBase(ActiveStatName.AOE));
			addBox(ActiveStatName.DAMAGE, 0, 5);
            updateField(ActiveStatName.DAMAGE.toString(), a.getBase(ActiveStatName.DAMAGE));
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
		super.updateField(n, val);
	}
	public void save(){
		// this is invoked by Customizer
		super.save();
		AbstractActive.addActive((AbstractActive)getCustomizing());
	}
}
