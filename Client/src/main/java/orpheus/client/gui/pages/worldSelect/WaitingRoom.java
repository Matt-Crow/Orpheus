package orpheus.client.gui.pages.worldSelect;

import world.build.Build;
import orpheus.client.gui.components.BuildSelect;
import orpheus.client.gui.components.Chat;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Arrays;
import javax.swing.JPanel;
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
        
        //grid layout was causing problems with chat.
        //since it couldn't fit in 1/4 of the JPanel, it compressed to just a thin line
        setLayout(new BorderLayout());
        
        JPanel infoSection = new JPanel();
        infoSection.setLayout(new GridLayout(1, 3));
        
        teamList = cf.makeTextArea("Player Team:");
        infoSection.add(teamList);
        
        add(infoSection, BorderLayout.PAGE_START);
        
        JPanel center = new JPanel();
        center.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        
        gbc.anchor = GridBagConstraints.LINE_START;
        playerBuild = new BuildSelect(cf);
        center.add(playerBuild, gbc.clone());
        
        gbc.anchor = GridBagConstraints.LINE_END;
        chat = new Chat(cf, null);
        center.add(chat, gbc.clone());
        
        add(center, BorderLayout.CENTER);
        
        add(cf.makeButton("Start the match", ()->{
            backend.requestStart();
        }), BorderLayout.PAGE_END);
        
        backend = null;
        
        revalidate();
        repaint();
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
