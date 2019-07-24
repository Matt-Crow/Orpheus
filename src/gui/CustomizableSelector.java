package gui;

import javax.swing.JComponent;
import javax.swing.*;
import customizables.AbstractCustomizable;
import java.awt.BorderLayout;
import java.util.HashMap;

public class CustomizableSelector extends JComponent{
    private final JTextArea desc;
    private final JComboBox chooser;
    private final HashMap<String, AbstractCustomizable> options;
	
	public CustomizableSelector(String title, AbstractCustomizable[] a){
		super();
        options = new HashMap<>();
        
		setLayout(new BorderLayout());
		
		JLabel head = new JLabel(title);
        Style.applyStyling(head);
        add(head, BorderLayout.PAGE_START);
        
		desc = new JTextArea();
        desc.setEditable(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        Style.applyStyling(desc);
        JScrollPane scrolly = new JScrollPane(desc);
        scrolly.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrolly.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrolly, BorderLayout.CENTER);
        Style.applyStyling(scrolly);
        
        chooser = new JComboBox<>();
        chooser.addActionListener((e)->{
            if(getSelected() != null){
                desc.setText(getSelected().getDescription());
                SwingUtilities.invokeLater(()->{
                    scrolly.getVerticalScrollBar().setValue(0);
                });
            }
        });
        Style.applyStyling(chooser);
        add(chooser, BorderLayout.PAGE_END);
        
		Style.applyStyling(this);
	}
    public void addOption(AbstractCustomizable ac){
        if(options.containsKey(ac.getName())){
            throw new IllegalArgumentException(ac.getName() + " is already an option.");
        }
        options.put(ac.getName(), ac);
        chooser.addItem(ac.getName());
    }
    public void setOptions(AbstractCustomizable[] acs){
        chooser.removeAllItems();
        options.clear();
        for(AbstractCustomizable ac : acs){
            addOption(ac);
        }
    }
    public void setSelected(AbstractCustomizable ac){
        /*
        if(!options.containsValue(ac)){
            throw new IllegalArgumentException(ac.getName() + " is not a valid option");
        }*/
        chooser.setSelectedItem(ac.getName());
    }
    public AbstractCustomizable getSelected(){
        AbstractCustomizable ret = null;
        if(options.containsKey((String)chooser.getSelectedItem())){
            ret = options.get((String)chooser.getSelectedItem());
        }
        return ret;
    }
}
