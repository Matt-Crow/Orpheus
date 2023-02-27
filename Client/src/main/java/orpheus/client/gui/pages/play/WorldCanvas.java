package orpheus.client.gui.pages.play;

import world.World;
import util.Settings;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.*;
import orpheus.client.gui.pages.PlayerControls;
import java.awt.Graphics2D;
import orpheus.client.gui.pages.Canvas;

/**
 * P: pause Z: zoom in X: zoom out
 *
 * @author Matt Crow
 */
public class WorldCanvas extends Canvas {

    /**
     * renders a world on this canvas
     */
    private final WorldGraphSupplier worldSupplier;

    /**
     * handles repainting - but not updating - the world
     */
    private final Timer repaintTimer;

    private final String focusedEntityId;
    private final HeadsUpDisplay hud;

    private boolean paused;

    /**
     * The caller should call WorldCanvas.start() once they are using the canvas
     */
    public WorldCanvas(WorldGraphSupplier worldSupplier, World w, PlayerControls pc) {

        super();

        this.worldSupplier = worldSupplier;

        paused = false;
        repaintTimer = new Timer(1000 / Settings.FPS, (ActionEvent e) -> {
            endOfFrame();
            repaint();
        });
        repaintTimer.setRepeats(true);
        repaintTimer.stop();

        registerKey(KeyEvent.VK_Z, true, () -> zoomIn());
        registerKey(KeyEvent.VK_X, true, () -> zoomOut());
        registerKey(KeyEvent.VK_P, true, () -> togglePause());
        setZoom(0.5);

        addMouseListener(pc);
        addEndOfFrameListener(pc);
        pc.registerControlsTo(this);

        focusedEntityId = pc.getPlayerId();
        hud = new HeadsUpDisplay(this, w, pc.getPlayerId());
    }

    public void start() {
        repaintTimer.start();
    }

    private void togglePause() {
        paused = !paused;
        if (paused) {
            repaintTimer.stop();
        } else {
            repaintTimer.start();
        }
        repaint();
    }

    //need this for when leaving world page
    public void stop() {
        paused = true;
        repaintTimer.stop();
    }

    @Override
    public void paintComponent(Graphics g) {
        var world = worldSupplier.get();

        super.paintComponent(g);

        var focus = world.getPlayers()
            .getMemberById(focusedEntityId);
        
        if (focus.isPresent()) {
            centerOn(
                focus.get().getX(),
                focus.get().getY()
            );
        }
        
        Graphics2D g2d = applyTransforms(g);

        world.draw(g2d);

        reset();

        hud.draw(g);

        if (world.getGame().isOver()) {
            drawMatchResolution(g2d);
        } else if (paused) {
            drawPause(g2d);
        }
        
        if(!isFocusOwner()){ // draw overlay if not focused
            g.setColor(new Color(155, 155, 155, 200));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    public void drawPause(Graphics g) {
        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.red);
        g.drawString("The game is paused", (int) (getWidth() * 0.3), (int) (getHeight() * 0.3));
        g.drawString("Press 'p' to continue", (int) (getWidth() * 0.4), (int) (getHeight() * 0.5));
    }

    public void drawMatchResolution(Graphics g) {
        var world = worldSupplier.get();
        paused = true;
        repaintTimer.stop();

        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.yellow);
        g.drawString("The match is ended,", (int) (getWidth() * 0.3), (int) (getHeight() * 0.3));
        String winner = (world.getGame().isPlayerWin())
            ? "players"
            : "enemies";
        g.drawString(winner, (int) (getWidth() * 0.5), (int) (getHeight() * 0.5));
        g.drawString("are victorious!", (int) (getWidth() * 0.7), (int) (getHeight() * 0.7));
    }
}
