package resources;
import static java.lang.System.out;
import java.util.ArrayList;

public class Op {
	/**
	 * Op stands for Output, and is used solely for debugging
	 * purposes. It is a static class.
	 */
	private static ArrayList<String> messages;
	private static ArrayList<String> previousMessages;
	
	// Creates the message arraylist
	public static void reset(){
		messages = new ArrayList<String>();
		previousMessages = new ArrayList<String>();
	}
	
	// Adds to the message arraylist
	public static void add(String msg){
		try {
			messages.add(msg);
		} catch(NullPointerException e){
			reset();
			add(msg);
		}
	}
	
	public static void add(int msg){
		add(Integer.toString(msg));
	}
	
	// Prints the contents of the message arraylist
	public static void dp(){
		add(" ");
		if(! previousMessages.equals(messages)){
			out.println("<**DEBUG**>");
			for(String msg : messages){
				out.println(msg);
			}
		}
		previousMessages = messages;
		reset();
	}
}
