package windows.world;

import controllers.MainWindow;
import windows.Page;
import windows.WorldSelect.WSMain;

/**
 * The World windows are a bit weird.
 * Since the WorldPage initializes sub-pages immediately upon construction,
 * it cannot have a sub-page for each World (that would get out of control really fast)
 * so it only has one sub-page, which contains a world canvas.
 * That canvas will change based upon which world the user is playing.
 * 
 * @author Matt Crow
 */
public class WorldPage extends Page{
    public WorldPage(MainWindow host){
        super(host);
        addBackButton(new WSMain());
        addSubPage("WORLD", new WorldSubpage(this));
        switchToSubpage("WORLD");
    }
    public WorldPage setCanvas(WorldCanvas w){
        ((WorldSubpage)getCurrentSubPage()).setCanvas(w);
        return this;
    }
}
