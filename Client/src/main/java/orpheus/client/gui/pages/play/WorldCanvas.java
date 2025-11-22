package orpheus.client.gui.pages.play;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.UUID;

import orpheus.client.gui.pages.PlayerControls;
import orpheus.core.utils.timer.FrameTimer;
import orpheus.core.world.graph.particles.Particles;

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
     * renders particles on this canvas
     */
    private final Particles particles;

    /**
     * handles repainting - but not updating - the world
     */
    private final FrameTimer repaintTimer;

    /**
     * The ID of the player to center the camera on
     */
    private final UUID focusedEntityId;

    private boolean paused;

    /**
     * The caller should call WorldCanvas.start() once they are using the canvas
     */
    public WorldCanvas(WorldGraphSupplier worldSupplier, Particles particles, PlayerControls pc) {
        super();

        this.worldSupplier = worldSupplier;
        this.particles = particles;

        paused = false;
        repaintTimer = new FrameTimer(e -> repaint());

        registerKey(KeyEvent.VK_Z, true, () -> zoomIn());
        registerKey(KeyEvent.VK_X, true, () -> zoomOut());
        registerKey(KeyEvent.VK_P, true, () -> togglePause());
        setZoom(0.5);

        pc.registerControlsTo(this);

        focusedEntityId = pc.getPlayerId();
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
        particles.draw(g2d);

        reset();

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
