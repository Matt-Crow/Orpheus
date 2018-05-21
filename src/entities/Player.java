package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import actives.*;
import battle.*;
import customizables.*;
import resources.*;
import passives.*;
import ai.PlayerAI;
import statuses.Status;
import initializers.Master;

// this is going to be very big
public class Player extends Entity{
	private String name;
	private CharacterClass c;
	private Active[] actives;
	private Passive[] passives;
	
	private DamageBacklog log;
	private EnergyLog energyLog;
	
	private Slash slash;
	private ArrayList<Status> statuses;
	
	private PlayerAI playerAI;
	
	public Player(String n){
		super(500 / Master.FPS);
		name = n;
		slash = new Slash();
		slash.registerTo(this);
		actives = new Active[3];
		passives = new Passive[3];
		setType(EntityType.PLAYER);
	}
	
	public String getName(){
		return name;
	}
	
	public Active[] getActives(){
		return actives;
	}
	
	public DamageBacklog getLog(){
		return log;
	}
	public EnergyLog getEnergyLog(){
		return energyLog;
	}
	public CharacterClass getCharacterClass(){
		return c;
	}
	public PlayerAI getPlayerAI(){
		return playerAI;
	}
	
	// Build stuff
	public void applyBuild(Build b){
		setClass(b.getClassName());
		setActives(b.getActiveNames());
		setPassives(b.getPassiveNames());
		setSpeed((int) (c.getStatValue("speed") * (500 / Master.FPS)));
	}
	public void setClass(String name){
		switch(name.toLowerCase()){
			case "fire":
				c = new Fire();
				break;
			case "earth":
				c = new Earth();
				break;
			case "water":
				c = new Water();
				break;
			case "air":
				c = new Air();
				break;
			default:
				String[] classes = {"fire", "earth", "water", "air"};
				int randomNum = Random.choose(0, 4);
				setClass(classes[randomNum]);
				break;
		}
		
		for(Active a : c.getAttackOption()){
			a.registerTo(this);
		}
		for(Passive p : c.getPassiveOptions()){
			p.registerTo(this);
		}
	}
	public void setActives(String[] names){
		for(int nameIndex = 0; nameIndex < 3; nameIndex ++){
			boolean found = false;
			
			ArrayList<Active> attacks = Active.getAllActives();
			for(int i = 0; i < attacks.size() && !found; i++){
				if(attacks.get(i).getName().equals(names[nameIndex])){
					actives[nameIndex] = attacks.get(i);
					actives[nameIndex].registerTo(this);
					found = true;
				}
			}
			for(int i = 0; i < c.getAttackOption().length && !found; i++){
				if(c.getAttackOption()[i].getName().equals(names[nameIndex])){
					actives[nameIndex] = c.getAttackOption()[i];
					found = true;
				}
			}
			if(!found){
				actives[nameIndex] = new Slash();
				Op.add("The active by the name of " + names[nameIndex]);
				Op.add("is not found for the characterClass " + c.getName());
				Op.dp();
			}
		}
	}
	public void setPassives(String[] names){
		for(int nameIndex = 0; nameIndex < 3; nameIndex ++){
			boolean found = false;
			for(Passive p : c.getPassiveOptions()){
				if(p.getName() == names[nameIndex]){
					passives[nameIndex] = p;
					found = true;
					break;
				}
			}
			if(!found){
				passives[nameIndex] = new Passive("UNDEFINED");
				Op.add("The passive by the name of " + names[nameIndex]);
				Op.add("is not found for the characterClass " + c.getName());
				Op.dp();
			}
		}
	}
	
	public void inflict(Status newStatus){
		Status oldStatus = new Status("Placeholder", -1, -1);
		int oldLevel = 0;
		
		for(Status s : statuses){
			if(s.getName() == newStatus.getName()){
				oldStatus = s;
				oldLevel = s.getIntensityLevel();
				break;
			}
		}		
				
		if(oldLevel < newStatus.getIntensityLevel()){
			oldStatus.terminate();
			statuses.add(newStatus);
			newStatus.reset();
			return;
		} else {
			oldStatus.reset();
			return;
		}
	}
	
	public double getStatValue(String n){
		return c.getStatValue(n);
	}
	public void useMeleeAttack(){
		if(slash.canUse()){
			slash.use();
		}
	}
	
	public void useAttack(int num){
		if(actives[num].canUse()){
			actives[num].use();
		}
	}
	
	public void logDamage(int dmg){
		log.log(dmg);
	}
	
	public void init(Team t, int x, int y, Direction d){
		super.init(x, y, d.getDegrees());
		playerAI = new PlayerAI(this);
		
		if (!(this instanceof TruePlayer) && !Master.DISABLEALLAI){
			playerAI.enable();
		}
		setTeam(t);
		slash.init();
		c.calcStats();
		log = new DamageBacklog(this);
		energyLog = new EnergyLog(this);
		statuses = new ArrayList<>();
		for(Active a : actives){
			a.init();
		}
		for(Passive p : passives){
			p.registerTo(this);
		}
	}
	
	public void updateStatuses(){
		ArrayList<Status> newStatuses = new ArrayList<>();
		for(Status s : statuses){
			if(s.getShouldTerminate()){
				
			}else{
				newStatuses.add(s);
			}
		}
		statuses = newStatuses;
		for(Status s : statuses){
			s.inflictOn(this);
		}
	}
	
	public void update(){
		super.update();
		playerAI.update();
		slash.update();
		getActionRegister().resetTrips();
		for(Active a : actives){
			a.update();
		}
		for(Passive p : passives){
			p.update();
		}
		updateStatuses();
		getActionRegister().tripOnUpdate();
		log.update();
		energyLog.update();
		
		if(getShouldTerminate()){
			getTeam().loseMember();
		}
	}
	
	public void draw(Graphics g){
		int w = Master.CANVASWIDTH;
		int h = Master.CANVASHEIGHT;
		
		// HP value
		g.setColor(Color.black);
		g.drawString("HP: " + log.getHP(), getX() - w/12, getY() - h/8);
		
		// Update this to sprite later, doesn't scale to prevent hitbox snafoos
		g.setColor(getTeam().getColor());
		g.fillOval(getX() - 50, getY() - 50, 100, 100);
		g.setColor(c.getColor());
		g.fillOval(getX() - 40, getY() - 40, 80, 80);
		
		g.setColor(Color.black);
		int y = getY() + h/10;
		for(Status s : statuses){
			String iStr = "";
			int i = 0;
			while(i < s.getIntensityLevel()){
				iStr += "I";
				i++;
			}
			g.drawString(s.getName() + " " + iStr + "(" + s.getUsesLeft() + ")", getX() - 50, y);
			y += h/30;
		}
	}
}