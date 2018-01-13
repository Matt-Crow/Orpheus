package attacks;

import statuses.Rush;

public class SpeedTest extends BoostAttack{
	public SpeedTest(){
		super("Speed Test", 1, 3, new Rush(2, 7));
	}
}
