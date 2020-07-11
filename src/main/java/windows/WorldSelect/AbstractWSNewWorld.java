package windows.WorldSelect;

import battle.Battle;
import customizables.Build;
import gui.BuildSelect;
import gui.OptionBox;
import gui.Style;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import windows.Page;

/**
 *
 * @author Matt Crow
 */
public abstract class AbstractWSNewWorld extends Page{
    private final BuildSelect playerBuild;
    private final OptionBox<Integer> numWaves;
    private final OptionBox<Integer> maxEnemyLevel;
    private final JButton start;
    
    public AbstractWSNewWorld(){
        super();
        
        addBackButton(new WSMain());
        
        setLayout(new GridLayout(2, 2));
        
        playerBuild = new BuildSelect();
        add(playerBuild);
        
        start = startButton();
        add(start);
        
        numWaves = numWaveSelect();
        add(numWaves);
        
        maxEnemyLevel = enemyLvSelect();
        add(maxEnemyLevel);
        
        Style.applyStyling(this);
    }
    
    private OptionBox<Integer> numWaveSelect(){
        Integer[] nums = new Integer[]{1, 2, 3, 4, 5, 8};
        
        OptionBox<Integer> box = new OptionBox<>("Select number of waves", nums);
        box.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        box.setSelected(3);
        return box;
    }
    
    private OptionBox<Integer> enemyLvSelect(){
        Integer[] nums = new Integer[]{1, 10, 20};
        
        OptionBox<Integer> box = new OptionBox<>("Select maximum enemy level", nums);
        box.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        box.setSelected(10);
        return box;
    }
    
    private JButton startButton(){
        JButton ret = new JButton("Start World");
        Style.applyStyling(ret);
        ret.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });
        return ret;
    }
    
    public Build getSelectedBuild(){
        return playerBuild.getSelectedBuild();
    }
    
    /**
     * Creates a new game based on the values selected in the GUI
     * @return a new Battle. Future versions may support other minigames
     */
    public Battle createBattle(){
        return new Battle(maxEnemyLevel.getSelected(), numWaves.getSelected());
    }
    
    public abstract void start();
}
