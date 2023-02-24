package world;

import java.awt.Graphics;
import java.io.Serializable;
import world.battle.Team;
import world.entities.Projectile;
import world.game.Game;

/**
 * This is the part of a complete World that can be serialized and sent through
 * a network connection.
 * 
 * Currently in the process of reworking WorldContent into this class
 * 
 * @author Matt Crow
 */
public class SerializableWorldPart implements WorldContent, Serializable {
    private final Map map;
    private final Team players;
    private final Team ai;
    private final Game game;
    
    public SerializableWorldPart(Map map, Team players, Team ai, Game game){
        this.map = map;
        this.players = players;
        this.ai = ai;
        this.game = game;
    }
    
    @Override
    public Map getMap(){
        return map;
    }
    
    @Override
    public Team getPlayers(){
        return players;
    }
    
    @Override
    public Team getAi(){
        return ai;
    }
    
    @Override
    public Game getGame(){
        return game;
    }
    
    @Override
    public void init(){
        map.init();
        game.play();
    }
    
    @Override
    public void update(){
        players.update();
        ai.update();
        game.update();
        checkForCollisions(players, ai);
        checkForCollisions(ai, players);
    }
    
    private void checkForCollisions(Team t1, Team t2) {
        t1.forEach((e)->{
            map.checkForTileCollisions(e);
            if(e instanceof Projectile){
                t2.getMembersRem().forEach((p)->{
                    ((Projectile) e).checkForCollisions(p);
                });
            }
            
        });
    }
    
    @Override
    public void draw(Graphics g){
        // map.toGraph().draw(g); handled by non-serialized part
        ai.draw(g);
        players.draw(g);
    }

    @Override
    public void setWorld(World w) {
        game.setHost(w);
        players.setWorld(w);
        ai.setWorld(w);
    }
}
