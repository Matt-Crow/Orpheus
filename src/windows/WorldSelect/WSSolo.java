package windows.WorldSelect;

import battle.Battle;
import battle.Team;
import controllers.Master;
import controllers.World;
import customizables.Build;
import graphics.Map;
import graphics.Tile;
import gui.OptionBox;
import gui.Style;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import javax.swing.*;
import windows.Page;
import windows.SubPage;

/**
 *
 * @author Matt Crow
 */
public class WSSolo extends SubPage{
    private final OptionBox<String> playerBuild;
    private final OptionBox<String> teamSize;
    private final JButton battle;
    
    public WSSolo(Page p){
        super(p);
        
        setLayout(new GridLayout(2, 2));
        
        playerBuild = buildSelect();
        add(playerBuild);
        
        teamSize = teamSizeSelect();
        add(teamSize);
        
        battle = startBattleButton();
        add(battle);
        
        Style.applyStyling(this);
    }
    
    private OptionBox<String> buildSelect(){
        String[] buildNames = Arrays
            .stream(Build.getAll())
            .map((Build b)->{
                return b.getName();
            })
            .toArray(size -> new String[size]);
        OptionBox<String> buiSel = new OptionBox<>("Select your build", buildNames);
        buiSel.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        return buiSel;
    }
    
    private OptionBox<String> teamSizeSelect(){
        String[] nums = Arrays
            .stream(new Integer[]{1, 2, 3, 4, 5, 10, 99})
            .map((Integer i)->{
                return Integer.toString(i);
            })
            .toArray(size -> new String[size]);
        
        OptionBox<String> box = new OptionBox<>("Select team sizes", nums);
        box.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        return box;
    }
    
    private JButton startBattleButton(){
        JButton ret = new JButton("Battle");
        Style.applyStyling(ret);
        ret.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                startBattle();
            }
        });
        return ret;
    }
    
    private void startBattle(){
        Team team1 = Team.constructRandomTeam("Team 1", Color.green, Integer.parseInt(teamSize.getSelected()) - 1);
        Team team2 = Team.constructRandomTeam("Team 2", Color.red, Integer.parseInt(teamSize.getSelected()));

        Master.TRUEPLAYER.applyBuild(Build.getBuildByName(playerBuild.getSelected()));
        team1.addMember(Master.TRUEPLAYER);

        World battleWorld = new World(20);
        //it's like a theme park or something
        battleWorld.createCanvas();
        
        Map m = new Map(20, 20);
        Tile block = new Tile(0, 0, Color.red);
        block.setBlocking(true);
        m
            .addToTileSet(0, new Tile(0, 0, Color.BLUE))
            .addToTileSet(1, block);
        m
            .setTile(8, 10, 1)
            .setTile(8, 11, 1)
            .setTile(8, 12, 1)
            .setTile(7, 12, 1)
            .setTile(7, 13, 1)
            .setTile(7, 14, 1)
            .setTile(8, 14, 1)
            .setTile(9, 14, 1)
            .setTile(10, 14, 1)
            .setTile(10, 13, 1)
            .setTile(10, 10, 1)
            .setTile(10, 11, 1)
            .setTile(10, 12, 1);
        battleWorld.setMap(m);
        
        battleWorld
            .addTeam(team1)
            .addTeam(team2);
        
        Battle b = new Battle(
            battleWorld.getCanvas(),
            team1,
            team2
        );
        battleWorld.setCurrentMinigame(b);
        b.setHost(battleWorld);
        
        battleWorld.init();
        
        //can change this to switchToPage once world canvas is a Page
        JFrame parent = (JFrame)SwingUtilities.getWindowAncestor(this);
        parent.setContentPane(battleWorld.getCanvas());
        parent.revalidate();
        battleWorld.getCanvas().requestFocus();
    }
}
