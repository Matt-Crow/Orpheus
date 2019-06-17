package windows.WorldSelect;

import entities.Player;
import java.util.ArrayList;
import util.Chat;
import windows.Page;
import windows.SubPage;

/**
 *
 * @author Matt
 */
public class WSWaitForPlayers extends SubPage{
    private int teamSize;
    private ArrayList<Player> team1Players;
    private ArrayList<Player> team2Players;
    
    public WSWaitForPlayers(Page p){
        super(p);
        teamSize = 1;
        team1Players = new ArrayList<>();
        team2Players = new ArrayList<>();
        Chat.addTo(this);
    }
    
    public WSWaitForPlayers setTeamSize(int s){
        teamSize = s;
        return this;
    }
    
    public WSWaitForPlayers joinTeam1(Player p){
        if(team2Players.contains(p)){
            team2Players.remove(p);
            Chat.log(p.getName() + " has left team 2.");
        }
        if(team1Players.contains(p)){
            Chat.log(p.getName() + " is already on team 1.");
        }else if(team1Players.size() < teamSize){
            team1Players.add(p);
            Chat.log(p.getName() + " has joined team 1.");
        }else{
            Chat.log(p.getName() + " cannot joint team 1: Team 1 is full.");
        } 
        return this;
    }
    
    public WSWaitForPlayers joinTeam2(Player p){
        if(team1Players.contains(p)){
            team1Players.remove(p);
            Chat.log(p.getName() + " has left team 1.");
        }
        if(team2Players.contains(p)){
            Chat.log(p.getName() + " is already on team 2.");
        }else if(team2Players.size() < teamSize){
            team2Players.add(p);
            Chat.log(p.getName() + " has joined team 2.");
        }else{
            Chat.log(p.getName() + " cannot joint team 2: Team 2 is full.");
        } 
        return this;
    }
}
