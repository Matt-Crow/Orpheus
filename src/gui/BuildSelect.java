package gui;

import customizables.Build;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author Matt
 */
public class BuildSelect extends OptionBox<String>{
    
    public BuildSelect() {
        //most rediculous constructor call ever
        super(
            "Select Build", 
            Build
                .getAllBuilds()
                .stream()
                .map((Build b)->{
                    return b.getName();
                })
            .toArray((size)-> new String[size])
        );
        addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
    }
    
    public Build getSelectedBuild(){
        return Build.getBuildByName(getSelected());
    }
    
    
}
