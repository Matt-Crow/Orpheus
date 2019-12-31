package gui;

import controllers.Master;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import customizables.characterClass.CharacterClass;
import customizables.characterClass.CharacterStatName;
import graphics.CustomColors;

@SuppressWarnings("serial")
public class CharacterClassCustomizer extends Customizer<CharacterStatName>{
	public CharacterClassCustomizer(CharacterClass c){
		super(c);
		for(CharacterStatName cs : CharacterStatName.values()){
			addBox(cs, 1, 5);
            updateField(cs.toString(), c.getBase(cs));
		}
		addColorBox();
	}
	public void updateField(String n, int val){
		CharacterClass c = (CharacterClass)getCustomizing();
		c.setStat(CharacterStatName.fromString(n.toUpperCase()), val);
		super.updateField(n, val);
	}
	public void addColorBox(){
		CustomColors[][] options = CustomColors.all;
		OptionBox<CustomColors[]> box = new OptionBox<>("Colors", options);
		box.setSelected(((CharacterClass)getCustomizing()).getColors());
		box.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				((CharacterClass)getCustomizing()).setColors(box.getSelected());
			}
		});
		addBox(box);
	}
	public void save(){
		super.save();
		Master.getDataSet().addCharacterClass((CharacterClass)getCustomizing());
	}
}
