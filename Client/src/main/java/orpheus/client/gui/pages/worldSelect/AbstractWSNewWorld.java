package orpheus.client.gui.pages.worldselect;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import orpheus.client.ClientAppContext;
import orpheus.client.gui.components.BuildSelect;
import orpheus.client.gui.components.OptionBox;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import orpheus.client.gui.pages.Page;
import orpheus.client.gui.pages.PageController;
import world.builds.AssembledBuild;
import world.game.Game;
import world.game.Onslaught;

/**
 *
 * @author Matt Crow
 */
public abstract class AbstractWSNewWorld extends Page{
    private final BuildSelect playerBuild;
    private final OptionBox<Integer> numWaves;
    
    public AbstractWSNewWorld(ClientAppContext context, PageController host){
        super(context, host);
        var cf = context.getComponentFactory();
        addBackButton(()-> new WSMain(context, host));
        
        setLayout(new BorderLayout());
        
        add(cf.makeLabel("New World"), BorderLayout.PAGE_START);
        
        JPanel center = cf.makePanel();
        center.setLayout(new BoxLayout(center, BoxLayout.PAGE_AXIS));
        add(center, BorderLayout.CENTER);
        
        center.add(Box.createRigidArea(new Dimension(20, 20))); // padding
        playerBuild = new BuildSelect(context);
        center.add(playerBuild);
        center.add(Box.createRigidArea(new Dimension(20, 20)));
        numWaves = numWaveSelect();
        center.add(cf.makeSpaceAround(numWaves, Color.GRAY));
        
        add(cf.makeSpaceAround(
                cf.makeButton("Start World", this::start), 
                Color.GRAY
        ), BorderLayout.PAGE_END);
    }
    
    private OptionBox<Integer> numWaveSelect(){
        Integer[] nums = new Integer[]{1, 2, 3, 4, 5, 8};
        
        OptionBox<Integer> box = new OptionBox<>(
            getContext().getComponentFactory(), 
            "Select number of waves", 
            nums
        );
        box.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        box.setSelected(3);
        return box;
    }
    
    public AssembledBuild getSelectedBuild(){
        return playerBuild.getSelectedAssembledBuild();
    }
    
    public Game createGame(){
        return new Onslaught(numWaves.getSelected());
    }
    
    public abstract void start();
}
