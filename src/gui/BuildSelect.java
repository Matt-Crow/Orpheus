package gui;

import customizables.Build;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        JLabel title = new JLabel("Select Build");
        Style.applyStyling(title);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(title, gbc.clone());
        
        desc = new JTextArea();
        desc.setEditable(false);
        desc.setWrapStyleWord(true);
        desc.setLineWrap(true);
        
        JScrollPane scrolly = new JScrollPane(desc);
        scrolly.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrolly.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.BASELINE_LEADING;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(scrolly, gbc.clone());
        
        box = new JComboBox<>();
        Build.getAllBuilds().stream().forEach((b)->{
            box.addItem(b.getName());
        });
        Style.applyStyling(box);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.LAST_LINE_START;
        add(box, gbc.clone());
        
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
