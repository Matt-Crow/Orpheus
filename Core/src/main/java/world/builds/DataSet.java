package world.builds;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import gui.graphics.CustomColors;
import orpheus.core.champions.Champion;
import orpheus.core.champions.ChampionSpecification;
import orpheus.core.champions.Specification;
import orpheus.core.champions.orpheus.OrpheusChampion;
import orpheus.core.utils.PrototypeFactory;
import world.builds.actives.*;
import world.builds.characterClass.CharacterClass;
import world.builds.passives.*;
import world.entities.ParticleType;
import world.statuses.*;

/**
 * Store all the Actives, Passives, CharacterClasses, and Builds.
 * Future versions may add the ability to load additional classes at runtime.
 * 
 * Currently, the Settings class contains a static DataSet, which is 
 * automatically populated
 * 
 * @author Matt Crow
 */
public final class DataSet {
    private final PrototypeFactory<ChampionSpecification> championSpecifications = new PrototypeFactory<>();
    private final PrototypeFactory<Champion> champions = new PrototypeFactory<>();
    private final PrototypeFactory<Build> builds = new PrototypeFactory<>();
    private final PrototypeFactory<CharacterClass> characterClasses = new PrototypeFactory<>();
    private final PrototypeFactory<AbstractActive> actives = new PrototypeFactory<>();
    private final PrototypeFactory<AbstractPassive> passives = new PrototypeFactory<>();
    
    private final CharacterClass DEFAULT_CHARACTER_CLASS = new CharacterClass("Default", CustomColors.WHITE, 3, 3, 3, 3);
    private final AbstractPassive DEFAULT_PASSIVE = new ThresholdPassive("Default", 2);
    private final Build DEFAULT_BUILD = new Build("0x138", "Default", "RAINBOW OF DOOM", "Hammer Toss", "Speed Test", "Cinder Strikes", "Escapist", "Cursed");
    
    public DataSet(){        
        DEFAULT_PASSIVE.addStatus(new Resistance(2, 2));
        
        passives.add(DEFAULT_PASSIVE);
        addCharacterClass(DEFAULT_CHARACTER_CLASS);
        addBuild(DEFAULT_BUILD);
    }

    public void addChampion(Champion champion) {
        championSpecifications.add(champion.getSpecification());
        champions.add(champion);
    }

    public void addBuild(Build b){
		builds.add(b);
	}

    public void addCharacterClass(CharacterClass c){
        characterClasses.add(c);
    }

    public void addActives(AbstractActive[] as){
        actives.addAll(Arrays.asList(as));
    }

    public void addPassives(AbstractPassive[] ps){
		passives.addAll(Arrays.asList(ps));
	}

    public Champion getChampionByName(String name) {
        return champions.make(name);  
    }

    public CharacterClass getCharacterClassByName(String n){
        return characterClasses.make(n);
    }

    public AbstractActive getActiveByName(String n){
        return actives.make(n);
    }
    
    public AbstractPassive getPassiveByName(String n){
        return passives.make(n);
	}

    /**
     * @return all specifications players can choose from
     */
    public Collection<Specification> getAllSpecifications() {
        var result = new ArrayList<Specification>();
        result.addAll(championSpecifications.getAll());
        result.addAll(builds.getAll());
        return result;
    }

    public Build[] getAllBuilds() {
        // needs to be seperate from getAllSpecs since players can customize builds
        return builds.getAll().toArray(Build[]::new);
    }
    
    public CharacterClass[] getAllCharacterClasses(){
        return characterClasses.getAll().toArray(CharacterClass[]::new);
    }

    public AbstractActive[] getAllActives(){
        return actives.getAll().toArray(AbstractActive[]::new);
    }

    public AbstractPassive[] getAllPassives(){
		return passives.getAll().toArray(AbstractPassive[]::new);
	}

    public AssembledBuild assemble(Build b) {
        var assembled = new AssembledBuild(
            b.getName(), 
            getCharacterClassByName(b.getClassName()), 
            Arrays.stream(b.getActiveNames()).map(this::getActiveByName).toArray((s) -> new AbstractActive[s]),
            Arrays.stream(b.getPassiveNames()).map(this::getPassiveByName).toArray((s) -> new AbstractPassive[s])
        );

        return assembled;
    } 

    public void loadDefaults(){
        loadDefaultCharacterClasses();
        loadDefaultActives();
        loadDefaultPassives();
        loadDefaultBuilds();
        addChampion(new OrpheusChampion());
    }

    private void loadDefaultCharacterClasses(){
		characterClasses.addAll(Arrays.asList(new CharacterClass[]{
            new CharacterClass("Fire", CustomColors.RED, 1, 5, 4, 3),
            new CharacterClass("Air", CustomColors.YELLOW, 2, 3, 1, 5),
            new CharacterClass("Earth", CustomColors.GREEN, 4, 4, 4, 1),
            new CharacterClass("Water", CustomColors.BLUE, 5, 1, 3, 3)
        }));
    }

