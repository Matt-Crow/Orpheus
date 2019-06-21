package windows.WorldSelect;

import customizables.Build;
import gui.OptionBox;
import gui.Style;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import windows.Page;
import windows.SubPage;

/**
 *
 * @author Matt Crow
 */
public abstract class AbstractWSNewWorld extends SubPage{
    private final OptionBox<String> playerBuild;
    private final OptionBox<Integer> teamSize;
    private final JButton start;
    
    public AbstractWSNewWorld(Page p){
        super(p);
        setLayout(new GridLayout(2, 2));
        
        playerBuild = buildSelect();
        add(playerBuild);
        
        teamSize = teamSizeSelect();
        add(teamSize);
        
        start = startButton();
        add(start);
        
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
        return Build.getBuildByName(playerBuild.getSelected());
    }
    
    public int getTeamSize(){
        return teamSize.getSelected();
    }
    
    public abstract void start();
}