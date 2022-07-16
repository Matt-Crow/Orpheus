package orpheus.client.gui.pages.worldPlay;

import world.World;
import util.Settings;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.*;
import javax.swing.*;
import orpheus.client.gui.pages.PlayerControls;
import world.entities.AbstractPlayer;
import java.awt.Graphics2D;
import orpheus.client.gui.pages.Canvas;

/**
 * P: pause Z: zoom in X: zoom out
 *
 * @author Matt Crow
 */
public class WorldCanvas extends Canvas {

    private final World world;
    private final Timer timer;
    private final String focusedEntityId;
    private final HeadsUpDisplay hud;
    private final boolean pauseEnabled;

    private boolean paused;

    /**
     *
     * @param w
     * @param pc
     * @param pauseEnabled
     *
     * The caller should call WorldCanvas.start() once they are using the canvas
     */
    public WorldCanvas(World w, PlayerControls pc, boolean pauseEnabled) {
        super();
        world = w;

        paused = false;
        this.pauseEnabled = pauseEnabled;
        timer = new Timer(1000 / Settings.FPS, (ActionEvent e) -> {
            world.update();
            endOfFrame();
            repaint();
        });
        timer.setRepeats(true);
        timer.stop();

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
        timer.start();
    }

    private void togglePause() {
        if (pauseEnabled) {
            paused = !paused;
            if (paused) {
                timer.stop();
            } else {
                timer.start();
            }
            repaint();
        }
    }

    //need this for when leaving world page
    public void stop() {
        paused = true;
        timer.stop();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        AbstractPlayer focus = world.getPlayers().getMemberById(focusedEntityId);
        centerOn(
                focus.getX(),
                focus.getY()
        );
        Graphics2D g2d = applyTransforms(g);

        world.draw(g2d);

        reset();

        hud.draw(g);

        if (world.getGame().isOver()) {
            drawMatchResolution(g2d);
        } else if (paused) {
            drawPause(g2d);
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
        paused = true;
        timer.stop();

        g.setColor(new Color(0, 0, 0, 200));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.yellow);
        g.drawString("The match is ended,", (int) (getWidth() * 0.3), (int) (getHeight() * 0.3));
        String winner = (world.getGame().isPlayerWin())
            ? world.getPlayers().getName()
            : world.getAi().getName();
        g.drawString(winner, (int) (getWidth() * 0.5), (int) (getHeight() * 0.5));
        g.drawString("is victorious!", (int) (getWidth() * 0.7), (int) (getHeight() * 0.7));
    }
}
