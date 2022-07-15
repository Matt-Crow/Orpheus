package gui.pages.worldSelect;

import world.build.Build;
import gui.components.BuildSelect;
import gui.components.OptionBox;
import gui.components.Style;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import gui.pages.Page;
import start.PageController;
import world.game.Game;
import world.game.Onslaught;

/**
 *
 * @author Matt Crow
 */
public abstract class AbstractWSNewWorld extends Page{
    private final BuildSelect playerBuild;
    private final OptionBox<Integer> numWaves;
    private final JButton start;
    
    public AbstractWSNewWorld(PageController host){
        super(host);
        
        addBackButton(new WSMain(host));
        
        setLayout(new GridLayout(2, 2));
        
        playerBuild = new BuildSelect();
        add(playerBuild);
        
        start = startButton();
        add(start);
        
        numWaves = numWaveSelect();
        add(numWaves);
        
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
    
    public Game createGame(){
        return new Onslaught(numWaves.getSelected());
    }
    
    public abstract void start();
}
