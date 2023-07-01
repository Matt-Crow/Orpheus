package orpheus.client.gui.pages.play;

import net.protocols.EndOfFrameListener;

import java.awt.FlowLayout;
import java.util.UUID;
import java.awt.Color;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * provides a HeadsUpDisplay for an AbstractPlayer
 *
 * @author Matt Crow
 */
public class HeadsUpDisplay extends JComponent implements EndOfFrameListener {
    
    /**
     * supplied player details
     */
    private final WorldGraphSupplier graph;

    /**
     * cannot directly reference a player, as they are serialized instead, world 
     * and playerId remain stable, so reference those
    */
    private final UUID playerId;

    /**
     * displays the player's remaining HP
     */
    private final JLabel playerHp;

    /**
     * displays the player's 3 active abilities
     */
    private final JLabel[] activeLabels;
    

    public HeadsUpDisplay(WorldGraphSupplier graph, UUID playerId) {

        setLayout(new FlowLayout(FlowLayout.CENTER));
        playerHp = new JLabel("---");
        playerHp.setOpaque(true);
        playerHp.setBackground(Color.RED);
        add(playerHp);

        activeLabels = new JLabel[3];
        for (var i = 0; i < 3; i++) {
            activeLabels[i] = new JLabel("---");
            activeLabels[i].setOpaque(true);
            add(activeLabels[i]);
        }

        this.graph = graph;
        this.playerId = playerId;
    }

    @Override
    public void frameEnded() {
        var maybePlayer = graph.get()
            .getPlayers()
            .getMemberById(playerId);
        
        if (!maybePlayer.isPresent()) {
            return; // exit early if player not found
        }

        var player = maybePlayer.get();
        playerHp.setText(String.format("HP: %4d", player.getHp()));

        var actives = player.getActives();
        for (var i = 0; i < actives.size() && i < activeLabels.length; i++) {
            var active = actives.get(i);
            var label = activeLabels[i];
            if (active.isAvailable()) {
                label.setText(actives.get(i).getName());
                label.setForeground(Color.BLACK);
                label.setBackground(Color.YELLOW);
            } else {
                label.setText(String.join(", ", active.getUnavailabilityMessages()));
                label.setForeground(Color.RED);
                label.setBackground(Color.BLACK);
            }    
        }
        
        repaint();
    }
}
