package windows;

import controllers.World;
import windows.WorldSelect.WorldSelectCanvas;

/**
 *
 * @author Matt
 */
public class WorldPage extends Page{
    public WorldPage(World w){
        super();
        addBackButton(new WorldSelectCanvas());
    }
}
