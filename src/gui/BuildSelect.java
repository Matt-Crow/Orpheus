package gui;

import customizables.Build;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

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
        Build.getAllBuilds().stream().forEach((b)->{
            box.addItem(b.getName());
        });
        Style.applyStyling(box);
        add(box, BorderLayout.PAGE_END);
        
        box.addActionListener(new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e) {
                desc.setText(getSelectedBuild().getDescription());
                repaint();
            }
        });
    }
    
    public Build getSelectedBuild(){
        return Build.getBuildByName((String)box.getSelectedItem());
    }
}
