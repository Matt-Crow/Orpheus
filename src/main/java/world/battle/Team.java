package world.battle;

import world.WorldContent;
import java.awt.Color;
import java.util.ArrayList;
import world.entities.AbstractPlayer;
import util.Coordinates;
import world.entities.AIPlayer;
import world.entities.AbstractEntity;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.function.Consumer;
import static java.lang.System.out;
import java.util.HashMap;
import users.LocalUser;
import util.SafeList;

/**
 * The Team class is used to keep track of
 * which Players are allies, and which are enemies.
 * It keeps track of which team members are still in the game,
 * and reports itself as defeated once it has no members remaining.
 * 
 * @author Matt Crow
 */
public class Team extends SafeList<AbstractEntity> implements Serializable{
	private final String name;
	private final Color color;
	private Team enemyTeam;
	private final String id;
	private static int nextId = 0;
    
    private final HashMap<String, AbstractPlayer> roster;
    private final ArrayList<AbstractPlayer> membersRem;
	
	public Team(String n, Color c){
		super();
        name = n;
		color = c;
		id = "#" + nextId;
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
	
    /**
     * Registers a player as a part of this team.
     * This method is used outside of battle,
     * as it does not add the player to the list
     * of members remaining.
     * 
     * @param m 
     */
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
    
	public static Team constructRandomTeam(WorldContent inWorld, String name, Color color, int size, int lv){
		Team t = new Team(name, color);
		for(int teamSize = 0; teamSize < size; teamSize++){
			AbstractPlayer p = new AIPlayer(
                inWorld,
                String.format("%s member #%d", name, (teamSize + 1)), 
                lv
            );
			t.addMember(p);
		}
		return t;
	}
    
	public void init(WorldContent w){
        membersRem.clear();
        clear();
        roster.values().forEach((p) -> {
            initPlayer(p, w);
        });
	}
    
    /**
     * Initializes a player into the team.
     * This method can be invoked while in battle,
     * so the player will be added to the given world
     * and fully initialized.
     * 
     * @param p
     * @param w 
     */
    public void initPlayer(AbstractPlayer p, WorldContent w){
       w.spawnIntoWorld(p);
       p.init();
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
		return membersRem.isEmpty();
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
       forEach((AbstractEntity e)->{
           e.doUpdate();
       });
   }
   
   public void draw(Graphics g){
       forEach((AbstractEntity e)->e.draw(g));
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
    }
    
    @Override
    public void displayData(){
        out.println("TEAM: " + name + "(ID: " + id + ")");
        out.println("Roster: ");
        roster.values().stream().forEach((AbstractPlayer p)->{
            if(p.id.equals(LocalUser.getInstance().getRemotePlayerId())){
                out.print("*");
            }
            out.println(p.getName() + "(ID: " + p.id + ")");
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
