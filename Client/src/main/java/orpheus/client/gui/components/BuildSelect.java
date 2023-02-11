package orpheus.client.gui.components;

import world.build.AssembledBuild;
import world.build.Build;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.*;

import orpheus.client.ClientAppContext;

/**
 * A component that allows players to select which build they want to use
 * @author Matt
 */
public class BuildSelect extends JPanel{
    private final ClientAppContext ctx;
    private final JComboBox<String> box;
    private final JTextArea desc;
    
    public BuildSelect(ClientAppContext ctx) {
        super();
        this.ctx = ctx;
        Style.applyStyling(this);
        setLayout(new BorderLayout());
        
        var cf = ctx.getComponentFactory();
        
        add(cf.makeLabel("Select Build"), BorderLayout.PAGE_START);
        
        desc = cf.makeTextArea();
        desc.setColumns(80);
        desc.setTabSize(4);
        
        var scrolly = new JScrollPane(desc);
        scrolly.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrolly.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrolly, BorderLayout.CENTER);
        
        box = new JComboBox<>();
        Style.applyStyling(box);
        add(box, BorderLayout.PAGE_END);
        
        box.addActionListener((ActionEvent e) -> {
            var b = getSelectedAssembledBuild();
            if(b != null){
                desc.setText(b.getDescription());
            }
            SwingUtilities.invokeLater(()->{
                scrolly.getVerticalScrollBar().setValue(0);
            });
            
            repaint();
        });
        
        refreshOptions();
    }
    
    public void refreshOptions(){
        box.removeAllItems();
        for(var b : ctx.getDataSet().getAllBuilds()){
            box.addItem(b.getName());
        }
    }
    
    public Build getSelectedBuild(){
        var name = (String)box.getSelectedItem();
        Build ret = null;
        if(name != null){
            ret = ctx.getDataSet().getBuildByName(name);
        }
        return ret;
    }

    public AssembledBuild getSelectedAssembledBuild() {
        var unassembled = getSelectedBuild();
        return (unassembled == null) 
            ? null 
            : ctx.getDataSet().assemble(unassembled);
    }
}