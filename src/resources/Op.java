package resources;
import static java.lang.System.out;
import java.util.ArrayList;

public class Op {
	/**
	 * Op stands for Output, and is used solely for debugging
	 * purposes. It is a static class.
	 */
	private static ArrayList<String> messages;
	
	// Adds to the message arraylist
	public static void add(String msg){
		try {
			messages.add(msg);
		} catch(NullPointerException e){
			messages = new ArrayList<String>();
			add(msg);
		}
	}
	
	public static void add(int msg){
		add(Integer.toString(msg));
	}
	
	public static void add(double msg){
		add(Double.toString(msg));
	}
	public static void add(boolean msg){
		String s = (msg) ? "true" : "false";
		add(s);
	}
	
	// Prints the contents of the message arraylist
	public static void dp(){
		add(" ");
		out.println("<**DEBUG**>");
		for(String msg : messages){
			out.println(msg);
		}
		
		messages = new ArrayList<String>();
	}
}
