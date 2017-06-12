package windows;

import java.awt.Color;
import javax.swing.JFrame;

import battle.Battle;
import battle.Team;
import entities.Player;

public class BattleWindow extends JFrame{
	public static final long serialVersionUID = 1L;
	Battle battle;
	BattleCanvas draw;
	Team team1;
	Team team2;
	
	public BattleWindow(){
		setTitle("Match");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 1000);
		
		team1 = new Team("Team 1", Color.red);
		team2 = new Team("Team 2", Color.blue);
		
		team1.addMember(new Player("Matt"));
		team1.addMember(new Player("Nick"));
		team1.addMember(new Player("Paul"));
		team1.addMember(new Player("Gianna"));
		team1.addMember(new Player("David"));
		
		team2.addMember(new Player("Grunt #1"));
		team2.addMember(new Player("Grunt #2"));
		team2.addMember(new Player("Grunt #3"));
		team2.addMember(new Player("Grunt #4"));
		team2.addMember(new Player("Grunt #5"));
		
		battle = new Battle(team1, team2);
		
		draw = new BattleCanvas(30, 30, 100);
		setContentPane(draw);
		setVisible(true);
	}
}
