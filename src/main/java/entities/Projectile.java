package entities;

import java.awt.Graphics;

import graphics.CustomColors;
import customizables.actives.AbstractActive;
import customizables.actives.ActiveStatName;
import customizables.actives.ActiveTag;
import controllers.Master;
import util.CombatLog;
import util.Random;

public class Projectile extends AbstractReactiveEntity{
	private final AbstractPlayer user;
	private final AbstractActive registeredAttack;
	private int distanceTraveled;
	private int range;
	private AbstractPlayer hit;
	
	private final int useId; //used to prevent double hitting. May not be unique to a single projectile. See AbstractActive for more info
    
	public Projectile(int useId, int x, int y, int degrees, int momentum, AbstractPlayer attackUser, AbstractActive a){
		super();
        setSpeed(momentum);
        doInit();
        setX(x);
        setY(y);
        setFacing(degrees);
		this.useId = useId;
		distanceTraveled = 0;
		user = attackUser;
		setTeam(user.getTeam());
		registeredAttack = a;
		range = a.getRange();
		setRadius(25);
		setMoving(true);
		hit = null;
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
	public AbstractPlayer getUser(){
		return user;
	}
	public AbstractPlayer getHit(){
		return hit;
	}
	public int getDistance(){
		return distanceTraveled;
	}
	public AbstractActive getAttack(){
		return registeredAttack;
	}
	
	public void hit(AbstractPlayer p){
		hit = p;
        registeredAttack.hit(p);
		p.wasHitBy(this);
		getActionRegister().triggerOnHit(p);
        
        //can't move this to AbstractActive
        if(registeredAttack.containsTag(ActiveTag.KNOCKSBACK)){
            p.knockBack(range, getDir(), Master.seconds(1));
        }
        
		CombatLog.logProjectileData(this);
		terminate();
	}
    
	public boolean checkForCollisions(AbstractPlayer p){
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
		p.setX(getX());
        p.setY(getY());
        p.setFacing(degrees);
		p.setTeam(getTeam());
        getWorld().addParticle(p);
	}
    
    /**
     * Needs to be kept separate from update,
     * as update is not invoked by clients.
     * Update automatically calls this method.
     */
    public void spawnParticles(){
        CustomColors[] cs = registeredAttack.getColors();
		
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
            case NONE:
                break;
			default:
				System.out.println("The particle type of " + registeredAttack.getParticleType() + " is not found for Projectile.java");
			}
		}
    }
    
	@Override
    public void init() {
        super.init();
    }
    
    @Override
	public void update(){
        super.update();
        distanceTraveled += getMomentum();
		
		// need to change range based on projectile type: attack range for seed, aoe for aoeprojectile
		if(distanceTraveled >= range && !getShouldTerminate()){
			terminate();
		}
        spawnParticles();
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
