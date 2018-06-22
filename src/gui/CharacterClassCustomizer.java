package gui;

import customizables.CharacterClass;
import customizables.CharacterStat;

@SuppressWarnings("serial")
public class CharacterClassCustomizer extends UpgradableCustomizer{
	public CharacterClassCustomizer(CharacterClass c){
		super(c);
		for(CharacterStat cs : CharacterStat.values()){
			addBox(cs.toString());
		}
		
	}
	public void updateField(String n, int val){
		CharacterClass c = (CharacterClass)getCustomizing();
		c.setStat(CharacterStat.valueOf(n.toUpperCase()), val);
		c.init();
		super.updateField(n, val);
	}
	public void save(){
		super.save();
		CharacterClass.addCharacterClass((CharacterClass)getCustomizing());
	}
}
