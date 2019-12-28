package battle;

import controllers.Master;
import controllers.World;
import java.awt.Color;
import java.util.ArrayList;
import entities.AbstractPlayer;
import util.Coordinates;
import entities.AIPlayer;
import entities.Entity;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.function.Consumer;
import static java.lang.System.out;
import java.util.HashMap;
import util.SafeList;

/**
 * The Team class is used to keep track of
 * which Players are allies, and which are enemies.
 * It keeps track of which team members are still in the game,
 * and reports itself as defeated once it has no members remaining.
 * 
 * @author Matt Crow
 */
public class Team extends SafeList<Entity> implements Serializable{
	private final String name;
	private final Color color;
	private Team enemyTeam;
	private boolean defeated;
	private final String id;
	private static int nextId = 0;
    
    private final HashMap<String, AbstractPlayer> roster;
    private final ArrayList<AbstractPlayer> membersRem;
	
	public Team(String n, Color c){
		super();
        name = n;
		color = c;
		id = Master.SERVER.getIpAddr() + ": " + nextId;
		nextId++;
        roster = new HashMap<>();
        membersRem = new ArrayList<>();
	}
    
	public String getId(){
		return id;
	}
	public String getName(){
		return name;
	}
	
	public Color getColor(){
		return color;
	}
	
	public void addMember(AbstractPlayer m){
        roster.put(m.id, m);
        m.setTeam(this);
	}
    
    /**
     * Returns the AbstractPlayer on this team
     * with the given ID, if one exists.
     * Note that this includes all members
     * in the roster, not just players that
     * are still in play.
     * 
     * @param id the ID of the member to search for
     * @return the player on this team with the given id, or null if one doesn't exist
     */
    public AbstractPlayer getMemberById(String id){
        return roster.get(id);
    }
    
	public static Team constructRandomTeam(String name, Color color, int size, int lv){
		Team t = new Team(name, color);
		for(int teamSize = 0; teamSize < size; teamSize++){
			AbstractPlayer p = new AIPlayer(name + " member #" + (teamSize + 1), lv);
			t.addMember(p);
		}
		return t;
	}
    
	public void init(World w){
        membersRem.clear();
        clear();
		for(AbstractPlayer p : roster.values()){
			initPlayer(p, w);
		}
		defeated = false;
	}
    public void initPlayer(AbstractPlayer p, World w){
       w.spawnIntoWorld(p);
       p.doInit();
       membersRem.add(p);
       add(p);
    }
    
	public void setEnemy(Team t){
		enemyTeam = t;
	}
	public Team getEnemy(){
		return enemyTeam;
	}
	
	public boolean isDefeated(){
		return defeated;
	}
	
    public AbstractPlayer nearestPlayerTo(int x, int y){
		if(membersRem.isEmpty()){
			throw new IndexOutOfBoundsException("No players exist for team " + name);
		}
        AbstractPlayer ret = membersRem.get(0);
		int distance = (int)Coordinates.distanceBetween(ret.getX(), ret.getY(), x, y);
		for(AbstractPlayer p : membersRem){
            if(p.getShouldTerminate()){
                continue;
            }
			int check = (int)Coordinates.distanceBetween(p.getX(), p.getY(), x, y);
			if(check < distance){
				ret = p;
				distance = check;
			}
		}
		return ret;
	}
	public final ArrayList<AbstractPlayer> getMembersRem(){
		return membersRem;
	}
    
    public int getRosterSize(){
        return roster.size();
    }
    
    /**
     * Invokes a method on each member in the team
     * note that this is EVERY member,
     * NOT just members remaining
     * @param f 
     */
    public final void forEachMember(Consumer<AbstractPlayer> f){
        f.getClass();
        roster
            .values()
            .stream()
            .forEach((AbstractPlayer p)->f.accept(p));
    }
    
    public void update(){
       forEach((Entity e)->{
           e.doUpdate();
       });
   }
   
   public void draw(Graphics g){
       forEach((Entity e)->e.draw(g));
   }
   
   public void print(){
       forEach((e)->{
           System.out.println(e.toString());
       });
   }
    
    /**
     * Notifies this that a member is out of the game.
     * Updates the list of members remaining accordingly.
     * Note that this does not affect iteration,
     * just anything that references membersRem
     * @param p the AbstractPlayer who was removed from the game.
     */
    public void notifyTerminate(AbstractPlayer p){
        membersRem.remove(p);
        defeated = membersRem.isEmpty();
    }
    
    @Override
    public void displayData(){
        out.println("TEAM: " + name + "(ID: " + id + ")");
        out.println("Roster: ");
        roster.values().stream().forEach((AbstractPlayer p)->{
            if(p.equals(Master.getUser().getPlayer())){
                out.print("*");
            }
            out.println(p.getName() + "(ID: " + p.id + ")");
        });
    }
    public void detailedDisplayData(){
        out.println("TEAM: " + name + "(ID: " + id + ")");
        roster.values().stream().forEach((AbstractPlayer p)->{
            p.detailedDisplayData();
        });
    }
    
    @Override
    public boolean equals(Object obj){
        return obj != null && obj instanceof Team && ((Team)obj).getId().equals(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
