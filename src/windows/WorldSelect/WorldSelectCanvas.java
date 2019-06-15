package windows.WorldSelect;

import java.awt.CardLayout;
import javax.swing.*;
import windows.DrawingPlane;

/**
 *
 * @author Matt
 */
public class WorldSelectCanvas extends DrawingPlane{
    private final JPanel body;
    private final String MAIN = "MAIN";
    private final String SOLO = "SOLO";
    private final String NEW_MULTIPLAYER = "NEW MULTIPLAYER";
    private final String JOIN_MULTIPLAYR = "JOIN MULTIPLAYER";
    public WorldSelectCanvas(){
        super();
        body = new JPanel(new CardLayout());
        body.add(new WSMain(), MAIN);
        add(body);
        resizeComponents(1, 1);
        ((CardLayout)body.getLayout()).show(body, MAIN);
    }
}
