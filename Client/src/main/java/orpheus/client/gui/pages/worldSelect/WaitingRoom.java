package orpheus.client.gui.pages.worldSelect;

import world.build.Build;
import orpheus.client.gui.components.BuildSelect;
import orpheus.client.gui.components.Chat;
import java.awt.BorderLayout;
import java.util.Arrays;
import javax.swing.JTextArea;
import orpheus.client.WaitingRoomClientProtocol;
import orpheus.client.gui.components.ComponentFactory;
import users.AbstractUser;
import orpheus.client.gui.pages.Page;
import orpheus.client.gui.pages.PageController;

/**
 * WaitingRoom provides a "waiting room"
 * where players can stay while waiting for other
 * players to join.
 * 
 * @author Matt Crow
 */
public class WaitingRoom extends Page{
    private final JTextArea teamList;
    private final Chat chat;
    private final BuildSelect playerBuild;
    private WaitingRoomClientProtocol backend;
    
    public WaitingRoom(PageController host, ComponentFactory cf){
        super(host, cf);
        
        addBackButton(new WSMain(host, cf));
        
        setLayout(new BorderLayout());
        
        add(cf.makeLabel("Waiting for players to join..."), BorderLayout.PAGE_START);
        
        chat = new Chat(cf, null);
        add(chat, BorderLayout.LINE_START);
        
        playerBuild = new BuildSelect(cf);
        add(playerBuild, BorderLayout.CENTER);
        
        teamList = cf.makeTextArea("Player Team:"); 
        teamList.setColumns(20);
        add(teamList, BorderLayout.LINE_END);
        
        add(cf.makeSpaceAround(cf.makeButton("Start the match", ()->{
            backend.requestStart();
        })), BorderLayout.PAGE_END);
        
        backend = null;
    }
    
    public WaitingRoom(PageController host, ComponentFactory cf, WaitingRoomClientProtocol protocol){
        this(host, cf);
        setBackEnd(protocol);
    }
    
    public final Chat getChat(){
        return chat;
    }
    
    /**
     * Updates the text of this' team displays
     * to match the proto-teams of the backend.
     */
    public void updateTeamDisplays(){
        String newStr = "Player Team: \n";
        newStr = Arrays
            .stream(backend.getTeamProto())
            .map((AbstractUser use) -> "* " + use.getName() + "\n")
            .reduce(newStr, String::concat);
        teamList.setText(newStr);
    }
    
    public Build getSelectedBuild(){
        return playerBuild.getSelectedBuild();
    }
    
    public void setInputEnabled(boolean b){
        playerBuild.setEnabled(b);
    }
    
    public void setBackEnd(WaitingRoomClientProtocol protocol){
        backend = protocol;
    }
    public WaitingRoomClientProtocol getBackEnd(){
        return backend;
    }
}
