package customizables;

import customizables.actives.AbstractActive;
import customizables.actives.ActiveTag;
import customizables.actives.BoostActive;
import customizables.actives.ElementalActive;
import customizables.actives.MeleeActive;
import entities.ParticleType;
import graphics.CustomColors;
import java.util.Collection;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Set;
import statuses.AbstractStatus;
import statuses.Burn;
import statuses.Regeneration;
import statuses.Resistance;
import statuses.Rush;
import statuses.Strength;
import statuses.Stun;

/**
 * The DataSet class is used to store all the Actives, Passives, CharacterClasses, and Builds.
 * Future versions will add the ability to load additional classes at runtime.
 * 
 * @author Matt Crow
 */
public final class DataSet {
    public final HashMap<String, AbstractActive> allActives;
    private final AbstractActive DEFAULT_ACTIVE = new ElementalActive("Default", 3, 3, 3, 3, 3);
    
    
    public DataSet(){
        allActives = new HashMap<>();
    }
    
    public void addActive(AbstractActive a){
        allActives.put(a.getName().toUpperCase(), a.copy());
    }
    public void addActives(AbstractActive[] as){
        for(AbstractActive a : as){
            addActive(a);
        }
    }
    
    /**
     * Finds an active with the given name, ignoring case.
     * If no active is found, throws a NoSuchElementException
     * @param n the name of the active to search for
     * @return the active with that name
     */
    public AbstractActive getActiveByName(String n){
        if(!allActives.containsKey(n.toUpperCase())){
            throw new NoSuchElementException(n + " is not in the list of actives. Did you remember to call AbstractActive.addActive(...);?");
        }
        return allActives.get(n.toUpperCase()).copy();
    }
    public AbstractActive[] getAllActives(){
        AbstractActive[] ret = new AbstractActive[allActives.size()];
        Collection<AbstractActive> values = allActives.values();
        int i = 0;
        for(AbstractActive aa : values){
            ret[i] = aa;
            i++;
        }
        return ret;
    }
    public String[] getAllActiveNames(){
        String[] ret = new String[allActives.size()];
        Set<String> keys = allActives.keySet();
        int i = 0;
        for(String key : keys){
            ret[i] = key;
            i++;
        }
        return ret;
    }
    
    public AbstractActive getDefaultActive(){
        return DEFAULT_ACTIVE.copy();
    }
    
    public void loadDefaultActives(){
        // read from file later?
		MeleeActive s = new MeleeActive("Slash", 3);
		
		ElementalActive bt = new ElementalActive("Boulder Toss", 1, 2, 2, 3, 4);
		bt.setParticleType(ParticleType.BURST);
        bt.setColors(CustomColors.earthColors);
        bt.addTag(ActiveTag.KNOCKSBACK);
		
        ElementalActive eq = new ElementalActive("Earthquake", 1, 0, 2, 5, 1);
		eq.setParticleType(ParticleType.BURST);
        eq.setColors(CustomColors.earthColors);
        eq.addStatus(new Stun(3, 1));
        
		ElementalActive fof = new ElementalActive("Fields of Fire", 1, 0, 5, 3, 1);
		fof.setParticleType(ParticleType.SHEAR);
        fof.setColors(CustomColors.fireColors);
        fof.addStatus(new Burn(2, 3));
		
		ElementalActive fb = new ElementalActive("Fireball", 2, 3, 3, 3, 5);
		fb.setParticleType(ParticleType.BURST);
        fb.setColors(CustomColors.fireColors);
        
		ElementalActive b = new ElementalActive("Boreus", 1, 5, 5, 0, 1);
		b.setParticleType(ParticleType.BEAM);
        b.setColors(CustomColors.airColors);
        
        ElementalActive z = new ElementalActive("Zephyrus", 1, 5, 5, 0, 1);
		z.setParticleType(ParticleType.BEAM);
		z.setColors(CustomColors.airColors);
        
        ElementalActive wb = new ElementalActive("Waterbolt", 1, 3, 3, 1, 2);
		wb.setParticleType(ParticleType.BEAM);
        wb.setColors(CustomColors.waterColors);
        
        ElementalActive wp = new ElementalActive("Whirlpool", 1, 0, 4, 4, 3);
        wp.setParticleType(ParticleType.SHEAR);
        wp.setColors(CustomColors.waterColors);
        
		ElementalActive rod = new ElementalActive("RAINBOW OF DOOM", 4, 3, 5, 5, 1);
		rod.setParticleType(ParticleType.BURST);
		rod.setColors(CustomColors.rainbow);
		
		
		BoostActive ws = new BoostActive("Warrior's Stance", new AbstractStatus[]{new Strength(1, 2), new Resistance(1, 2)});
		BoostActive st = new BoostActive("Speed Test", new AbstractStatus[]{new Rush(2, 3)});
		BoostActive ss = new BoostActive("Shield Stance", new AbstractStatus[]{new Resistance(2, 3)});
		BoostActive hr = new BoostActive("Healing Rain", new AbstractStatus[]{new Regeneration(2, 3)});
		BoostActive bs = new BoostActive("Blade Stance", new AbstractStatus[]{new Strength(2, 3)});
        BoostActive br = new BoostActive("Burning Rage", new AbstractStatus[]{new Strength(3, 3), new Burn(3, 3)});
		
		addActives(new AbstractActive[]{
			s,
			bt,
			eq,
			fof,
			fb,
			br,
			b,
            z,
			rod,
			wb,
            wp,
			ws,
			st,
			ss,
			hr,
			bs
		});
    }
    
    public void loadDefaults(){
        loadDefaultActives();
    }
}
