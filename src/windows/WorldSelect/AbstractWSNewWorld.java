package windows.WorldSelect;

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
    private final OptionBox<Integer> teamSize;
    private final JButton start;
    
    public AbstractWSNewWorld(){
        super();
        
        addBackButton(new WSMain());
        
        setLayout(new GridLayout(2, 2));
        
        playerBuild = new BuildSelect();
        add(playerBuild);
        
        teamSize = teamSizeSelect();
        add(teamSize);
        
        start = startButton();
        add(start);
        
        Style.applyStyling(this);
    }
    
    private OptionBox<Integer> teamSizeSelect(){
        Integer[] nums = new Integer[]{1, 2, 3, 4, 5, 10, 99};
        
        OptionBox<Integer> box = new OptionBox<>("Select team sizes", nums);
        box.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
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
    
    public int getTeamSize(){
        return teamSize.getSelected();
    }
    
    public abstract void start();
}
