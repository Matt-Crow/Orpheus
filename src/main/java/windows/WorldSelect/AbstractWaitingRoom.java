package windows.WorldSelect;

import controllers.User;
import customizables.Build;
import gui.BuildSelect;
import gui.Chat;
import gui.Style;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import net.protocols.AbstractWaitingRoomProtocol;
import windows.Page;

/**
 * AbstractWaitingRoom provides a "waiting room"
 * where players can stay while waiting for other
 * players to join.
 * 
 * @author Matt Crow
 */
public abstract class AbstractWaitingRoom extends Page{
    private final JTextArea teamList;
    private final Chat chat;
    private final BuildSelect playerBuild;
    private final JButton startButton;
    private AbstractWaitingRoomProtocol backend;
    
    public AbstractWaitingRoom(AbstractWaitingRoomProtocol protocol){
        this();
        setBackEnd(protocol);
    }
    public AbstractWaitingRoom(){
        super();
        
        addBackButton(new WSMain());
        
        //grid layout was causing problems with chat.
        //since it couldn't fit in 1/4 of the JPanel, it compressed to just a thin line
        setLayout(new BorderLayout());
        
        JPanel infoSection = new JPanel();
        infoSection.setLayout(new GridLayout(1, 3));
        
        teamList = new JTextArea("Player Team:");
        Style.applyStyling(teamList);
        infoSection.add(teamList);
        
        add(infoSection, BorderLayout.PAGE_START);
        
        JPanel center = new JPanel();
        center.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        
        gbc.anchor = GridBagConstraints.LINE_START;
        playerBuild = new BuildSelect();
        center.add(playerBuild, gbc.clone());
        
        gbc.anchor = GridBagConstraints.LINE_END;
        chat = new Chat();
        center.add(chat, gbc.clone());
        
        add(center, BorderLayout.CENTER);
        
        startButton = new JButton("Start the match");
        startButton.addActionListener((e)->{
            startButton();
        });
        add(startButton, BorderLayout.PAGE_END);
        
        backend = null;
        
        revalidate();
        repaint();
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
            .map((User use) -> "* " + use.getName() + "\n")
            .reduce(newStr, String::concat);
        teamList.setText(newStr);
    }
    
    public Build getSelectedBuild(){
        return playerBuild.getSelectedBuild();
    }
    
    public void setStartButtonEnabled(boolean b){
        startButton.setEnabled(b);
    }
    
    public void setInputEnabled(boolean b){
        playerBuild.setEnabled(b);
    }
    
    public void setBackEnd(AbstractWaitingRoomProtocol protocol){
        backend = protocol;
    }
    public AbstractWaitingRoomProtocol getBackEnd(){
        return backend;
    }
    
    public abstract void startButton();
}
