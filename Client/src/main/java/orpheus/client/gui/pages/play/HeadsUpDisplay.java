package orpheus.client.gui.pages.play;

import gui.graphics.CustomColors;
import java.awt.Color;
import java.awt.Graphics;
import world.World;
import world.build.actives.AbstractActive;
import world.entities.AbstractPlayer;
import world.entities.HumanPlayer;

/**
 * provides a HeadsUpDisplay for an AbstractPlayer
 *
 * @author Matt Crow
 */
public class HeadsUpDisplay {
    private final WorldCanvas renderedOn;
    private final World world;
    private final String playerId;
    /*
    cannot directly reference a player, as they are serialized
    instead, world and playerId remain stable, so reference those
    */

    protected HeadsUpDisplay(WorldCanvas renderedOn, World world, String playerId) {
        this.renderedOn = renderedOn;
        this.world = world;
        this.playerId = playerId;
    }

    protected void draw(Graphics g) {
        int w = renderedOn.getWidth();
        int h = renderedOn.getHeight();

        // compass
        int compassX = w / 10 * 9; // center points
        int compassY = h / 10 * 3;
        int compassDiameter = w / 10;
        
        AbstractPlayer forPlayer = world.getPlayers().getMemberById(playerId);

        g.setColor(CustomColors.darkGrey);
        g.fillOval(compassX - compassDiameter, compassY - compassDiameter, compassDiameter * 2, compassDiameter * 2); // draws from upper-left corner, not center
        g.setColor(CustomColors.red);
        g.drawLine(
                compassX,
                compassY,
                (int) (compassX + forPlayer.getFacing().getXMod() * compassDiameter),
                (int) (compassY + forPlayer.getFacing().getYMod() * compassDiameter)
        );

        int guiY = (int) (h * 0.9);
        int sw = w / 5;
        int sh = h / 10;

        // HP
        String strHP = forPlayer.getLog().getHP() + "";
        g.setColor(Color.red);
        g.fillRect(0, guiY, sw, sh);
        g.setColor(Color.black);
        g.drawString("HP: " + strHP, (int) (w * 0.1), (int) (h * 0.93));

        // Actives
        if (forPlayer instanceof HumanPlayer) {
            int i = sw;
            for (AbstractActive a : ((HumanPlayer) forPlayer).getActives()) {
                a.drawStatusPane(g, i, (int) (h * 0.9), sw, sh);
                i += sw;
            }
        }
    }
}
