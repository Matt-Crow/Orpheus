package windows.WorldSelect;

import customizables.Build;
import gui.OptionBox;
import gui.Style;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import javax.swing.AbstractAction;
import windows.Page;
import windows.SubPage;

/**
 *
 * @author Matt Crow
 */
public class WSSolo extends SubPage{
    private final OptionBox<String> playerBuild;
    private final OptionBox<String> map;
    
    public WSSolo(Page p){
        super(p);
        
        setLayout(new GridLayout(2, 2));
        
        String[] buildNames = Arrays
            .stream(Build.getAll())
            .map((Build b)->{
                return b.getName();
            })
            .toArray(size -> new String[size]);
        playerBuild = new OptionBox<>("Select your build", buildNames);
        playerBuild.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        add(playerBuild);
        
        map = new OptionBox<>("Choose a map", new String[]{"n/a"});
        map.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        
        Style.applyStyling(this);
    }
}
