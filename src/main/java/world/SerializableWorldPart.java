package world;

import gui.graphics.Map;
import java.awt.Graphics;
import java.io.Serializable;
import world.battle.Battle;
import world.battle.Team;

/**
 * This is the part of a complete World that can be serialized and sent through
 * a network connection.
 * 
 * @author Matt Crow
 */
public class SerializableWorldPart implements Serializable {
    private final Map map;
    private final Team players;
    private final Team ai;
    private final Battle game;
    
    public SerializableWorldPart(Map map, Team players, Team ai, Battle game){
        this.map = map;
        this.players = players;
        this.ai = ai;
        this.game = game;
    }
    
    public Map getMap(){
        return map;
    }
    
    public Team getPlayers(){
        return players;
    }
    
    public Team getAi(){
        return ai;
    }
    
    public Battle getGame(){
        return game;
    }
    
    protected void init(){
        map.init();
        //players.init(w); not supported yet
        //ai.init(w); not supported yet
        game.init();
    }
    
    protected void update(){
        players.update();
        ai.update();
        game.update();
    }
    
    public void draw(Graphics g){
        map.draw(g);
        ai.draw(g);
        players.draw(g);
    }
}
