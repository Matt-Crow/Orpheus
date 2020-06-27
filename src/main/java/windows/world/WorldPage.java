package windows.world;

import controllers.Master;
import gui.Chat;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import windows.Page;
import windows.WorldSelect.WSMain;
import world.HostWorld;
import world.RemoteProxyWorld;

/**
 * The WorldPage is used to render WorldCanvases.
 * So yes, it is rather convoluted:
 use the MainWindow to render the WorldPage,
 which renders the WorldPage,
 which renders the WorldCanvas,
 which renders the AbstractWorld.
 * 
 * @author Matt Crow
 */
public class WorldPage extends Page{
    private final JPanel canvasArea;
    private WorldCanvas canvas;
    private final Chat chat;
    
    public WorldPage(){
        super();
        addBackButton(new WSMain(), ()->{
            if(Master.SERVER.isStarted()){
                Master.SERVER.shutDown();
            }
            if(canvas != null){
                canvas.stop();
            }
        });
        
        setLayout(new BorderLayout());
        
        canvasArea = new JPanel();
        canvasArea.setBackground(Color.red);
        canvasArea.setFocusable(false);
        canvasArea.setLayout(new GridLayout(1, 1));
        add(canvasArea, BorderLayout.CENTER);
        
        //other area
        //make this collapsable
        JPanel otherArea = new JPanel();
        otherArea.setLayout(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        chat = new Chat();
        c2.fill = GridBagConstraints.VERTICAL;
        c2.anchor = GridBagConstraints.FIRST_LINE_START;
        c2.weightx = 1.0;
        c2.weighty = 1.0;
        otherArea.add(chat, c2.clone());
        
        JTextArea controls = new JTextArea(
            "#CONTROLS#\n"+
                "Click on a tile to move there\n"+
                "Hold down the mouse to 'follow' the mouse\n"+
                "(Q): use your melee attack\n"+
                "(1-3): use your attacks 1, 2, or 3\n"+
                "(P): pause / resume (singleplayer only)\n"+
                "(Z): zoom in\n"+
                "(X): zoom out\n"
        );
        controls.setEditable(false);
        controls.setWrapStyleWord(true);
        controls.setLineWrap(true);
        JScrollPane scrolly = new JScrollPane(controls);
        scrolly.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrolly.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        c2.gridx = GridBagConstraints.RELATIVE;
        c2.fill = GridBagConstraints.BOTH;
        otherArea.add(scrolly, c2.clone());
        
        add(otherArea, BorderLayout.PAGE_END);
    }
    
    public WorldPage setCanvas(WorldCanvas w){
        canvas = w;
        canvasArea.removeAll();
        canvasArea.add(w);
        w.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                w.requestFocusInWindow();
            }
        });
        if(w.getWorld() instanceof RemoteProxyWorld || w.getWorld() instanceof HostWorld){
            chat.openChatServer();
        }
        SwingUtilities.invokeLater(()->w.requestFocusInWindow());
        chat.logLocal("Currently rendering World " + w.getWorld().hashCode());
        chat.logLocal("Rendered on WorldCanvas " + w.hashCode());
        revalidate();
        repaint();
        return this;
    }
}
