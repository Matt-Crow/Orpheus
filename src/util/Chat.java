package util;

import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JList;
import java.util.ArrayList;

public class Chat {
	private static ArrayList<String> messages;
	private static JScrollPane box;
	
	private static void init(){
		messages = new ArrayList<String>();
		box = new JScrollPane();
	}
	
	public static void log(String msg){
		messages.add(msg);
		String[] contents = new String[messages.size()];
		for(int i = 0; i < messages.size(); i++){
			contents[i] = messages.get(i);
		}
		JList<String> list = new JList<String>(contents);
		box.setViewportView(list);
	}
	public static void addTo(JPanel j){
		init();
		box.setBounds(0, 0, 100, 100);
		j.add(box);
	}
}
