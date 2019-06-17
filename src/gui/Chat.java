package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JList;
import java.util.ArrayList;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Chat {
	private static ArrayList<String> messages;
    private static final JPanel content;
	private static final JTextArea msgs;
    private static final JScrollPane box;
    private static final JTextField newMsg;
	
    static{
        content = new JPanel();
        content.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        
        msgs = new JTextArea("###CHAT###");
        msgs.setEditable(false);
        box = new JScrollPane(msgs);
        gbc.weighty = 4;
        gbc.fill = GridBagConstraints.BOTH;
        content.add(box, gbc.clone());
        
        newMsg = new JTextField();
        newMsg.setMaximumSize(new Dimension(Integer.MAX_VALUE, newMsg.getPreferredSize().height));
        newMsg.addActionListener((e)->{
            log(newMsg.getText());
            newMsg.setText("");
        });
        gbc.weighty = 1;
        gbc.gridy = 1;
        content.add(newMsg, gbc.clone());
        
        content.revalidate();
        content.repaint();
    }
    
	private static void init(){
		messages = new ArrayList<String>();
	}
	
	public static void log(String msg){
		messages.add(msg);
		String[] contents = new String[messages.size()];
		for(int i = 0; i < messages.size(); i++){
			contents[i] = messages.get(i);
		}
		JList<String> list = new JList<String>(contents);
		//box.setViewportView(list);
        msgs.setText(msgs.getText() + '\n' + msg);
	}
	public static void addTo(JPanel j){
		init();
		//box.setBounds(0, 0, 100, 100);
		//j.add(box);
        j.add(content);
	}
}
