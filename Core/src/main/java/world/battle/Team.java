package world.battle;

import java.awt.Color;
import java.util.ArrayList;
import world.entities.AbstractPlayer;
import world.entities.Projectile;
import world.events.termination.Terminables;
import util.Coordinates;

import java.util.function.Consumer;

import controls.ai.PlayerAI;
import orpheus.core.utils.timer.TimerTasks;
import orpheus.core.world.graph.Graphable;
import orpheus.core.world.occupants.WorldOccupant;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import world.World;

/**
 * The Team class is used to keep track of which Players are allies, and which
 * are enemies. It keeps track of which team members are still in the game, and
 * reports itself as defeated once it has no members remaining.
 *
 * @author Matt Crow
 */
public class Team implements Graphable {
    private World world;
    private final String name;
    private final Color color;
    private Team enemyTeam;

    private final HashMap<UUID, AbstractPlayer> roster = new HashMap<>();
    private final ArrayList<AbstractPlayer> membersRem = new ArrayList<>();

    /**
     * the entities belonging to this team
     */
    private final Terminables<WorldOccupant> entities = new Terminables<>();

    /**
     * tasks which this team must run each timer tick
     */
    private final TimerTasks timerTasks = new TimerTasks();


    public Team(String n, Color c) {
        super();
        name = n;
        color = c;
    }

    /**
     * Creates a standard player team.
     * @param players the players to add to the team.
     * @return the team with the given players
     */
    public static Team ofPlayers(AbstractPlayer... players) {
        var result = new Team("Players", Color.GREEN);
        for (var player : players) {
            result.addMember(player);
        }
        return result;
    }

    /**
     * Creates a standard AI team.
     * @param players the players to add to the team.
     * @return the team with the given players
     */
    public static Team ofAi(AbstractPlayer... players) {
        var result = new Team("AI", Color.RED);
        for (var player : players) {
            result.addMember(player);
            result.timerTasks.add(new PlayerAI(player));
        }
        return result;
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
        roster.put(m.getId(), m);
        m.setTeam(this);
    }

    public void addAiMember(AbstractPlayer player) {
        addMember(player);
        timerTasks.add(new PlayerAI(player));
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
    public AbstractPlayer getMemberById(UUID id) {
        return roster.get(id);
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

    public void add(WorldOccupant e) {
        e.setWorld(world);
        entities.add(e);
    }
    
    public void clear() {
        entities.clear();
    }

    public void forEach(Consumer<WorldOccupant> doThis) {
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
            if (p.isTerminating()) {
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

    /**
     * called each world update
     */
    public void update() {
        entities.forEach((e) -> e.update());
        entities.update(); // these are separate things
        timerTasks.tick();
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
