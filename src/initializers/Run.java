package initializers;
import entities.Player;
import windows.StartWindow;

public class Run {
	public static Player player;
	public static void main(String[] args) {
		new StartWindow();
		player = new Player();
	}
}
