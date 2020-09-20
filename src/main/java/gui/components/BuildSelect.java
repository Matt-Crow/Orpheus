package gui.components;

import util.Settings;
import world.customizables.Build;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 *
 * @author Matt
 */
public class BuildSelect extends JComponent{
    private final JComboBox<String> box;
    private final JTextArea desc;
    public BuildSelect() {
        super();
        Style.applyStyling(this);
        setLayout(new BorderLayout());
        
        JLabel title = new JLabel("Select Build");
        Style.applyStyling(title);
        add(title, BorderLayout.PAGE_START);
        
        desc = new JTextArea();
        desc.setEditable(false);
        desc.setWrapStyleWord(true);
        desc.setLineWrap(true);
        desc.setTabSize(4);
        
        JScrollPane scrolly = new JScrollPane(desc);
        scrolly.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrolly.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrolly, BorderLayout.CENTER);
        
        box = new JComboBox<>();
        Style.applyStyling(box);
        add(box, BorderLayout.PAGE_END);
        
        box.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Build b = getSelectedBuild();
                if(b != null){
                    desc.setText(b.getDescription());
                }
                SwingUtilities.invokeLater(()->{
                    scrolly.getVerticalScrollBar().setValue(0);
                });
                
                repaint();
            }
        });
        
        refreshOptions();
    }
    
    public void refreshOptions(){
        box.removeAllItems();
        for(Build b : Settings.getDataSet().getAllBuilds()){
            box.addItem(b.getName());
        }
    }
    
    public Build getSelectedBuild(){
        String name = (String)box.getSelectedItem();
        Build ret = null;
        if(name != null){
            ret = Settings.getDataSet().getBuildByName(name);
        }
        return ret;
    }
}