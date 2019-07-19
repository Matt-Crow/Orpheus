package gui;

import javax.swing.JComponent;
import java.awt.GridLayout;

import java.awt.event.ActionEvent;
import javax.swing.*;
import customizables.AbstractCustomizable;

@SuppressWarnings("serial")
public class CustomizableSelector extends JComponent{
	private OptionBox<AbstractCustomizable> box;
	private JTextArea desc;
	
	public CustomizableSelector(String title, AbstractCustomizable[] a){
		super();
		setLayout(new GridLayout(2, 1));
		
		
		desc = new JTextArea();
        desc.setEditable(false);
        desc.setLineWrap(true);
        desc.setWrapStyleWord(true);
        
        
        JScrollPane scrolly = new JScrollPane(desc);
        scrolly.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrolly.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrolly);
        
        Style.applyStyling(desc);
        
        box = new OptionBox<>(title, a);
		box.addActionListener(new AbstractAction(){
			public void actionPerformed(ActionEvent e){
                if(box.getSelected() != null){
                    desc.setText(box.getSelected().getDescription());
                    SwingUtilities.invokeLater(()->{
                        scrolly.getVerticalScrollBar().setValue(0);
                    });
                }
            }
		});
		add(box);
        
		Style.applyStyling(this);
	}
    public void setOptions(AbstractCustomizable[] acs){
        box.clear();
        for(AbstractCustomizable ac : acs){
            box.addOption(ac);
        }
    }
	public OptionBox<AbstractCustomizable> getBox(){
		return box;
	}
    
    public AbstractCustomizable getSelected(){
        return box.getSelected();
    }
}
