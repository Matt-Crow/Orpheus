package world.battle;

import java.awt.Color;
import java.util.ArrayList;
import world.entities.AbstractPlayer;
import world.entities.Projectile;
import world.events.termination.Terminables;
import util.Coordinates;
import world.entities.AIPlayer;
import world.entities.AbstractEntity;
import java.io.Serializable;
import java.util.function.Consumer;

import orpheus.core.world.graph.Graphable;

import java.util.HashMap;
import java.util.LinkedList;

import world.World;

/**
 * The Team class is used to keep track of which Players are allies, and which
 * are enemies. It keeps track of which team members are still in the game, and
 * reports itself as defeated once it has no members remaining.
 *
 * @author Matt Crow
 */
public class Team implements Serializable, Graphable {
    private transient World world;
    private final String name;
    private final Color color;
    private Team enemyTeam;
    private final String id;
    private static int nextId = 0;

    private final HashMap<String, AbstractPlayer> roster;
    private final ArrayList<AbstractPlayer> membersRem;

    /**
     * the entities belonging to this team
     */
    private final Terminables<AbstractEntity> entities = new Terminables<>();

    public Team(String n, Color c) {
        super();
        name = n;
        color = c;
        id = "#" + nextId;
        nextId++;
        roster = new HashMap<>();
        membersRem = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    /**
     * Registers a player as a part of this team. This method is used outside of
     * battle, as it does not add the player to the list of members remaining.
     *
     * @param m
     */
    public void addMember(AbstractPlayer m) {
        roster.put(m.id, m);
        m.setTeam(this);
    }

    /**
     * Returns the AbstractPlayer on this team with the given ID, if one exists.
     * Note that this includes all members in the roster, not just players that
     * are still in play.
     *
     * @param id the ID of the member to search for
     * @return the player on this team with the given id, or null if one doesn't
     * exist
     */
    public AbstractPlayer getMemberById(String id) {
        return roster.get(id);
    }

    public static Team constructRandomTeam(World inWorld, String name, Color color, int size, int lv) {
        Team t = new Team(name, color);
        for (int teamSize = 0; teamSize < size; teamSize++) {
            AbstractPlayer p = new AIPlayer(
                    inWorld,
                    String.format("%s member #%d", name, (teamSize + 1)),
                    lv
            );
            t.addMember(p);
        }
        return t;
    }
    
    public void setWorld(World w){
        world = w;
        entities.forEach((e)->e.setWorld(w));
    }
    
    /**
     * 
     * @param w 
     */
    public void init(World w){
        world = w;
        membersRem.clear();
        clear();
        roster.values().forEach((p)->{ // concurrent modification
            initPlayer(p, w);
        });
    }

    public void add(AbstractEntity e) {
        e.setWorld(world);
        entities.add(e);
    }
    
    public void clear() {
        entities.clear();
    }

    public void forEach(Consumer<AbstractEntity> doThis) {
        entities.forEach(doThis);
    }
    
    /** 
     * Initializes a player into the team. This method can be invoked while in
     * battle, so the player will be added to the given world and fully
     * initialized.
     *
     * @param p the player to initialize
     * @param w the world to spawn the player into
     */
    public void initPlayer(AbstractPlayer p, World w) {
        w.spawn(p);
        p.setWorld(w);
        p.init();
        membersRem.add(p);
        entities.add(p);
    }

    public void setEnemy(Team t) {
        enemyTeam = t;
    }

    public Team getEnemy() {
        return enemyTeam;
    }

    public boolean isDefeated() {
        return membersRem.isEmpty();
    }

    public AbstractPlayer nearestPlayerTo(int x, int y) {
        if (membersRem.isEmpty()) {
            throw new IndexOutOfBoundsException("No players exist for team " + name);
        }
        AbstractPlayer ret = membersRem.get(0);
        int distance = (int) Coordinates.distanceBetween(ret.getX(), ret.getY(), x, y);
        for (AbstractPlayer p : membersRem) {
            if (p.getShouldTerminate()) {
                continue;
            }
            int check = (int) Coordinates.distanceBetween(p.getX(), p.getY(), x, y);
            if (check < distance) {
                ret = p;
                distance = check;
            }
        }
        return ret;
    }

    public final ArrayList<AbstractPlayer> getMembersRem() {
        return membersRem;
    }

    public int getRosterSize() {
        return roster.size();
    }

    /**
     * Invokes a method on each member in the team note that this is EVERY
     * member, NOT just members remaining
     *
     * @param f
     */
    public final void forEachMember(Consumer<AbstractPlayer> f) {
        f.getClass();
        roster
                .values()
                .stream()
                .forEach((AbstractPlayer p) -> f.accept(p));
    }

    public void update() {
        entities.forEach((e) -> e.update());
        entities.update(); // these are separate things
    }

    public void print() {
        entities.forEach((e) -> {
            System.out.println(e.toString());
        });
    }

    /**
     * Notifies this that a member is out of the game. Updates the list of
     * members remaining accordingly. Note that this does not affect iteration,
     * just anything that references membersRem
     *
     * @param p the AbstractPlayer who was removed from the game.
     */
    public void notifyTerminate(AbstractPlayer p) {
        membersRem.remove(p);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Team && ((Team) obj).getId().equals(id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public orpheus.core.world.graph.Team toGraph() {
        var projectiles = new LinkedList<orpheus.core.world.graph.Projectile>();
        entities.forEach((e) -> {
            if (e instanceof Projectile) {
                projectiles.add(((Projectile)e).toGraph());
            }
        });
        
        return new orpheus.core.world.graph.Team(
            membersRem.stream().map((player) -> player.toGraph()).toList(),
            projectiles
        );
    }
}
