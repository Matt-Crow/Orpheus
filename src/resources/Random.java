package resources;

import java.util.concurrent.ThreadLocalRandom;

public class Random {
	public static int choose(int min, int max){
		int n = ThreadLocalRandom.current().nextInt(min, max + 1);
		return n;
	}
	public static boolean chance(int perc){
		int n = choose(0, 100);
		return n <= perc;
	}
}
