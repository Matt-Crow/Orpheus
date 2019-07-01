package entities;

import java.awt.Graphics;

import graphics.CustomColors;
import actives.AbstractActive;
import actives.ActiveStatName;
import actives.ActiveTag;
import actives.MeleeActive;
import customizables.CharacterStatName;
import controllers.Master;
import util.CombatLog;
import util.Random;

public class Projectile extends Entity{
	private final Player user;
	private final AbstractActive registeredAttack;
	private int distanceTraveled;
	private int range;
	private Player hit;
	
	private final int useId; //used to prevent double hitting. May not be unique to a single projectile. See AbstractActive for more info
    
	public Projectile(int useId, int x, int y, int degrees, int momentum, Player attackUser, AbstractActive a){
		super();
        setSpeed(momentum);
        doInit();
		initPos(x, y, degrees);
		this.useId = useId;
		distanceTraveled = 0;
		user = attackUser;
		setTeam(user.getTeam());
		registeredAttack = a;
		range = (int) a.getStatValue(ActiveStatName.RANGE);
		setRadius(25);
		setMoving(true);
		hit = new Player("Void");
	}
	public int getUseId(){
		return useId;
	}
	public void setRange(int i){
		range = i;
	}
	
	public String getAttackName(){
		return registeredAttack.getName();
	}
	public Player getUser(){
		return user;
	}
	public Player getHit(){
		return hit;
	}
	public int getDistance(){
		return distanceTraveled;
	}
	public AbstractActive getAttack(){
		return registeredAttack;
	}
	
	public void hit(Player p){
		hit = p;
		p.logDamage((int) (registeredAttack.getStatValue(ActiveStatName.DAMAGE) * user.getStatValue(CharacterStatName.DMG) * p.getStatValue(CharacterStatName.REDUCTION)));
		p.setLastHitById(getUseId());
		if(registeredAttack instanceof MeleeActive){
			user.getActionRegister().tripOnMeleeHit(p);
			user.getEnergyLog().gainEnergy(5);
			p.getActionRegister().tripOnBeMeleeHit(user);
		} else {
			user.getActionRegister().triggerOnHit(p);
			p.getActionRegister().triggerOnHitReceived(user);
		}
		getActionRegister().triggerOnHit(p);
        
        if(registeredAttack.containsTag(ActiveTag.KNOCKSBACK)){
            p.knockBack(range, getDir(), Master.seconds(1));
        }
        
		CombatLog.logProjectileData(this);
		terminate();
	}
    
	public boolean checkForCollisions(Player p){
		boolean ret = super.checkForCollisions(p);
		if(ret && p.getLastHitById() != useId){
			ret = true;
			hit(p);
		}
		return ret;
	}
	
	public void spawnParticle(int degrees, int m, CustomColors c){
		Particle p = new Particle(m, c);
        p.doInit();
		p.initPos(getX(), getY(), degrees);
		p.setTeam(this.getTeam());
        //p.insertAfter(this);
        spawn(p);
	}
	@Override
    public void init() {
        
    }
    
    @Override
	public void update(){
        distanceTraveled += getMomentum();
		
		// need to change range based on projectile type: attack range for seed, aoe for aoeprojectile
		if(distanceTraveled >= range && !getShouldTerminate()){
			terminate();
		}
		
		CustomColors[] cs = user.getCharacterClass().getColors();
		
		if(!Master.DISABLEPARTICLES && !getShouldTerminate()){
			switch(registeredAttack.getParticleType()){
			case BURST:
				for(int i = 0; i < Master.TICKSTOROTATE; i++){
					CustomColors rbu = cs[Random.choose(0, cs.length - 1)];
					spawnParticle(360 * i / Master.TICKSTOROTATE, 5, rbu);
				}
				break;
			case SHEAR:
				CustomColors rs = cs[Random.choose(0, cs.length - 1)];
				spawnParticle(getDir().getDegrees() - 45, 5, rs);
				rs = cs[Random.choose(0, cs.length - 1)];
				spawnParticle(getDir().getDegrees() + 45, 5, rs);
				break;
			case BEAM:
				CustomColors rbe = cs[Random.choose(0, cs.length - 1)];
				spawnParticle(getDir().getDegrees() - 180, 5, rbe);
				break;
			case BLADE:
				CustomColors rbl = cs[Random.choose(0, cs.length - 1)];
				spawnParticle(getDir().getDegrees(), 0, rbl);
				break;
			default:
				System.out.println("The particle type of " + registeredAttack.getParticleType() + "is not found for Projectile.java");
			}
		}
	}
    
    @Override
	public void draw(Graphics g){
		if(registeredAttack.getParticleType() == ParticleType.NONE || Master.DISABLEPARTICLES){
            int r = getRadius();
			g.setColor(user.getTeam().getColor());
			g.fillOval(getX() - r, getY() - r, 2 * r, 2 * r);
		}
	}
}
