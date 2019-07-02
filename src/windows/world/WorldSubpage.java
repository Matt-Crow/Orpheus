package windows.world;

import gui.Chat;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import windows.Page;
import windows.SubPage;

/**
 * The WorldSubpage is used to render WorldCanvases.
 * So yes, it is rather convoluted:
 * use the MainWindow to render the WorldPage,
 * which renders the WorldSubpage,
 * which renders the WorldCanvas,
 * which renders the World.
 * 
 * @author Matt Crow
 */
public class WorldSubpage extends SubPage{
    private final JPanel canvasArea;
    private WorldCanvas canvas;
    private final Chat chat;
    
    public WorldSubpage(Page p){
        super(p);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        
        canvasArea = new JPanel();
        canvasArea.setBackground(Color.red);
        canvasArea.addFocusListener(new FocusListener(){
            @Override
            public void focusGained(FocusEvent e) {
                if(canvas != null){
                    System.out.println("focus");
                    canvas.requestFocus();
                }
            }

            @Override
            public void focusLost(FocusEvent e) {}
        });
        canvasArea.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                System.out.println("click");
                //canvasArea.requestFocusInWindow();
            }
        });
        canvasArea.setFocusable(true);
        canvasArea.setLayout(new GridLayout(1, 1));
        gbc.weighty = 9;
        add(canvasArea, gbc.clone());
        
        chat = new Chat();
        gbc.weighty = 1;
        gbc.gridy = 1;
        add(chat, gbc.clone());
    }
    
    public WorldSubpage setCanvas(WorldCanvas w){
        //canvasArea.removeAll();
        canvasArea.add(w);
        chat.logLocal("success? " + w.requestFocusInWindow());
        chat.logLocal("Currently rendering World " + w.getWorld());
        chat.logLocal("Rendered on WorldCanvas " + w);
        revalidate();
        repaint();
        return this;
    }
}
