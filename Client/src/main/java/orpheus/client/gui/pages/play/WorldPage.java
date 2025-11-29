package orpheus.client.gui.pages.play;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.UUID;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import orpheus.client.ClientAppContext;
import orpheus.client.gui.components.ChatBox;
import orpheus.client.gui.components.ShowHideDecorator;
import orpheus.client.gui.pages.Page;
import orpheus.client.gui.pages.PageController;
import orpheus.client.gui.pages.PlayerControls;
import orpheus.client.gui.pages.start.StartPlay;
import orpheus.core.commands.executor.Executor;
import orpheus.core.utils.timer.FrameTimer;
import orpheus.core.world.graph.particles.Particles;

/**
 * use the PageController to render the WorldPage,
 * which renders the WorldCanvas,
 * which renders the World
 * which renders both the serialized and non-serialized world parts
 * 
 * @author Matt Crow
 */
public class WorldPage extends Page {
    private final JPanel canvasArea;
    private final FrameTimer updater;
    private WorldCanvas canvas;

    /**
     * the chatbox where the player can enter messages
     */
    private final ChatBox chatBox;
    
    public WorldPage(
        ClientAppContext context, 
        PageController host, 
        WorldGraphSupplier worldGraphSupplier,
        UUID playerId,
        Executor commandExecutor
    ){
        super(context, host);

        var hud = new HeadsUpDisplay(worldGraphSupplier, playerId);
        var particles = new Particles();

        updater = new FrameTimer();
        updater.addEndOfFrameListener(hud::frameEnded);
        updater.addEndOfFrameListener(e -> {
            worldGraphSupplier.get().spawnParticlesInto(particles);
            particles.update();
        });

        var cf = context.getComponentFactory();
        
        addBackButton(()-> new StartPlay(context, host));        
        setLayout(new BorderLayout());
        
        // set up the canvas area at the center of the page
        canvasArea = new JPanel();
        canvasArea.setLayout(new BorderLayout());
        canvasArea.setFocusable(false);
        add(canvasArea, BorderLayout.CENTER);

        canvas = new WorldCanvas(worldGraphSupplier, particles, new PlayerControls(playerId, commandExecutor));
        canvasArea.add(canvas, BorderLayout.CENTER);
        canvasArea.add(hud, BorderLayout.PAGE_END);
        SwingUtilities.invokeLater(canvas::requestFocusInWindow);
        
        //other area
        JPanel otherArea = new JPanel();
        otherArea.setLayout(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        chatBox = new ChatBox(context);
        c2.fill = GridBagConstraints.VERTICAL;
        c2.anchor = GridBagConstraints.FIRST_LINE_START;
        c2.weightx = 1.0;
        c2.weighty = 1.0;
        otherArea.add(chatBox, c2.clone());
        
        JScrollPane scrolly = new JScrollPane(cf.makeTextArea(
            PlayerControls.getPlayerControlScheme()
        ));
        scrolly.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrolly.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        c2.gridx = GridBagConstraints.RELATIVE;
        c2.fill = GridBagConstraints.BOTH;
        otherArea.add(scrolly, c2.clone());
        
        add(
            new ShowHideDecorator(context.getComponentFactory(), otherArea), 
            BorderLayout.PAGE_END
        );
    }

    @Override
    public void enteredPage() {
        updater.start();
        canvas.start();
    }
    
    @Override
    public void leavingPage(){
        updater.stop();
        canvas.stop();
    }

    /**
     * Allows the server to attach itself as a listener to the chat.
     * @return the chatbox shown on the page.
     */
    public ChatBox getChatBox() {
        return chatBox;
    }
}
