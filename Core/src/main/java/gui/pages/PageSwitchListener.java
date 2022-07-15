package gui.pages;

/**
 * Used to detect when the user switches
 * from one Page to another in PageController.
 * 
 * Currently unused, not sure if I'll need it.
 * 
 * @author Matt Crow
 */
public interface PageSwitchListener {
    public void leavingPage(Page p);
    public void switchedToPage(Page p);
}