    private void loadDefaultActives(){		
        ElementalActive eq = new ElementalActive("Earthquake", Arc.CIRCULAR, Range.NONE, 2, Range.LONG, 1);
		eq.setParticleType(ParticleType.BURST);
        eq.setColors(CustomColors.EARTH_COLORS);
        eq.addStatus(new Stun(3, 1));
        
		ElementalActive fof = new ElementalActive("Fields of Fire", Arc.CIRCULAR, Range.NONE, 5, Range.MEDIUM, 1);
		fof.setParticleType(ParticleType.SHEAR);
        fof.setColors(CustomColors.FIRE_COLORS);
        fof.addStatus(new Burn(2, 3));
		
		ElementalActive fb = new ElementalActive("Fireball", Arc.NARROW, Range.MEDIUM, 3, Range.MEDIUM, 5);
		fb.setParticleType(ParticleType.BURST);
        fb.setColors(CustomColors.FIRE_COLORS);
        
		ElementalActive b = new ElementalActive("Boreus", Arc.NONE, Range.LONG, 5, Range.NONE, 1);
		b.setParticleType(ParticleType.BEAM);
        b.setColors(CustomColors.AIR_COLORS);
        
        ElementalActive z = new ElementalActive("Zephyrus", Arc.NONE, Range.LONG, 5, Range.NONE, 1);
		z.setParticleType(ParticleType.BEAM);
		z.setColors(CustomColors.AIR_COLORS);
        
        ElementalActive wb = new ElementalActive("Waterbolt", Arc.NARROW, Range.MEDIUM, 3, Range.SHORT, 2);
		wb.setParticleType(ParticleType.BEAM);
        wb.setColors(CustomColors.WATER_COLORS);
        
        ElementalActive wp = new ElementalActive("Whirlpool", Arc.CIRCULAR, Range.NONE, 4, Range.LONG, 3);
        wp.setParticleType(ParticleType.SHEAR);
        wp.setColors(CustomColors.WATER_COLORS);
        
		ElementalActive rod = new ElementalActive("RAINBOW OF DOOM", Arc.WIDE, Range.MEDIUM, 5, Range.LONG, 1);
		rod.setParticleType(ParticleType.BURST);
		rod.setColors(CustomColors.RAINBOW_COLORS);
		
		
		BoostActive ws = new BoostActive("Warrior's Stance", new AbstractStatus[]{new Strength(1, 2), new Resistance(1, 2)});
		BoostActive st = new BoostActive("Speed Test", new AbstractStatus[]{new Rush(2, 3)});
		BoostActive ss = new BoostActive("Shield Stance", new AbstractStatus[]{new Resistance(2, 3)});
		BoostActive hr = new BoostActive("Healing Rain", new AbstractStatus[]{new Regeneration(2, 3)});
		BoostActive bs = new BoostActive("Blade Stance", new AbstractStatus[]{new Strength(2, 3)});
        BoostActive br = new BoostActive("Burning Rage", new AbstractStatus[]{new Strength(3, 3), new Burn(3, 3)});
		
		addActives(new AbstractActive[]{
			new BoulderToss(),
            new FlameCharge(),
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
			bs,
            new HammerToss()
		});
    }

    private void loadDefaultPassives(){
        // on be hit
		OnBeHitPassive t = new OnBeHitPassive("Toughness", true);
		t.addStatus(new Resistance(1, 1));
        OnBeHitPassive cu = new OnBeHitPassive("Cursed", false);
        cu.addStatus(new Stun(3, 1));
		
        // on hit
        OnHitPassive ch = new OnHitPassive("Crippling Hits", false);
        ch.addStatus(new Stun(1, 1));
        OnHitPassive lh = new OnHitPassive("Leechhealer", true);
		lh.addStatus(new Regeneration(1, 1));
		OnHitPassive m = new OnHitPassive("Momentum", true);
		m.addStatus(new Rush(1, 1));
		OnHitPassive s = new OnHitPassive("Sharpen", true);
		s.addStatus(new Strength(1, 1));
        
		//threshold
		ThresholdPassive b = new ThresholdPassive("Bracing", 1);
		b.addStatus(new Resistance(2, 1));
		ThresholdPassive d = new ThresholdPassive("Determination", 2);
		d.addStatus(new Strength(1, 1));
		d.addStatus(new Resistance(1, 1));
		ThresholdPassive e = new ThresholdPassive("Escapist", 2);
		e.addStatus(new Rush(2, 1));
		ThresholdPassive re = new ThresholdPassive("Retaliation", 2);
		re.addStatus(new Strength(2, 1));
        
        
		addPassives(new AbstractPassive[]{
            lh,
            m,
            s,
            t,
            b,
            d,
            e,
            re,
            cu,
            ch,
            new CinderStrikes()
		});
    }

    private void loadDefaultBuilds(){
        builds.addAll(Arrays.asList(new Build[]{
            new Build("Default Earth", "Earth", "Boulder Toss", "Warrior's Stance", "Earthquake", "Toughness", "Retaliation", "Crippling Hits"),
            new Build("Default Fire", "Fire", "Fireball", "Fields of Fire", "Burning Rage", "Escapist", "Cinder Strikes", "Bracing"),
            new Build("Default Water", "Water", "Waterbolt", "Whirlpool", "Healing Rain", "Sharpen", "Bracing", "Leechhealer"),
            new Build("Default Air", "Air", "Boreus", "Zephyrus", "Speed Test", "Momentum", "Sharpen", "Leechhealer"),
            new Build("Flame Charge Fire", "Fire", "Flame Charge", "Earthquake", "Burning Rage", "Cinder Strikes", "Momentum", "Crippling Hits"),
        }));
    } 
}
