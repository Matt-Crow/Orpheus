package orpheus.client.gui.pages.worldselect;

import orpheus.client.gui.components.ChatBox;
import orpheus.client.gui.components.ShowHideDecorator;
import orpheus.client.gui.components.SpecificationSelector;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Optional;

import javax.swing.JTextArea;

import orpheus.client.ClientAppContext;
import orpheus.client.gui.pages.Page;
import orpheus.client.gui.pages.PageController;
import orpheus.client.gui.pages.start.StartPlay;
import orpheus.client.protocols.WaitingRoomClientProtocol;
import orpheus.core.champions.Specification;

/**
 * WaitingRoom provides a "waiting room"
 * where players can stay while waiting for other
 * players to join.
 * 
 * @author Matt Crow
 */
public class WaitingRoom extends Page {
    private final JTextArea teamList;
    private final ChatBox chat;
    private final SpecificationSelector<Specification> playerBuild;
    private WaitingRoomClientProtocol backend;
    
    public WaitingRoom(ClientAppContext context, PageController host){
        super(context, host);
        var cf = context.getComponentFactory();
        addBackButton(()-> new StartPlay(context, host));
        
        setLayout(new BorderLayout());
        
        add(cf.makeLabel("Waiting for players to join..."), BorderLayout.PAGE_START);
        
        chat = new ChatBox(context);
        add(new ShowHideDecorator(cf, chat), BorderLayout.LINE_START);
        
        playerBuild = new SpecificationSelector<>(
            context.getComponentFactory(),
            context.getDataSet().getAllSpecifications()
        );
        add(playerBuild, BorderLayout.CENTER);
        
        teamList = cf.makeTextArea("Player Team:"); 
        teamList.setColumns(20);
        add(teamList, BorderLayout.LINE_END);
        
        add(cf.makeSpaceAround(cf.makeButton("Start the match", ()->{
            backend.requestStart();
        })), BorderLayout.PAGE_END);
        
        backend = null;
    }
    
    public final ChatBox getChat(){
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
            .map((use) -> "* " + use.getName() + "\n")
            .reduce(newStr, String::concat);
        teamList.setText(newStr);
    }
    
    public Optional<Specification> getSelectedSpecification(){
        return playerBuild.getSelected();
    }
    
    public void setInputEnabled(boolean b){
        playerBuild.setEnabled(b);
    }
    
    public void setBackEnd(WaitingRoomClientProtocol protocol){
        backend = protocol;
    }
}
