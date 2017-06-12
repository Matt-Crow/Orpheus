package battle;

import java.util.ArrayList;


public class Battle {
	ArrayList<Team> teams;
	
	public Battle(Team team1, Team team2){
		teams = new ArrayList<>();
		teams.add(team1);
		teams.add(team2);
	}
}
