package gui.pages.worldSelect;

import world.battle.Battle;
import net.protocols.WaitingRoomHostProtocol;

/**
 *
 * @author Matt
 */
public class HostWaitingRoom extends AbstractWaitingRoom{
    public HostWaitingRoom(Battle game){
        super();
        setBackEnd(new WaitingRoomHostProtocol(this, game));
    }

    @Override
    public void startButton() {
        ((WaitingRoomHostProtocol)getBackEnd()).prepareToStart();
        getChat().log("The game will start in " + WaitingRoomHostProtocol.WAIT_TIME + " seconds. Please select your build and team.");
    }
}
